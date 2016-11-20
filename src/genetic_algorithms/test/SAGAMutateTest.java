package genetic_algorithms.test;

import genetic_algorithms.test.utils.RandomizedFittingRoom;
import genetic_algorithms.operations.LinearSAController;
import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.main.Chromosome;
import genetic_algorithms.operations.SAController;
import genetic_algorithms.operations.SAMutate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

//@author Michael Haertling
public class SAGAMutateTest {

    public static void main(String[] args) {
        System.out.println("Test returned " + autoTest(true) + ".");
    }

    public static boolean autoTest(boolean print) {
        boolean valid = true;

        //Create the operation
        double mutationRate = 0.9; // The chance that the mutation will occur
        double maxPerterbation = 0.5; // 2* the maximum diviation of values
        SAMutate o = new SAMutate(mutationRate, maxPerterbation);

        //Place the operation in SAController
        int timesOuter = 50; //Number of temperatures run at
        int timesInner = 100; //Times run at each temperature
        double startingTemp = 1000;
        SAController sac = new LinearSAController(startingTemp/timesOuter,startingTemp);
        sac.addOperation(o);
        timesOuter++;
        
        //Create the fitting room
        FittingRoom fr = new RandomizedFittingRoom(100);

        //Create the in/out
        double[] testd = {0.5, 0.5, 0.5, 0.5};
        double[] tmp = {0, 0, 0, 0};
        Chromosome[] in = new Chromosome[1];
        in[0] = new Chromosome(testd);
        fr.fitChromosome(in[0]);
        Chromosome[] out = new Chromosome[1];
        out[0] = new Chromosome(tmp);

        if (print) {
            System.out.println("Test Data: " + in[0].getFitness() + "\t" + Arrays.toString(testd));
        }
        HashMap<Double,Integer> tempMap = new HashMap<>();
        int acceptSmaller = 0;
        for (int outer = 0; outer < timesOuter; outer++) {
            int acceptSmallerInner = 0;
            for (int i = 0; i < timesInner; i++) {
                o.operate(null, fr, in, out, true, false);
                if (print) {
                    System.out.println(out[0].getFitness() + "\t" + Arrays.toString(out[0].getValues()));
                    if (out[0].getFitness() < in[0].getFitness()) {
                        System.out.println("Accepted smaller fitness value.");
                        acceptSmaller++;
                        acceptSmallerInner++;
                    }
                }
                for (double val : out[0].getValues()) {
                    if (val < 0 || val > 1) {
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
            double percent = ((double)acceptSmaller/(timesInner*timesOuter));
            System.out.println("*Total Percent Smaller Accepted: "+percent);
            ArrayList<Double> orderedKeys = new ArrayList<>(tempMap.keySet());
            Collections.sort(orderedKeys);
            for(int i=orderedKeys.size()-1;i>=0;i--){
                Double key = orderedKeys.get(i);
                System.out.println("*Percent Smaller Accepted at ("+key+"): "+((double)tempMap.get(key)/timesInner));
            }
        }
        return valid;
    }

}
