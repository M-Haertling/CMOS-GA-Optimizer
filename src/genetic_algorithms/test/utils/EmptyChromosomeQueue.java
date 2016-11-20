package genetic_algorithms.test.utils;

import genetic_algorithms.main.Chromosome;
import genetic_algorithms.operations.ChromosomeQueue;



//@author Michael Haertling

public class EmptyChromosomeQueue implements ChromosomeQueue{

    @Override
    public void push(Chromosome c) {
    }

    @Override
    public void finalizeChromosomes(int num) {
    }
    
    
    
}
