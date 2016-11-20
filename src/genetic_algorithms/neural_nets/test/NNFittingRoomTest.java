package genetic_algorithms.neural_nets.test;

import genetic_algorithms.main.Chromosome;
import genetic_algorithms.neural_nets.NeuralNetFittingRoom;
import genetic_algorithms.neural_nets.activation_functions.ActivationFunction;
import genetic_algorithms.neural_nets.activation_functions.StepActivationFunction;



//@author Michael Haertling

public class NNFittingRoomTest {
    
    public static void main(String[] args){
        //Create the data
        double[][] data = {
            {0,0,0},
            {0,1,1},
            {1,0,1},
            {1,1,0}
        };
        
        //Create the layer sizes
        int[] layerSizes = {2,2,1};
        
        //Create the activation function list
        StepActivationFunction sf = new StepActivationFunction();
        ActivationFunction[][] functions = {
            {sf,sf},
            {sf}
        };
        
        //Create the fitting room
        NeuralNetFittingRoom fr = new NeuralNetFittingRoom(data,layerSizes,functions);
        
        //Create the chromosome
        //Remember the first weight of each layer is the bias value
        double[] cvals = {
            -0.5,1,-1,
            -0.5,-1,1,
            -0.5,1,1
        };
        Chromosome c = new Chromosome(cvals);
        
        //Do the test
        fr.fitChromosome(c);
        
        System.out.println(c.getFitness());
    }
    
    
}
