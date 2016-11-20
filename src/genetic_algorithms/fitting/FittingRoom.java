package genetic_algorithms.fitting;

//@author Michael Haertling

import genetic_algorithms.main.Chromosome;

public abstract class FittingRoom {

    private long fittings = 0;
    
    /**
     * Calculates and returns the fitness for the specified chromosome.
     * This function must be thread safe.
     *
     * @param c
     * @return
     */
    protected abstract double calculateChromosomeFitness(Chromosome c);

    /**
     * Calculates, sets, and returns the fitness for the specified chromosome.
     * This function must be thread safe.
     * @param ch 
     */
    public void fitChromosome(Chromosome ch) {
        fittings++;
        ch.setFitness(this.calculateChromosomeFitness(ch));
    }

    public long getNumFittings(){
        return fittings;
    }
    
}
