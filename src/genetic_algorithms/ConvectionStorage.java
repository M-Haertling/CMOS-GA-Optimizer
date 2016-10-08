package genetic_algorithms;

/**
 * Used to store chromosomes in such a way that allocated memory can be reused
 * throughout the entire GA process.
 *
 * @author Michael
 * @param <T>
 */
public class ConvectionStorage<T> {

    Chromosome[][] chroms;
    int newLevel = 0;
    int oldLevel = 1;
    int numRemaining;
    int index;

    public ConvectionStorage(int populationSize, ChromosomeFactory factory) {
        chroms = new Chromosome[2][];
        numRemaining = populationSize;
        index = 0;
        Chromosome[] one = new Chromosome[populationSize];
        Chromosome[] two = new Chromosome[populationSize];
        for (int i = 0; i < one.length; i++) {
            one[i] = factory.generateChromosome();
            two[i] = factory.generateChromosome();
        }
        chroms[0] = one;
        chroms[1] = two;
    }

    /**
     * Rotate the working and historical sets.
     */
    public void convect() {
        newLevel = (newLevel == 0) ? 1 : 0;
        oldLevel = (oldLevel == 0) ? 1 : 0;
        numRemaining = chroms[0].length;
        index = 0;
    }

    /**
     *
     * @return The number of chromosomes remaining in the working set.
     */
    public int getNumRemaining() {
        return numRemaining;
    }

    /**
     *
     * @return A chromosome from the working set of chromosomes.
     */
    public Chromosome getChromosome() {
        numRemaining--;
        return chroms[newLevel][index++];
    }

    /**
     *
     * @return The set of chromosomes with pertinent data in them.
     */
    public Chromosome[] getHistoricalSet() {
        return chroms[oldLevel];
    }


}
