package genetic_algorithms.test;

import genetic_algorithms.test.utils.RandomizedFittingRoom;
import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.main.Chromosome;
import genetic_algorithms.operations.SinglePointCrossover;
import java.util.Arrays;

//@author Michael Haertling
public class GASinglePointCrossoverTest {

    public static void main(String[] args) {
        System.out.println("Test returned " + autoTest(true) + ".");
    }

    public static boolean autoTest(boolean print) {
        boolean valid = true;

        //Create the operation
        SinglePointCrossover o = new SinglePointCrossover();

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
        Chromosome[] out = new Chromosome[2];
        out[0] = new Chromosome(tmp1);
        out[1] = new Chromosome(tmp2);

        if (print) {
            System.out.println("Test Data 1: " + Arrays.toString(testd1));
            System.out.println("Test Data 2: " + Arrays.toString(testd2));
        }
        int times = 30;
        for (int i = 0; i < times; i++) {
            o.operate(null, fr, in, out, true, false);
            if (print) {
                System.out.println(out[0].getFitness() + "\t" + Arrays.toString(out[0].getValues()));
                System.out.println("\t\t\t" + Arrays.toString(out[1].getValues()));
            }
            for (int k = 0; k < 2; k++) {
                double initial = out[k].getValues()[0];
                boolean changed = false;
                for (double val : out[k].getValues()) {
                    if (val != initial) {
                        changed = true;
                    }
                    if (changed && val == initial) {
                        valid = false;
                    }
                }
            }
        }
        return valid;
    }

}
