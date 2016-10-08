package genetic_algorithms;

/**
 * The house for all genetic algorithm functions. This algorithm is designed to
 * handle order specific, fixed length chromosomes.
 *
 * @author Michael
 */
public class BreedingChamber {

    /**
     * The percentage of the time that a gene is mutated.
     */
    double mutationRate;
    /**
     * 2 times the max mutation value (up or down).
     */
    double maxPerturbation;
    /**
     * The rate at which crossovers are performed.
     */
    double crossoverRate;
    /**
     * The number of elite chromosomes to add per round of evolution.
     */
    int numElite;
    /**
     * The the number of times to copy each elite chromosome.
     */
    int eliteCopies;

    public BreedingChamber(double mr, double mp, double cr, int numElite, int eliteCopies) {
        mutationRate = mr;
        maxPerturbation = mp;
        crossoverRate = cr;
        this.numElite = numElite;
        this.eliteCopies = eliteCopies;
    }

    /**
     * Begin evolving the array of chromosomes given their fitness values.
     *
     * @param fit An array of fitness values corresponding to each of the
     * chromosomes in the array.
     */
    public void performEvolution(FitnessBlock fit) {
        //Add elite
        //Loop till population is full
        //Perform crossover
        //Mutate the results
        //Add to population
        //End
    }

    /**
     * Adds the elite most chromosomes to the new set. Uses numElite and
     * eliteCopies.
     * @param fit
     * @param store
     */
    public void addElite(FitnessBlock fit, ConvectionStorage store) {
        int[] elite = fit.getEliteIndexes();
        for(int i=0; i<numElite; i++){
            //Get the elite
            Chromosome ch = store.getHistoricalSet()[elite[i]];
            //Copy the elite
            for(int k=0; k<eliteCopies; k++){
                Chromosome chnew = store.getChromosome();
                ch.copyTo(chnew);
            }
        }
    }

    /**
     * Mutates the chromosome values and places them in the child chromosome. Uses mutationRate
     * and maxPerturbation. This function will override the child array with new
     * values.
     *
     * @param chrom
     * @param child
     */
    public void mutate(double[] chrom, double[] child) {
        for (int i = 0; i < child.length; i++) {
            if (Math.random() < mutationRate) {
                //Mutate the data
                child[i] += (Math.random() - Math.random()) * maxPerturbation;
            }
        }
    }

    /**
     * Performs genetic crossover between chrom1 and chrom2 and places the
     * results in child1 and child2. Uses crossoverRate to randomly decide where
     * or not to perform the crossover. If crossover is not done, the parents
     * are copied and returned. The child1 and child2 arrays are overridden.
     *
     * @param chrom1 Parent 1
     * @param chrom2 Parent 2
     * @param child1 Resulting child 1
     * @param child2 Resulting child 2
     */
    public void singlePointCrossover(double[] chrom1, double[] chrom2, double[] child1, double[] child2) {
        if (Math.random() < crossoverRate) {
            //Choose the crossover point
            int point = (int) (Math.random() * chrom1.length);
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
        } else {
            //Simply copy the data over
            for (int i = 0; i < chrom1.length; i++) {
                child1[i] = chrom1[i];
                child2[i] = chrom2[i];
            }
        }
    }

    /**
     * Get a chromosome using the roulette selection method.
     *
     * @param fit
     * @return The index of the chosen chromosome.
     */
    public int getRouletteChromosome(FitnessBlock fit) {
        //"Spin the wheel" - randomly choose a stoping point
        double stop = Math.random() * fit.getFitnessSum();
        //Find the index
        return rouletteBinarySearch(stop, fit.getSummationArray());
    }

    /**
     * A convenience method for the rouletteBinarySearch function.
     *
     * @param target The chosen roulette value.
     * @param sums The incremental list of fitness sums.
     * @return The index of the largest sum less than the target value.
     */
    private int rouletteBinarySearch(double target, double[] sums) {
        //Start in the center of the array
        int centerIndex = (int) (sums[sums.length - 1] / 2);
        return rouletteBinarySearch(target, sums, centerIndex);
    }

    /**
     * A modified, recursive, binary search algorithm that looks for the largest
     * sum that is less than the target value.
     *
     * @param target The chosen roulette value.
     * @param sums The incremental list of fitness sums.
     * @param pindex The current index.
     * @return The index of the largest sum less than the target value.
     */
    private int rouletteBinarySearch(double target, double[] sums, int index) {
        //Base Case Logic: if (index == sums.length-1 || (sums[index] < target && sums[index + 1] > target))
        if (sums[index] < target) {
            //Base case
            if (index == sums.length - 1 || sums[index + 1] > target) {
                return index;
            }
            //Go forward
            return rouletteBinarySearch(target, sums, (int) ((index + sums.length) / 2));
        } else {
            //Go backward
            return rouletteBinarySearch(target, sums, (int) (index / 2));
        }
    }

    
}
