package genetic_algorithms.convergence;

import genetic_algorithms.main.ConvectionStorage;
import genetic_algorithms.convergence.ConvergenceMonitor;
import java.util.Arrays;
import java.util.LinkedList;



//@author Michael Haertling

public class SumBasedConvergenceMonitor implements ConvergenceMonitor{

    private final ConvectionStorage store;
    private final double acceptableDifference;
    private final LinkedList<Double> history;
    private final int historySize;
    
    public SumBasedConvergenceMonitor(ConvectionStorage s, int historySize, double acceptableDifference){
        store = s;
        this.acceptableDifference = acceptableDifference;
        history = new LinkedList<>();
        this.historySize = historySize;
    }
    
    @Override
    public boolean converged() {
        
        for(int i=0;i<store.getNumElite();i++){
            System.out.print(store.getEliteChromosome(i).getFitness()+" ");
        }
        System.out.println("\nCurrent Fitness Sum: "+store.getFitnessSum());
        
        double sum = store.getFitnessSum();
        if(history.size()<historySize){
            history.add(sum);
            return false;
        }
        //Find the highest and lowest recorded values
        double highest = sum;
        double lowest = highest;
        for(Double d:history){
            if(d>highest){
                highest = d;
            }else if(d<lowest){
                lowest = d;
            }
        }
        System.out.println("Convergence Difference: "+(highest-lowest));
        if(highest-lowest <= acceptableDifference){
            history.clear();
            System.out.println("Coverged!");
            return true;
        }else{
            history.add(sum);
            history.removeFirst();
            return false;
        }
    }
    
}
