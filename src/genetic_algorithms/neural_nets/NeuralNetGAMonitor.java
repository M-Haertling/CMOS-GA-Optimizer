package genetic_algorithms.neural_nets;

import genetic_algorithms.main.Chromosome;
import genetic_algorithms.main.GAMonitor;
import genetic_algorithms.main.RegressiveConvectionStorage;
import genetic_algorithms.operations.SAController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;



//@author Michael Haertling

public class NeuralNetGAMonitor extends GAMonitor{

    RegressiveConvectionStorage store;
    SAController sac;
    long numSaved = 0;
    String path;
    long time;
    
    public NeuralNetGAMonitor(String path,RegressiveConvectionStorage store, SAController sac){
        this.store = store;
        this.sac = sac;
        this.path = path;
        new File(path).mkdirs();
        time = System.currentTimeMillis();
    }
    
    @Override
    public void generationComplete() {
        numSaved++;
        Chromosome best = store.getHistoricalSet()[store.getMostElite()];
        String filename = path+"/"+numSaved+"-"+sac.getCurrentTemperature()+"-"+best.getFitness();
        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(Arrays.toString(best.getValues()));
            out.print(System.currentTimeMillis()-time);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NeuralNetGAMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        time = System.currentTimeMillis();
    }
    
    
    
}
