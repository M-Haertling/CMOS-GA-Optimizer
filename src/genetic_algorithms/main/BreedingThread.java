package genetic_algorithms.main;

//@author Michael Haertling
import genetic_algorithms.fitting.FittingRoom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BreedingThread extends Thread {

    public static final int SUSPEND_TIME = 5;

    boolean suspended;
    boolean run;
    boolean master;

    BreedingChamber breedingChamber;
    FittingRoom fittingRoom;
    RegressiveConvectionStorage store;

    Chromosome[] cins;
    Chromosome[] couts;

    public BreedingThread(BreedingChamber bc, FittingRoom fr, RegressiveConvectionStorage cs, int maxCIn, int maxCOut, boolean master) {
        suspended = false;
        run = true;
        this.master = master;
        breedingChamber = bc;
        fittingRoom = fr;
        store = cs;
        cins = new Chromosome[maxCIn];
        couts = new Chromosome[maxCOut];
    }

    @Override
    public void run() {
        while (run) {
            breedingChamber.performEvolution(this);
            if (master) {
                //Wait for the generation to finish
                waitForOtherThreads();
                breedingChamber.prepareForNextGeneration();
            } else {
                suspendGeneration();
            }
        }
    }

    public void suspendGeneration() {
        suspended = true;
        while (suspended) {
            try {
                Thread.sleep(SUSPEND_TIME);
            } catch (InterruptedException ex) {
                Logger.getLogger(BreedingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void waitForOtherThreads(){
        breedingChamber.suspendUntilCompletion();
    }
    
    public void resumeGeneration() {
        suspended = false;
    }

    public Chromosome[] getInputArray() {
        return cins;
    }

    public Chromosome[] getOutputArray() {
        return couts;
    }

    public void end() {
        run = false;
    }
    
    public boolean isSuspended(){
        return suspended;
    }

}
