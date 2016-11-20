package genetic_algorithms.convergence;



//@author Michael Haertling

public class IterationBasedConvergenceMonitor implements ConvergenceMonitor{

    int count = 0;
    final int numIterations;
    
    public IterationBasedConvergenceMonitor(int numIterations){
        this.numIterations = numIterations;
    }
    
    @Override
    public boolean converged() {
        count++;
        System.out.println("Iteration: "+count);
        if(count >= numIterations){
            count = 0;
            System.out.println("Converged");
            return true;
        }else{
            return false;
        }
    }
    
    
    
}
