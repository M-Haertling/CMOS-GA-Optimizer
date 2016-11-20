package genetic_algorithms.test.utils;

import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.main.Chromosome;



//@author Michael Haertling

public class RandomizedFittingRoom extends FittingRoom{

    private final int max;
    
    public RandomizedFittingRoom(int max){
        this.max = max;
    }
    
    @Override
    public double calculateChromosomeFitness(Chromosome c) {
        return Math.random()*max;
    }
    
    
    
}
