package genetic_algorithms.neural_nets.activation_functions;



//@author Michael Haertling

public class StepActivationFunction implements ActivationFunction{

    private final double threshold;
    
    public StepActivationFunction(){
        threshold = 0;
    }
    
    public StepActivationFunction(double threshold){
        this.threshold = threshold;
    }
    
    @Override
    public double activate(double value) {
        if(value > threshold){
            return 1;
        }else{
            return 0;
        }
    }
    
    
    
}
