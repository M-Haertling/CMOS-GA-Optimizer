package genetic_algorithms.test;

import genetic_algorithms.test.utils.RandomizedFittingRoom;
import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.main.Chromosome;
import genetic_algorithms.operations.Mutate;
import java.util.Arrays;

//@author Michael Haertling
public class GAMutateTest {

    public static void main(String[] args) {
        System.out.println("Test returned " + autoTest(true) + ".");
    }

    public static boolean autoTest(boolean print) {
        boolean valid = true;

        //Create the operation
        double mutationRate = 0.9; // The chance that the mutation will occur
        double maxPerterbation = 0.5; // 2* the maximum diviation of values
        Mutate o = new Mutate(mutationRate, maxPerterbation);

        //Create the fitting room
        FittingRoom fr = new RandomizedFittingRoom(100);

        //Create the in/out
        double[] testd = {0.5, 0.5, 0.5, 0.5};
        double[] tmp = {0, 0, 0, 0};
        Chromosome[] in = new Chromosome[1];
        in[0] = new Chromosome(testd);
        Chromosome[] out = new Chromosome[1];
        out[0] = new Chromosome(tmp);

        if (print) {
            System.out.println("Test Data: " + Arrays.toString(testd));
        }
        int times = 30;
        for (int i = 0; i < times; i++) {
            o.operate(null, fr, in, out, true, false);
            if (print) {
                System.out.println(out[0].getFitness() + "\t" + Arrays.toString(out[0].getValues()));
            }
            for(double val:out[0].getValues()){
                if(val<0 || val>1){
                    valid = false;
                }
            }
        }
        return valid;
    }

}
