package genetic_algorithms.operations;

import genetic_algorithms.main.Chromosome;
import genetic_algorithms.fitting.FittingRoom;

//@author Michael Haertling
public abstract class GeneticOperation {

    /**
     * Performs the operation on the specified chromosomes.
     *
     * @param queue
     * @param fr
     * @param in
     * @param out
     * @param fit
     * @param deduct True if the output chromosomes should be deducted from the
     * number of remaining chromosomes
     */
    public void operate(ChromosomeQueue queue, FittingRoom fr, Chromosome[] in, Chromosome[] out, boolean fit, boolean deduct) {
        if (deduct) {
            queue.finalizeChromosomes(numFinalOutputs());
        }
    }

    /**
     *
     * @return The number of inputs this operation accepts.
     */
    public abstract int numInputs();

    /**
     *
     * @return The number of output chromosomes this operation requires.
     */
    public abstract int numOutputs();

    /**
     *
     * @return The number of output chromosomes this operation keeps.
     */
    public abstract int numFinalOutputs();
}
