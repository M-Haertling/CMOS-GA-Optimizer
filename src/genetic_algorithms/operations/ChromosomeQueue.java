/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetic_algorithms.operations;

import genetic_algorithms.main.Chromosome;

/**
 *
 * @author Michael
 */
public interface ChromosomeQueue {
    
    public void push(Chromosome c);
    public void finalizeChromosomes(int num);
    
}
