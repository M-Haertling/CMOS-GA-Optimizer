package genetic_algorithms.main;

import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.operations.ChromosomeQueue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

//@author Michael Haertling
/**
 *
 * @author Michael
 */
public class RegressiveConvectionStorage extends ConvectionStorage implements ChromosomeQueue {

    private static final int SUSPEND_TIME = 5;
    LinkedList<Chromosome> queue;

    //Semaphores
    Semaphore getChromsSem = new Semaphore(1);
    Semaphore finalizeChromsSem = new Semaphore(1);

    public RegressiveConvectionStorage(int populationSize, int numElite, ChromosomeFactory factory, FittingRoom fr) {
        super(populationSize, numElite, factory);
        queue = new LinkedList<>();
        for(Chromosome c:chroms[newLevel]){
            fr.fitChromosome(c);
        }
        convect();
    }

    @Override
    public void convect() {
        super.convect();
        //Push all of working set onto the queue
        for (Chromosome c : chroms[newLevel]) {
            queue.push(c);
        }
    }

    @Override
    public boolean getChromosomes(Chromosome[] buffer, int num) {
        if (num > numRemaining) {
            return false;
        }
        try {
            getChromsSem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(RegressiveConvectionStorage.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (num > numRemaining) {
            getChromsSem.release();
            return false;
        }

        for (int i = 0; i < num; i++) {
            //Block until a chromosome is available
            while (queue.isEmpty() && num <= numRemaining) {
                try {
                    Thread.sleep(SUSPEND_TIME);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RegressiveConvectionStorage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if(!queue.isEmpty() && num <= numRemaining){
                buffer[i] = queue.pop();
                buffer[i].unfit();
            }else{
                //Turns out this operation does have too many outputs
                //Return the chromosomes we have gotten and skip this opperation
                for (int k = 0; k < i; k++) {
                    push(buffer[k]);
                }
                getChromsSem.release();
                return false;
            }
        }
        getChromsSem.release();
        return true;
    }

    @Override
    public void push(Chromosome c) {
        queue.push(c);
    }

    @Override
    public void finalizeChromosomes(int num) {
        try {
            finalizeChromsSem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(RegressiveConvectionStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
        numRemaining -= num;
        finalizeChromsSem.release();
    }

}
