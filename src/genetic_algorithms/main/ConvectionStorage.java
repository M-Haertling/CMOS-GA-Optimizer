package genetic_algorithms.main;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Used to store chromosomes in such a way that allocated memory can be reused
 * throughout the entire GA process.
 *
 * @author Michael
 * @param <T>
 */
public class ConvectionStorage<T> {

    Chromosome[][] chroms;

    /**
     * Holds the indexes of the highest fitness values.
     */
    private final LinkedList<Integer>[] elite;

    /**
     * The sum array of the fitness scores. The is an inherently sorted array.
     */
    private final double[][] sums;

    int newLevel = 0;
    int oldLevel = 1;
    int numRemaining;
    int index;
    int numElite;

    public ConvectionStorage(int populationSize, int numElite, ChromosomeFactory factory) {
        chroms = new Chromosome[2][];
        numRemaining = populationSize;
        index = 0;
        this.numElite = numElite;
        Chromosome[] one = new Chromosome[populationSize];
        Chromosome[] two = new Chromosome[populationSize];
        for (int i = 0; i < one.length; i++) {
            one[i] = factory.generateChromosome();
            two[i] = factory.generateChromosome();
        }
        chroms[0] = one;
        chroms[1] = two;

        if (numElite > 0) {
            elite = new LinkedList[2];
            elite[0] = new LinkedList<>();
            elite[1] = new LinkedList<>();
        } else {
            elite = null;
        }

        sums = new double[2][];
        sums[0] = new double[populationSize];
        sums[1] = new double[populationSize];
    }

    /**
     * Rotate the working and historical sets.
     */
    public void convect() {
        pack();
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
    protected synchronized Chromosome getChromosome() {
        if (numRemaining == 0) {
            return null;
        }
        numRemaining--;
        Chromosome c = chroms[newLevel][index++];
        c.unfit();
        return c;
    }

    /**
     * Collects the specified number of chromosomes into the buffer array.
     *
     * @param buffer
     * @param num
     * @return False if the number of requested chromosomes was not available.
     */
    public synchronized boolean getChromosomes(Chromosome[] buffer, int num) {
        if (num > numRemaining) {
            return false;
        }
        for (int i = 0; i < num; i++) {
            buffer[i] = getChromosome();
            if (buffer[i] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return The set of chromosomes with pertinent data in them.
     */
    public Chromosome[] getHistoricalSet() {
        return chroms[oldLevel];
    }

    public Chromosome[] getWorkingSet() {
        return chroms[newLevel];
    }

    /**
     * Finalizes the set of fitness values. Calculations that require the entire
     * set are done here.
     */
    public void pack() {
        //Fill the sums array and find the elite
        sums[newLevel][0] = chroms[newLevel][0].getFitness();
        if (elite != null) {
            elite[newLevel].clear();
        }
        for (int i = 1; i < chroms[newLevel].length; i++) {
            double currentValue = chroms[newLevel][i].getFitness();
            sums[newLevel][i] = sums[newLevel][i - 1] + currentValue;
            //Update the elite array
            if (elite != null) {
                if (elite[newLevel].isEmpty()) {
                    elite[newLevel].add(i);
                } else {
                    boolean shouldBeAdded = false;
                    ListIterator<Integer> eliteIterator = elite[newLevel].listIterator();
                    double currentEliteValue;
                    while (eliteIterator.hasNext()) {
                        currentEliteValue = chroms[newLevel][eliteIterator.next()].getFitness();
                        //WE only want to add the index if the value is at least greater
                        //than the first value
                        if (currentValue > currentEliteValue) {
                            shouldBeAdded = true;
                        } else {
                            if (shouldBeAdded) {
                                eliteIterator.previous();
                                eliteIterator.add(i);
                                if (this.getNumElite() < elite[newLevel].size()) {
                                    elite[newLevel].removeFirst();
                                }
                                shouldBeAdded = false;
                            } else if (elite[newLevel].size() < this.getNumElite()) {
                                elite[newLevel].addFirst(i);
                            }
                            break;
                        }
                    }
                    //The value is greater than all currently found elites and should
                    //be added to the head of the array
                    if (shouldBeAdded) {
                        elite[newLevel].addLast(i);
                        if (this.getNumElite() < elite[newLevel].size()) {
                            elite[newLevel].removeFirst();
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @return The array of incremental sums of the fitness values.
     */
    public double[] getSummationArray() {
        return sums[oldLevel];
    }

    /**
     *
     * @return The array of indexes pointing to the top chromosomes. The
     * chromosome with the largest fitness is at index 0 decreasing as the index
     * increases.
     */
    public LinkedList<Integer> getEliteIndexes() {
        if (elite == null) {
            return null;
        }
        return elite[oldLevel];
    }

    /**
     *
     * @return The sum of all the fitness values.
     */
    public double getFitnessSum() {
        return sums[oldLevel][sums[oldLevel].length - 1];
    }

    public Chromosome getEliteChromosome(int index) {
        if (elite == null) {
            return null;
        }
        return chroms[oldLevel][elite[oldLevel].get(index)];
    }

    public int getNumElite() {
        return numElite;
    }

    public int getMostElite() {
        if (elite == null) {
            return -1;
        }
        return elite[oldLevel].getLast();
    }

}
