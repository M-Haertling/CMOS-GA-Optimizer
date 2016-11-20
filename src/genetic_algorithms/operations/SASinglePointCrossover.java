package genetic_algorithms.operations;

import genetic_algorithms.main.Chromosome;
import genetic_algorithms.fitting.FittingRoom;

//@author Michael Haertling
public class SASinglePointCrossover extends SAGeneticOperation {


    @Override
    public void operate(ChromosomeQueue queue, FittingRoom fr, Chromosome[] in, Chromosome[] out, boolean fit, boolean deduct) {
        super.operate(queue, fr, in, out, fit, deduct);

        if (!in[0].hasBeenFitted()) {
            fr.fitChromosome(in[0]);
        }
        if (!in[1].hasBeenFitted()) {
            fr.fitChromosome(in[1]);
        }

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
        //Fit the chromosomes
        fr.fitChromosome(out[0]);
        fr.fitChromosome(out[1]);

        //Place the best child at index 0, remove/return the other
        if (out[1].getFitness() > out[0].getFitness()) {
            queue.push(out[0]);
            out[0] = out[1];
        } else {
            queue.push(out[1]);
        }
        out[1] = null;

        //Find the best parent
        int bestP = 0;
        if (in[1].getFitness() > in[0].getFitness()) {
            bestP = 1;
        }

        //Decide if the child should be kept
        if (in[bestP].getFitness() > out[0].getFitness()) {
            if (!this.shouldAcceptChild(in[bestP].getFitness(), out[0].getFitness())) {
                //Decline the child
                in[0].copyAllTo(out[0]);
            }
        }

//        System.out.println("SASPC: " + out[0].getFitness());

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
        return 1;
    }

}
