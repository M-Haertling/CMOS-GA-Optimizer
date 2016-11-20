package genetic_algorithms.test;

import genetic_algorithms.Utils;
import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.main.Chromosome;
import genetic_algorithms.main.ChromosomeFactory;
import genetic_algorithms.main.RegressiveConvectionStorage;
import genetic_algorithms.test.utils.RandomizedFittingRoom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

//@author Michael Haertling
public class RCSTest {

    public static void main(String[] args) {
        System.out.println("Test returned " + autoTest(true) + ".");
    }

    public static boolean autoTest(boolean print) {
        boolean valid = true;
        
        //Create the fitting room
        FittingRoom fr = new RandomizedFittingRoom(100);
        
        //Create the RCS
        int populationSize = 10;
        int numElite = 2;
        RegressiveConvectionStorage rcs = new RegressiveConvectionStorage(populationSize, numElite, new ChromosomeFactory() {
            @Override
            public Chromosome generateChromosome() {
                double[] tmp = {0, 0, 0, 0};
                return new Chromosome(tmp);
            }
        },fr);

        

        Chromosome[] buffer = new Chromosome[10];

        //Gather chromosomes
        if (print) {
            System.out.println("Doing initial chromosome fitting");
        }
        int previous = rcs.getNumRemaining();
        while (rcs.getNumRemaining() > 0) {
            rcs.getChromosomes(buffer, 1);
            if (previous != rcs.getNumRemaining() + 1) {
                valid = false;
            }
            previous = rcs.getNumRemaining();
            for (int i = 1; i < buffer.length; i++) {
                if (buffer[i] != null) {
                    valid = false;
                }
            }
            fr.fitChromosome(buffer[0]);
            if (print) {
                System.out.println(buffer[0].getFitness());
            }
        }

        if (print) {
            System.out.println("Convecting");
        }

        rcs.convect();

        if (print) {
            System.out.println("Testing roulette selection");
            System.out.println("Sum Array: " + Arrays.toString(rcs.getSummationArray()));
        }

        HashMap<Double, Integer> counts = new HashMap<>();

        for (int i = 0; i < 1000; i++) {
            double stop = Math.random() * rcs.getFitnessSum();
            int index = Utils.rouletteBinarySearch(stop, rcs.getSummationArray());
            Chromosome c = rcs.getHistoricalSet()[index];
            Integer count = counts.get(c.getFitness());
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            counts.put(c.getFitness(), count);
        }

        if (print) {
            ArrayList<Double> keys = new ArrayList<>(counts.keySet());
            Collections.sort(keys);
            for (Double key : keys) {
                System.out.println(counts.get(key) + "\t|" + key);
            }
        }

        return valid;
    }

}
