/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author FllodraB
 */
public class QAP {
    public static final int POPULATION_SIZE = 500;
    public static final int NUM_GENERATIONS = 100;
    public static final int TOURNAMENT_SIZE = 5;
    
    private static int numUnits;
    private static int[][] distances;
    private static int[][] flows;
    private static GA geneticAlgorithm;
    private static LamarckGA lamarckGeneticAlgorithm;
    private static BaldwinGA baldwinGeneticAlgorithm;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {

        /* Read from file */
        InputStream myInput = new FileInputStream("/Users/FllodraB/NetBeansProjects/TSP/src/tsp/qap.datos/tai256c.dat");
        Scanner scanner = new Scanner(myInput);
        numUnits = scanner.nextInt();
        distances = new int[numUnits][numUnits];
        flows = new int[numUnits][numUnits];

        // Recogemos datos para las matrices de distancia y coste (por eso se recorre 2 veces)
        for (int n = 0; n < 2; n++) {
            for (int i = 0; i < numUnits; i++) {
                for (int j = 0; j < numUnits; j++) {
                    if (n == 0) {
                        distances[i][j] = scanner.nextInt();
                    } else {
                        flows[i][j] = scanner.nextInt();
                    }
                }
            }
        }
        
        // Inicializar algoritmos
        geneticAlgorithm = new GA (distances, flows, numUnits);
        lamarckGeneticAlgorithm = new LamarckGA(distances, flows, numUnits);
        baldwinGeneticAlgorithm = new BaldwinGA(distances, flows, numUnits);
        
        // Ejecutar algoritmos
        geneticAlgorithm.evolvePopulation();
        lamarckGeneticAlgorithm.evolvePopulation();
        baldwinGeneticAlgorithm.evolvePopulation();
        
        // Selección del mejor resultado
        GenerateIndividual bestCandidate = geneticAlgorithm.getBestCandidate();
        GenerateIndividual bestCandidateLamarck = lamarckGeneticAlgorithm.getBestCandidate();
        GenerateIndividual bestCandidateBaldwin = baldwinGeneticAlgorithm.getBestCandidate();

        // Print final results
        System.out.println("\nParámetros:\n\tTamaño de población: "
                + QAP.POPULATION_SIZE + "\n\tNúmero de generaciones: "
                + QAP.NUM_GENERATIONS + "\n\tTamaño de torneo: "
                + QAP.TOURNAMENT_SIZE + "%\n\tProbabilidad de mutación: "
                + (0.015 * 100) + "%");

        // GA estandar
        System.out.println("\nAlgoritmo genético estándar\n\t");
        System.out.println(bestCandidate.toString() + " Coste asociado: " + bestCandidate.cost());
        
        // GA Lamarckiano
        System.out.println("\nAlgoritmo genético lamarckiano\n\t");
        System.out.println(bestCandidateLamarck.toString() + "\n\tCoste asociado: " + bestCandidateLamarck.cost());
        
        
        // GA Baldwiniano
        System.out.println("\nAlgoritmo genético baldwiniano\n\t");
        System.out.println(bestCandidateBaldwin.toString() + "\n\tCoste asociado: " + bestCandidateBaldwin.cost());
        }
}
