package tsp;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author FllodraB
 */
public class LamarckGA {
    /* GA parameters */

    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 5;
    private GenerateIndividual[] population;
    private ArrayList<GenerateIndividual> generation;
    private final int numUnits;
    private final int[][] distances;
    private final int[][] flows;
    private GenerateIndividual bestCandidate;

    // Constructor
    LamarckGA(int[][] dstn, int[][] flws, int nUnits) {
        numUnits = nUnits;
        distances = new int[numUnits][numUnits];
        System.arraycopy(dstn, 0, distances, 0, dstn.length);
        flows = new int[numUnits][numUnits];
        System.arraycopy(flws, 0, flows, 0, flws.length);
    }

    /**
     * Obtener el mejor candidato según el coste
     *
     * @return
     */
    public GenerateIndividual getBestCandidate() {
        return bestCandidate;
    }

    /**
     * Iniciar población
     */
    private void initPopulation() {
        population = new GenerateIndividual[QAP.POPULATION_SIZE];

        for (int i = 0; i < QAP.POPULATION_SIZE; i++) {
            population[i] = new GenerateIndividual(distances, flows, numUnits);
            population[i].setIndividualProfile();
        }

        bestCandidate = new GenerateIndividual(evaluate(population));
    }

    // Evoluciona la poblocación / Evolves a population over one generation
    public void evolvePopulation() {
        // Iniciamos población
        initPopulation();

        System.out.println("\nAlgoritmo Genético Lamarckiano\n");
        // Crossover population / hacemos cruce
        for (int i = 1; i <= QAP.NUM_GENERATIONS; i++) {
            generation = new ArrayList<GenerateIndividual>();
            for (int j = 0; j < QAP.POPULATION_SIZE / 2; j++) {
                // Select parents / Selección de padres
                GenerateIndividual father = new GenerateIndividual(tournamentSelection(QAP.POPULATION_SIZE));
                GenerateIndividual son = new GenerateIndividual(crossover(father));

                // Mutate the new population a bit to add some new genetic material
                // Mutación
                mutate(son);

                // set profile / establecer aptitud a los hijos
                son.setIndividualProfile();

                // Add child to new population / Añadimos hijos a la población
                generation.add(son);
            }
            // Nueva población / New Population
            population = generation.toArray(new GenerateIndividual[QAP.POPULATION_SIZE]);
            bestCandidate = new GenerateIndividual(evaluate(population));

            System.out.println("Generación " + i + ": " + bestCandidate.getIndividualProfile());
        }
    }

    /**
     * Applies crossover to a set of parents and creates offspring / Cruce de
     * genes y creación de descendencia
     *
     * @param father1 Un Padre
     * @return offspring Descendencia
     */
    public GenerateIndividual crossover(GenerateIndividual father1) {
        GenerateIndividual father2 = bestCandidate; // Se considera siempre el segundo padre el mejor individuo de la población
        GenerateIndividual son = new GenerateIndividual(father1);

        // Get start position for parent's gens
        int startPos = (int) (Math.random() * numUnits);

        // Loop and add the sub tour from parent1 and parent 2 to our childs
        // Iterar añadiendo una parte de un padre y otra del otro padre a los hijos
        for (int i = startPos; i < numUnits; i++) {
            son.setUnit(i, father2.getUnit(i));
        }

        return son;
    }

    // Mutate a tour using swap mutation / Mutación intercambiando posiciones
    private void mutate(GenerateIndividual child) {
        // Loop through our childs
        for (int i = 0; i < numUnits; i++) {
            // Apply mutation rate
            // Aplicamos ratio de mutación
            if (Math.random() < mutationRate) {
                int point1 = 0;
                int point2 = 0;

                while (point1 == point2) {
                    point1 = (int) Math.floor(Math.random() * numUnits);
                    point2 = (int) Math.floor(Math.random() * numUnits);
                }

                int aux = child.getUnit(point1);
                // Swap them around
                // Intercambio
                child.setUnit(point1, child.getUnit(point2));
                child.setUnit(point1, aux);
            }
        }
    }

    // Selects candidate for crossover
    // Selección candidatos por torneo para el cruce
    private GenerateIndividual tournamentSelection(int pop) {
        GenerateIndividual[] tournament = new GenerateIndividual[QAP.TOURNAMENT_SIZE];

        // Random competitor selection
        // Elección de los participantes de forma aleatoria
        for (int i = 0; i < QAP.TOURNAMENT_SIZE; i++) {
            int competitor = (int) Math.floor(Math.random() * pop);
            tournament[i] = new GenerateIndividual(population[competitor]);
        }

        // Return best competitor
        return evaluate(tournament);
    }

    /**
     * Evaluar población
     *
     * @param population Población
     * @return bCandidate Mejor candidato
     */
    private GenerateIndividual evaluate(GenerateIndividual[] pop) {
        GenerateIndividual bCandidate;
        int bestPosition = 0;
        double bestProfile = 0;

        for (int i = 0; i < pop.length; i++) {
            for (int j = i + 1; j < QAP.POPULATION_SIZE - 1; j++) {
                GenerateIndividual aux = population[j];
                population[j] = population[i];
                population[i] = aux;

                if (pop[i].getIndividualProfile() < bestProfile) {
                    bestPosition = i;
                    bestProfile = pop[i].getIndividualProfile();
                }
            }
        }

        bCandidate = new GenerateIndividual(pop[bestPosition]);

        return bCandidate;
    }
}
