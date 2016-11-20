package genetic_algorithms.test;

import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.main.BreedingChamber;
import genetic_algorithms.main.BreedingThread;
import genetic_algorithms.main.Chromosome;
import genetic_algorithms.main.ChromosomeFactory;
import genetic_algorithms.main.ConvectionStorage;
import genetic_algorithms.convergence.ConvergenceMonitor;
import genetic_algorithms.main.RegressiveConvectionStorage;
import genetic_algorithms.operations.GeneticOperation;
import genetic_algorithms.operations.LinearSAController;
import genetic_algorithms.operations.Mutate;
import genetic_algorithms.operations.OperationGroup;
import genetic_algorithms.operations.SAMutate;
import genetic_algorithms.operations.SASinglePointCrossover;
import genetic_algorithms.operations.SinglePointCrossover;
import genetic_algorithms.test.utils.RandomizedFittingRoom;

//@author Michael Haertling
public class BreedingChamberTest {

    public static void main(String[] args) {
        System.out.println("Test returned " + autoTest(true) + ".");
    }

    public static boolean autoTest(boolean print) {
        boolean valid = true;

        //Create the operations
        double mutationRate = 0.9; // The chance that the mutation will occur
        double maxPerterbation = 0.5; // 2* the maximum diviation of values
        GeneticOperation[] ops = {
            new Mutate(mutationRate, maxPerterbation),
            new SAMutate(mutationRate, maxPerterbation),
            new SinglePointCrossover(),
            new SASinglePointCrossover()
        };
        double[] probs = {0.2, 0.3, 0.2, 0.3};

        //Create the operation group
        OperationGroup opg = new OperationGroup(ops, probs, new LinearSAController(10,1000));
        
        //Create the fitting room
        FittingRoom fr = new RandomizedFittingRoom(100); 
        
        //Create the RCS
        int populationSize = 10;
        int numElite = 2;
        final RegressiveConvectionStorage rcs = new RegressiveConvectionStorage(populationSize, numElite, new ChromosomeFactory() {
            @Override
            public Chromosome generateChromosome() {
                double[] tmp = {0, 0, 0, 0};
                return new Chromosome(tmp);
            }
        },fr);

        

        //Create the convergence monitor
        ConvergenceMonitor cm = new ConvergenceMonitor() {
            @Override
            public boolean converged() {
                double biggest = rcs.getHistoricalSet()[rcs.getMostElite()].getFitness();
                return biggest > 95;
            }
        };

        BreedingChamber bc = new BreedingChamber(rcs, fr, opg, cm, null, 2);

//        bc.performOperation(rcs, fr, bc.getThreads()[0]);
//        bc.performEvolution(rcs, fr, bc.getThreads()[0]);
        bc.beginGeneticSearch();

        return valid;
    }

}
