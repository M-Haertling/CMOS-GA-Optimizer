package genetic_algorithms.main;

import genetic_algorithms.convergence.ConvergenceMonitor;
import genetic_algorithms.Utils;
import genetic_algorithms.fitting.FittingRoom;
import genetic_algorithms.operations.GeneticOperation;
import genetic_algorithms.operations.OperationGroup;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The house for all genetic algorithm functions. This algorithm is designed to
 * handle order specific, fixed length chromosomes.
 *
 * @author Michael
 */
public class BreedingChamber {

    BreedingThread[] threads;
    OperationGroup operations;
    ConvergenceMonitor cm;
    RegressiveConvectionStorage store;
    FittingRoom fittingRoom;
    GAMonitor gamonitor;
    int generations;

    public BreedingChamber(RegressiveConvectionStorage store, FittingRoom fr, OperationGroup opg, ConvergenceMonitor cm, GAMonitor gamonitor, int numThreads) {
        operations = opg;
        this.store = store;
        fittingRoom = fr;
        generations = 0;
        this.cm = cm;
        this.gamonitor = gamonitor;
        //Instantiate the threads
        threads = new BreedingThread[numThreads];
        boolean master = true;
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new BreedingThread(this, fr, store, opg.getMaxNumberOfInputs(), opg.getMaxNumberOfOutputs(), master);
            master = false;
        }
    }

    public void suspendUntilCompletion() {
        while (!allThreadsSuspended()) {
            try {
                Thread.sleep(BreedingThread.SUSPEND_TIME);
            } catch (InterruptedException ex) {
                Logger.getLogger(BreedingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean allThreadsSuspended() {
        boolean done = true;
        for (int i = 1; i < threads.length; i++) {
            if (!threads[i].isSuspended()) {
                done = false;
                break;
            }
        }
        return done;
    }

    public void releaseThreads() {
        for (BreedingThread thread : threads) {
            thread.resumeGeneration();
        }
    }

    public void beginGeneticSearch() {
        for (int i = 1; i < threads.length; i++) {
            threads[i].start();
        }
        threads[0].run();
    }

    public void endGeneticSearch() {
        for (BreedingThread thread : threads) {
            thread.end();
        }
        releaseThreads();
    }

    public boolean hasConverged() {
        return cm.converged();
    }

    public void prepareForNextGeneration() {
//        System.out.println("Preparing Next Generation: " + (generations + 1));

        //Convect the data
        store.convect();

        if (gamonitor != null) {
            gamonitor.generationComplete();
        }

//        for (Chromosome c : store.getHistoricalSet()) {
//            System.out.print(" " + c.getFitness());
//        }
        ListIterator<Integer> li = store.getEliteIndexes().listIterator();
        while (li.hasNext()) {
            int index = li.next();
            System.out.println("The Elite (" + index + "): " + store.getHistoricalSet()[index].getFitness());
        }

        System.out.println("Num Fittings: " + fittingRoom.getNumFittings());

        //Check for convergence
        if (hasConverged()) {
            if (operations.getSAController().scheduleComplete()) {
                //End the GA algorithm
                endGeneticSearch();
                return;
            } else {
                //Advance the SA schedule
                operations.getSAController().advanceSchedule();
            }
        }

        //Continue to next generation
        releaseThreads();
    }

    /**
     * Begin evolving the array of chromosomes given their fitness values.
     *
     * @param thread
     */
    public void performEvolution(BreedingThread thread) {
        //Loop till population is full
        generations++;
//        int numop = 0;
        while (store.getNumRemaining() > 0) {
//            numop++;
            performOperation(thread);
        }
//        System.out.println("Number of Operations Done: "+numop);
//        for(Chromosome c:store.getWorkingSet()){
//            System.out.println(c.getFitness());
//        }
    }

    public void performOperation(BreedingThread thread) {
        //Select an operation
        GeneticOperation op = operations.selectOperation();

        //Check if there are enough remaining chromosomes
        if (op.numOutputs() > store.getNumRemaining()) {
            //Too many outputs, skip this operation
            return;
        }

        //Select output(child) chromosomes
        Chromosome[] out = thread.getOutputArray();
        if (!store.getChromosomes(out, op.numOutputs())) {
            return;
        }
        //Select input(parent) chromosomes
        Chromosome[] in = thread.getInputArray();
        for (int i = 0; i < in.length; i++) {
            in[i] = this.getRouletteChromosome(store);
        }
        //Perform the operation
        op.operate(store, fittingRoom, in, out, true, true);
    }

    /**
     * A convenience method for returning a chromosome from a
     * ConvectionStroage's historical set.
     *
     * @param store
     * @return A chromosome from the ConvectionStroage's historical set.
     */
    public Chromosome getRouletteChromosome(ConvectionStorage store) {
        return store.getHistoricalSet()[this.getRouletteChromosomeIndex(store)];
    }

    /**
     * Get a chromosome using the roulette selection method.
     *
     * @param fit
     * @return The index of the chosen chromosome.
     */
    public int getRouletteChromosomeIndex(ConvectionStorage fit) {
        //"Spin the wheel" - randomly choose a stoping point
        double stop = Math.random() * fit.getFitnessSum();
        //Find the index
        return Utils.rouletteBinarySearch(stop, fit.getSummationArray());
    }

    public BreedingThread[] getThreads() {
        return threads;
    }

}
