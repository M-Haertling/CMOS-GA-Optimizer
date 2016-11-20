package genetic_algorithms.neural_nets;

import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.main.BreedingChamber;
import genetic_algorithms.main.Chromosome;
import genetic_algorithms.main.ChromosomeFactory;
import genetic_algorithms.convergence.ConvergenceMonitor;
import genetic_algorithms.convergence.IterationBasedConvergenceMonitor;
import genetic_algorithms.main.RegressiveConvectionStorage;
import genetic_algorithms.neural_nets.activation_functions.ActivationFunction;
import genetic_algorithms.neural_nets.activation_functions.SigmoidActivationFunction;
import genetic_algorithms.neural_nets.activation_functions.StepActivationFunction;
import genetic_algorithms.operations.GeneticOperation;
import genetic_algorithms.operations.LinearSAController;
import genetic_algorithms.operations.Mutate;
import genetic_algorithms.operations.OperationGroup;
import genetic_algorithms.operations.SAMutate;
import genetic_algorithms.operations.SASinglePointCrossover;
import genetic_algorithms.operations.SinglePointCrossover;
import java.io.FileNotFoundException;
import java.util.Arrays;

//@author Michael Haertling
public class NeuralNetTrainer {

    public static void main(String[] args) throws FileNotFoundException {
        //Create the operations
        double mutationRate = 0.9; // The chance that the mutation will occur
        double maxPerterbation = 0.5; // 2* the maximum diviation of values
        int numThreads = 5;
        int populationSize = 100;
        int numElite = 1;
        double[] probs = {0.15, 0.35, 0.15, 0.35};

        GeneticOperation[] ops = {
            new Mutate(mutationRate, maxPerterbation),
            new SAMutate(mutationRate, maxPerterbation),
            new SinglePointCrossover(),
            new SASinglePointCrossover()
        };

        //Create the operation group
        OperationGroup opg = new OperationGroup(ops, probs, new LinearSAController(1,1000));

        //Create the layer sizes
        //There are 206 values in each instance
        //205 attributes
        //The hidden layer has 205 nodes and each node needs its own 205+1 weights
        //The output layer has only one node and 205 inputs, hence it has 205+1 weights
        //205*206+206
        int[] layerSizes = {205, 205, 1};

        //Create the activation function list
        StepActivationFunction stepf = new StepActivationFunction();
        SigmoidActivationFunction sigf = new SigmoidActivationFunction();
        ActivationFunction[][] functions = new ActivationFunction[2][];
        functions[0] = new ActivationFunction[205];
        functions[1] = new ActivationFunction[1];
        Arrays.fill(functions[0], sigf);
        Arrays.fill(functions[1], stepf);

        //Create the fitting room
        String dataPath = "files/train.nmv.txt";
        final FittingRoom fr = new NeuralNetFittingRoom(dataPath, " ", layerSizes, functions);

        //Create the RCS
        final RegressiveConvectionStorage rcs = new RegressiveConvectionStorage(populationSize, numElite, new ChromosomeFactory() {
            @Override
            public Chromosome generateChromosome() {
                double[] tmp = new double[42436];
                for (int i = 0; i < tmp.length; i++) {
                    tmp[i] = generateWeight();
                }
                Chromosome c = new Chromosome(tmp);
                return c;
            }

            public double generateWeight() {
                return Math.random() - Math.random();
            }
        },fr);

        System.out.println("Storage Instantiated...");
        
        //Create the convergence monitor
//        ConvergenceMonitor cm = new SumBasedConvergenceMonitor(rcs,2,0.1);
        ConvergenceMonitor cm = new IterationBasedConvergenceMonitor(2);

        NeuralNetGAMonitor gam = new NeuralNetGAMonitor("instances",rcs, opg.getSAController());

        BreedingChamber bc = new BreedingChamber(rcs, fr, opg, cm, gam, numThreads);

        bc.beginGeneticSearch();
    }

}
