package genetic_algorithms;

/**
 * This is a container class for the fitness values. As values are added, it
 * keeps track of a number of statistics needed by the GA. This prevents
 * unnecessary iterations over the fitness array.
 *
 * @author Michael
 */
public class FitnessBlock {

    /**
     * Holds the fitness values.
     */
    private final double[] fit;
    /**
     * Holds the indexes of the highest fitness values.
     */
    private final int[] elite;

    /**
     * The sum array of the fitness scores. The is an inherently sorted array.
     */
    private final double[] sums;

    /**
     *
     * @param size The number of fitness values expected.
     * @param numElite The number of elite fitness values to track.
     */
    public FitnessBlock(int size, int numElite) {
        fit = new double[size];
        elite = new int[numElite];
        sums = new double[size];
    }

    /**
     * Adds the fitness value to the array. NOTE: This function should only be
     * called once per index! No checks are made in order to maximize speed.
     *
     * @param index The index of the chromosome that this fitness value
     * corresponds to.
     * @param value The fitness value to add to the array.
     */
    public void addFitness(double value, int index) {
        fit[index] = value;
        //Check if this should be added to the elite array
        for (int i = 0; i < elite.length; i++) {
            if (value > fit[elite[i]]) {
                this.addToEliteArray(index, i);
                break;
            }
        }
    }

    /**
     * Adds the value to the elite array and then shifts the values to the right
     * by one index starting at the specified index.
     *
     * @param index The index at which to begin this algorithm.
     */
    private void addToEliteArray(int value, int index) {
        int tmp = value;
        int tmp2;
        for (int i = index; i < elite.length; i++) {
            tmp2 = elite[index];
            elite[index] = tmp;
            tmp = tmp2;
        }
    }

    /**
     *
     * @return The array of fitness values. Changes should NOT be made.
     */
    public double[] getFitnessArray() {
        return fit;
    }

    /**
     * Finalizes the set of fitness values. Calculations that require the entire
     * set are done here.
     */
    public void pack() {
        sums[0] = fit[0];
        for (int i = 1; i < fit.length; i++) {
            sums[i] = sums[i - 1] + fit[i];
        }
    }

    /**
     *
     * @return The sum of all the fitness values.
     */
    public double getFitnessSum() {
        return sums[sums.length - 1];
    }

    /**
     * 
     * @return The array of incremental sums of the fitness values.
     */
    public double[] getSummationArray(){
        return sums;
    }
    
    public int[] getEliteIndexes(){
        return elite;
    }
    
    /**
     * Resets the data in this object for reuse.
     */
    public void clear() {
        sums[0] = 0;
    }
}
