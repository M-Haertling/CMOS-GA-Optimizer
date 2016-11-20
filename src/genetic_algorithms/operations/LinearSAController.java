package genetic_algorithms.operations;


//@author Michael Haertling

public class LinearSAController extends SAController{

    double constant;
    
    public LinearSAController(double con, double start){
        constant = con;
        this.temperature = start;
    }
    
    @Override
    public void advanceSchedule() {
        this.setTemperature(Math.max(temperature-constant,0));
        System.out.println("Temp: "+temperature);
    }

    @Override
    public boolean scheduleComplete() {
        return (temperature<=0);
    }
    
    
    
}
