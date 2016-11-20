package genetic_algorithms.main;

/**
 * The primary storage unit for chromosome values.
 *
 * @author Michael
 */
public class Chromosome {

    private boolean fitted = false;

    private final double[] values;
    private double fitness;

    public Chromosome(double[] v) {
        values = v;
    }

    /**
     *
     * @return The values stored in this chromosome.
     */
    public double[] getValues() {
        return values;
    }

    /**
     * Copies all values, excluding fitness, from this chromosome to the
     * specified chromosome.
     *
     * @param c
     */
    public void copyDataTo(Chromosome c) {
        System.arraycopy(values, 0, c.values, 0, c.values.length);
    }

    /**
     * Copies all values, including fitness, from this chromosome to the
     * specified chromosome.
     *
     * @param c
     */
    public void copyAllTo(Chromosome c) {
        System.arraycopy(values, 0, c.values, 0, c.values.length);
        if(fitted){
        c.setFitness(fitness);
        }
    }

    public void setFitness(double f) {
        fitness = f;
        fitted = true;
    }

    public double getFitness() {
        return fitness;
    }

    public boolean hasBeenFitted() {
        return fitted;
    }

    public void unfit() {
        fitted = false;
    }
}
