package genetic_algorithms.operations;

import genetic_algorithms.main.Chromosome;
import genetic_algorithms.fitting.FittingRoom;

//@author Michael Haertling
public class SinglePointCrossover extends GeneticOperation {

    @Override
    public void operate(ChromosomeQueue queue, FittingRoom fr, Chromosome[] in, Chromosome[] out, boolean fit, boolean deduct) {
        super.operate(queue, fr, in, out, fit, deduct);

        double[] chrom1 = in[0].getValues();
        double[] chrom2 = in[1].getValues();
        double[] child1 = out[0].getValues();
        double[] child2 = out[1].getValues();
        //Choose the crossover point x>0 and x<chrom1.length
        int point = (int) (Math.random() * (chrom1.length - 1)) + 1;
        //Perform the crossover
        //Copy up to the crossover point
        for (int i = 0; i < point; i++) {
            child1[i] = chrom1[i];
            child2[i] = chrom2[i];
        }
        //Flip the copy for the rest
        for (int i = point; i < chrom1.length; i++) {
            child1[i] = chrom2[i];
            child2[i] = chrom1[i];
        }

        //Fit the new chromosomes if needed
        if (fit) {
            fr.fitChromosome(out[0]);
            fr.fitChromosome(out[1]);
        }

//        System.out.println("SPC: " + out[0].getFitness() + " " + out[1].getFitness());

    }

    @Override
    public int numInputs() {
        return 2;
    }

    @Override
    public int numOutputs() {
        return 2;
    }

    @Override
    public int numFinalOutputs() {
        return numOutputs();
    }

}
