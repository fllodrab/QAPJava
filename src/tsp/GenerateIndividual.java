package tsp;

import java.util.Random;

/**
 * Stores a candidate tour / Alamacena un tour candidato
 * @author FllodraB
 */
public class GenerateIndividual {
    private int numUnits;
    private int[][] distances;
    private int[][] flows;
    private int[] units;
    private double individualProfile;
    
    public GenerateIndividual(int[][] dstn, int[][] flws, int nUnits){
        numUnits = nUnits;
        distances = new int[numUnits][numUnits];
        System.arraycopy(dstn, 0, distances, 0, dstn.length);
        flows = new int[numUnits][numUnits];
        System.arraycopy(flws, 0, flows, 0, flws.length);
        
        units = new int[numUnits];
        
        for (int i = 0; i < numUnits; i++) {
            units[i] = i;
        }
        
        // Insertamos valores aleatorios en el vector de unidades
        Random aleatorio = new Random();
        for (int j = 0; j < numUnits; j++) {
            int posicion = aleatorio.nextInt(numUnits);
            int aux = units[j];
            units[j] = units[posicion];
            units[posicion] = aux;
        }
    }

    /**
     * Constructor de copia
     * @param generateIndividual 
     */
    GenerateIndividual(GenerateIndividual generateIndividual) {
        numUnits = generateIndividual.distances.length;
        distances = new int[numUnits][numUnits];
        System.arraycopy(generateIndividual.distances, 0, distances, 0, generateIndividual.distances.length);
        flows = new int[numUnits][numUnits];
        System.arraycopy(generateIndividual.flows, 0, flows, 0, generateIndividual.flows.length);
        units = new int[numUnits];
        System.arraycopy(generateIndividual.units, 0, units, 0, generateIndividual.units.length);
        
        numUnits = generateIndividual.numUnits;
        individualProfile = generateIndividual.individualProfile;
    }

    public int getUnit(int index) {
        return units[index];
    }
    
    public int getNumUnidades() {
        return numUnits;
    }
    
    public double getIndividualProfile() {
        return individualProfile;
    }
    
    public void setUnit(int index, int value) {
        units[index] = value;
    }
    
    public void setIndividualProfile() {
        individualProfile = cost();
    }
    
    /**
     * Función de coste o evaluación
     * @return 
     */
    public int cost() {
        int valor = 0;

        for (int i = 0; i < numUnits; i++) {
            for (int j = 0; j < numUnits; j++) {
                valor += flows[i][j] * distances[units[i]][units[j]];
            }
        }

        return valor;
    }
    
    @Override
    public String toString() {
        String cadena = null;
        
        System.out.println("Permutación del coste obtenido: ");
        
        for (int i : units) {
            cadena += " " +i;
        }

        return cadena.substring(0, cadena.length() - 1);
    }
}
