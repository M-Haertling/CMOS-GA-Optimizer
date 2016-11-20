package genetic_algorithms.operations;

import genetic_algorithms.Utils;
import java.util.Arrays;

//@author Michael Haertling
public class OperationGroup {

    private final GeneticOperation[] operations;
    private final double[] operationSums;
    private final int maxIn;
    private final int maxOut;
    private final SAController sac;

    public OperationGroup(GeneticOperation[] ops, double[] opChance, SAController sac) {
        operations = ops;
        this.sac = sac;
        operationSums = new double[opChance.length];
        //Create normalized summation array
        //Create the summation array
        int mi = ops[0].numInputs();
        int mo = ops[0].numOutputs();
        operationSums[0] = opChance[0];
        for (int i = 1; i < opChance.length; i++) {
            operationSums[i] = opChance[i] + operationSums[i-1];
            if (ops[i].numInputs() > mi) {
                mi = ops[i].numInputs();
            }
            if (ops[i].numOutputs() > mo) {
                mo = ops[i].numOutputs();
            }
            if (sac != null && ops[i] instanceof SAGeneticOperation) {
                sac.addOperation((SAGeneticOperation) ops[i]);
            }
        }
        maxIn = mi;
        maxOut = mo;
        //Normalize the array
        Utils.normalizeSumArray(operationSums);
    }

    /**
     *
     * @return A GeneticOperation in accordance to the probabilities set at
     * instantiation.
     */
    public GeneticOperation selectOperation() {
        double rand = Math.random();
        return operations[Utils.rouletteBinarySearch(rand, operationSums)];
    }

    public int getMaxNumberOfInputs() {
        return maxIn;
    }

    public int getMaxNumberOfOutputs() {
        return maxOut;
    }
    
    public SAController getSAController(){
        return sac;
    }
}
