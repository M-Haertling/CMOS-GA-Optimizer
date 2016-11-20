package genetic_algorithms.operations;

import genetic_algorithms.main.Chromosome;
import genetic_algorithms.fitting.FittingRoom;

//@author Michael Haertling
public class Copy extends GeneticOperation {

    final private int numCopies;

    public Copy(int numCopies) {
        this.numCopies = numCopies;
    }

    @Override
    public void operate(ChromosomeQueue queue, FittingRoom fr, Chromosome[] in, Chromosome[] out, boolean fit, boolean deduct) {
        super.operate(queue, fr, in, out, fit, deduct);
        if (deduct) {
            queue.finalizeChromosomes(numFinalOutputs());
        }
        in[0].copyAllTo(out[0]);
    }

    @Override
    public int numInputs() {
        return 1;
    }

    @Override
    public int numOutputs() {
        return numCopies;
    }

    @Override
    public int numFinalOutputs() {
        return numOutputs();
    }

}
