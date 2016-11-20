package genetic_algorithms.neural_nets.activation_functions;



//@author Michael Haertling

public class SigmoidActivationFunction implements ActivationFunction{
    
    final double response;
    
    public SigmoidActivationFunction(){
        response = 1;
    }
    
    public SigmoidActivationFunction(double response){
        this.response = response;
    }

    @Override
    public double activate(double value) {
        return (1/(1+Math.exp(-value/response)));
    }
    
}
