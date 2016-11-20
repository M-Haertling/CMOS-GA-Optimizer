package genetic_algorithms.operations;


//@author Michael Haertling

public abstract class SAGeneticOperation extends GeneticOperation{
    
    private double temperature;
    
    public void setTemperature(double temp){
        temperature = temp;
    }
        
    /**
     * Calculates the probability that the new chromosome will be kept given the
     * difference in fitness values between the old and the new.
     * @param fitnessChange
     * @return 
     */
    protected double calculateAcceptance(double fitnessChange){
        if(fitnessChange < 0){
            //Probability of adopting an inferior solution
            return Math.exp(fitnessChange/temperature);
        }else{
            return 1;
        }
    }
    
    protected double calculateAcceptance(double oldFitness, double newFitness){
        return calculateAcceptance(newFitness-oldFitness);
    }
    
    /**
     * Returns true if the child should be accepted and false if it should not.
     * @param fitnessChange
     * @return 
     */
    protected boolean shouldAcceptChild(double fitnessChange){
        return Math.random()<= calculateAcceptance(fitnessChange);
    }

    protected boolean shouldAcceptChild(double oldFitness, double newFitness){
        return shouldAcceptChild(newFitness-oldFitness);
    }
    
}
