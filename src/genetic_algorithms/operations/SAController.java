package genetic_algorithms.operations;

import java.util.ArrayList;



//@author Michael Haertling

public abstract class SAController {
    
    protected final ArrayList<SAGeneticOperation> ops;
    protected double temperature;
    
    public SAController(){
        ops = new ArrayList<>();
    }
    
    public void addOperation(SAGeneticOperation op){
        ops.add(op);
    }
    
    public void setTemperature(double temp){
        temperature = temp;
        for(SAGeneticOperation op:ops){
            op.setTemperature(temp);
        }
    }
    
    public double getCurrentTemperature(){
        return temperature;
    }
    
    public abstract void advanceSchedule();
    public abstract boolean scheduleComplete();
}
