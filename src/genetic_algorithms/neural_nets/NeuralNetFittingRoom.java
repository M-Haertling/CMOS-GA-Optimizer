package genetic_algorithms.neural_nets;

import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.main.Chromosome;
import genetic_algorithms.neural_nets.activation_functions.ActivationFunction;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//@author Michael Haertling
public class NeuralNetFittingRoom extends FittingRoom {

    private final double[][] data;
    private final int[] layerSizes;
    private final ActivationFunction[][] activationFunctions;

    public NeuralNetFittingRoom(String path, String delimeter, int[] layerSizes, ActivationFunction[][] activationFunctions) throws FileNotFoundException {
        data = loadData(path,delimeter);
        this.layerSizes = layerSizes;
        this.activationFunctions = activationFunctions;
    }

    public NeuralNetFittingRoom(double[][] data, int[] layerSizes, ActivationFunction[][] activationFunctions) {
        this.data = data;
        this.layerSizes = layerSizes;
        this.activationFunctions = activationFunctions;
    }

    private double[][] loadData(String path,String delimiter) throws FileNotFoundException {
        double[][] dta;
        Scanner in = new Scanner(new File(path));
        ArrayList<double[]> temp = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] split = line.split(delimiter);
            double[] values = new double[split.length];
            for (int i = 0; i < split.length; i++) {
                values[i] = Double.parseDouble(split[i]);
            }
            temp.add(values);
        }
        dta = new double[temp.size()][];
        for (int i = 0; i < temp.size(); i++) {
            dta[i] = temp.get(i);
        }
        return dta;
    }

    @Override
    public double calculateChromosomeFitness(Chromosome c) {
        double[] values = c.getValues();
        //Loop over all the test instances
        int correct = 0;
        for (double[] instance : data) {
//            System.out.println("Instance: "+Arrays.toString(instance));
            if (testInstance(instance, values)) {
                correct++;
            }
        }
//        System.out.println(correct);
        //Find the accuracy percentage
        double accuracy = (double) (correct * 100) / data.length;
//        return Math.pow(accuracy, 2);
        return accuracy;
    }

    /**
     *
     * @param instance
     * @param weights
     * @return True if the instance was classified correctly.
     */
    public boolean testInstance(double[] instance, double[] weights) {
        ArrayList<Double> comsp = new ArrayList<>();
        ArrayList<Double> comsn = new ArrayList<>();
        ArrayList<Double> tmp;
        int weightIndex = 0;
        //Do initial computations with the input values
        for (int i = 0; i < layerSizes[0]; i++) {
            comsp.add(instance[i]);
        }
        //Loop through each layer
        for (int l = 1; l < layerSizes.length; l++) {
            for (int i = 0; i < layerSizes[l]; i++) {
                //Calculate the dot product
                //Get the bias value
                double value = weights[weightIndex++];
//                System.out.print(weights[weightIndex-1]);
                for (int k = 0; k < comsp.size(); k++) {
                    value += comsp.get(k) * weights[weightIndex++];
//                    System.out.print(" + "+comsp.get(k)+"*"+weights[weightIndex-1]);
                }
//                System.out.print(" = "+value);
                //Run the activation function (if exists)
                ActivationFunction af = activationFunctions[l - 1][i];
                if (af != null) {
                    value = af.activate(value);
                }
//                System.out.println(" = "+value);
                //Save the computation result
                comsn.add(value);
            }
//            System.out.println();
            //Switch the computation arrays (the old values are now useless)
            comsp.clear();
            tmp = comsp;
            comsp = comsn;
            comsn = tmp;
        }
        //Check the final computation results with the remaining portion of the 
        //instance array
        int instanceInc = layerSizes[0];
        for (int i = 0; i < instance.length - layerSizes[0]; i++) {
//            System.out.println("Expected: "+instance[i+instanceInc]+" Output: "+comsp.get(i));
            if (instance[i+instanceInc] != comsp.get(i)) {
                return false;
            }
        }
        return true;
    }
}
