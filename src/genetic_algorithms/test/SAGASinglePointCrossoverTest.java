package genetic_algorithms.test;

import genetic_algorithms.test.utils.RandomizedFittingRoom;
import genetic_algorithms.test.utils.EmptyChromosomeQueue;
import genetic_algorithms.operations.LinearSAController;
import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.main.Chromosome;
import genetic_algorithms.operations.SAController;
import genetic_algorithms.operations.SASinglePointCrossover;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

//@author Michael Haertling
public class SAGASinglePointCrossoverTest {

    public static void main(String[] args) {
        System.out.println("Test returned " + autoTest(true) + ".");
    }

    public static boolean autoTest(boolean print) {
        boolean valid = true;

        //Create the operation
        SASinglePointCrossover o = new SASinglePointCrossover();

        //Place the operation in SAController
        int timesOuter = 50; //Number of temperatures run at
        int timesInner = 100; //Times run at each temperature
        double startingTemp = 1000;
        SAController sac = new LinearSAController(startingTemp / timesOuter,startingTemp);
        sac.addOperation(o);
        timesOuter++;

        //Create the fitting room
        FittingRoom fr = new RandomizedFittingRoom(100);

        //Create the in/out
        double[] testd1 = {1, 1, 1, 1};
        double[] testd2 = {0, 0, 0, 0};
        double[] tmp1 = {0, 0, 0, 0};
        double[] tmp2 = {0, 0, 0, 0};
        Chromosome[] in = new Chromosome[2];
        in[0] = new Chromosome(testd1);
        in[1] = new Chromosome(testd2);
        //Fit the input chromosomes
        fr.fitChromosome(in[0]);
        fr.fitChromosome(in[1]);
        //Create the output chromosomes
        Chromosome[] out = new Chromosome[2];
        out[0] = new Chromosome(tmp1);
        out[1] = new Chromosome(tmp2);

        EmptyChromosomeQueue queue = new EmptyChromosomeQueue();

        if (print) {
            System.out.println("Test Data 1: " + Arrays.toString(testd1)+" "+in[0].getFitness());
            System.out.println("Test Data 2: " + Arrays.toString(testd2)+" "+in[1].getFitness());
        }

        HashMap<Double, Integer> tempMap = new HashMap<>();
        int acceptSmaller = 0;
        for (int outer = 0; outer < timesOuter; outer++) {
            int acceptSmallerInner = 0;
            for (int i = 0; i < timesInner; i++) {
                o.operate(queue, fr, in, Arrays.copyOf(out, 2), true, false);
                if (print) {
                    System.out.println(out[0].getFitness() + "\t" + Arrays.toString(out[0].getValues()));
                    if (out[0].getFitness() < in[0].getFitness() && out[0].getFitness() < in[1].getFitness()) {
                        System.out.println("Accepted smaller fitness value.");
                        acceptSmaller++;
                        acceptSmallerInner++;
                    }
                }
                double initial = out[0].getValues()[0];
                boolean changed = false;
                for (double val : out[0].getValues()) {
                    if (val != initial) {
                        changed = true;
                    }
                    if (changed && val == initial) {
                        valid = false;
                    }
                }

            }
            //Save the number of smaller accepts
            tempMap.put(sac.getCurrentTemperature(), acceptSmallerInner);
            //Advance the SA schedule
            sac.advanceSchedule();
            if (print) {
                System.out.println("~SA TEMPERATURE SET TO: " + sac.getCurrentTemperature());
            }
        }
        if (print) {
            double percent = ((double) acceptSmaller / (timesInner * timesOuter));
            System.out.println("*Total Percent Smaller Accepted: " + percent);
            ArrayList<Double> orderedKeys = new ArrayList<>(tempMap.keySet());
            Collections.sort(orderedKeys);
            for (int i = orderedKeys.size() - 1; i >= 0; i--) {
                Double key = orderedKeys.get(i);
                System.out.println("*Percent Smaller Accepted at (" + key + "): " + ((double) tempMap.get(key) / timesInner));
            }
            if(!valid){
                System.out.println("Invalid crossover.");
            }
        }
        return valid;
    }

}
