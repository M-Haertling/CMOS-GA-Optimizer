package genetic_algorithms.operations;

import genetic_algorithms.main.Chromosome;
import genetic_algorithms.fitting.FittingRoom;

//@author Michael Haertling
public class Mutate extends GeneticOperation {

    /**
     * The percentage of the time that a gene is mutated.
     */
    private final double mutationRate;

    /**
     * 2 times the max mutation value (up or down).
     */
    private final double maxPerturbation;

    public Mutate(double mutationRate, double maxPerturbation) {
        this.maxPerturbation = maxPerturbation;
        this.mutationRate = mutationRate;
    }

    /**
     * Mutates the chromosome values and places them in the child chromosome.
     * Uses mutationRate and maxPerturbation. This function will override the
     * child array with new values. This function is thread safe.
     *
     * @param queue
     * @param fr
     * @param in
     * @param out
     * @param fit
     */
    @Override
    public void operate(ChromosomeQueue queue, FittingRoom fr, Chromosome[] in, Chromosome[] out, boolean fit, boolean deduct) {
        super.operate(queue, fr, in, out, fit, deduct);
        double[] child = out[0].getValues();
        double[] chrom = in[0].getValues();
        for (int i = 0; i < child.length; i++) {
            if (Math.random() < mutationRate) {
                //Mutate the data
                child[i] = chrom[i] + (Math.random() - Math.random()) * maxPerturbation;
            } else {
                child[i] = chrom[i];
            }
        }
        if (fit && !out[0].hasBeenFitted()) {
            fr.fitChromosome(out[0]);
        }

//        System.out.println("Mutate: " + out[0].getFitness());
    }

    @Override
    public int numInputs() {
        return 1;
    }

    @Override
    public int numOutputs() {
        return 1;
    }

    @Override
    public int numFinalOutputs() {
        return numOutputs();
    }

}
