package genetic_algorithms.test;

import genetic_algorithms.operations.GeneticOperation;
import genetic_algorithms.operations.LinearSAController;
import genetic_algorithms.operations.Mutate;
import genetic_algorithms.operations.OperationGroup;
import genetic_algorithms.operations.SAMutate;
import genetic_algorithms.operations.SASinglePointCrossover;
import genetic_algorithms.operations.SinglePointCrossover;
import java.util.HashMap;

//@author Michael Haertling
public class OperationGroupTest {

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

        HashMap<String, Integer> counts = new HashMap<>();

        int times = 10000;
        for (int i = 0; i < times; i++) {
            GeneticOperation op = opg.selectOperation();
            Integer count = counts.get(op.getClass().getSimpleName());
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            counts.put(op.getClass().getSimpleName(), count);
        }
        if (print) {
            for(String op:counts.keySet()){
                System.out.println(op+": "+counts.get(op)+" ("+((100.0*counts.get(op))/times)+"%)");
            }
        }
        
        double acceptableVarience = 1;
        String[] cnames = {"Mutate","SAMutate","SinglePointCrossover","SASinglePointCrossover"};
        for(int i=0;i<cnames.length;i++){
            double percentage = (double)counts.get(cnames[i])/times;
            if(Math.abs(percentage-probs[i])>acceptableVarience){
                valid = false;
            }
        }
        
        return valid;
    }

}
