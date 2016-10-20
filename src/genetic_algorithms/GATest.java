package genetic_algorithms;

//@author Michael Haertling
import java.util.Arrays;

public class GATest {

    public static void main(String[] args) {
        //mutationRate,maxPerterbation,crossoverRate,numElite,eliteCopies
        double mutationRate = 0.1;
        double maxPerterbation = 0.3;
        double crossoverRate = 1;
        int numElite = 4;
        int eliteCopies = 1;
        int populationSize = 20;
        final int chromosomeSize = 5;

        BreedingChamber bc = new BreedingChamber(mutationRate, maxPerterbation, crossoverRate, numElite, eliteCopies);

        FitnessBlock fb = new FitnessBlock(populationSize, numElite);

        ConvectionStorage store = new ConvectionStorage(populationSize, new ChromosomeFactory() {
            @Override
            public Chromosome generateChromosome() {
                double[] c = new double[chromosomeSize];
                for (int i = 0; i < c.length; i++) {
                    c[i] = (int) (Math.random() * 20);
                }
                return new Chromosome(c);
            }
        });

        //Add random fitness values
        for (int i = 0; i < populationSize; i++) {
            fb.addFitness((int) (Math.random() * 5), i);
        }
        fb.pack();

        //!!Display the fitness block
        System.out.println("Fitness Block:\n" + Arrays.toString(fb.getFitnessArray()) + "\n" + Arrays.toString(fb.getSummationArray()) + "\n");

        //!!Test the roulette selection
        int[] chosen = new int[populationSize];
        double[] chosenp = new double[populationSize];
        double[] fitp = new double[populationSize];
        double[] difference = new double[populationSize];

        double dtotal = 0;

        int testSize = 1000;
        //Do test
        for (int i = 0; i < testSize; i++) {
            int index = bc.getRouletteChromosome(fb);
            chosen[index]++;
        }
        //Find percentages
        for (int i = 0; i < chosenp.length; i++) {
            chosenp[i] = (double) chosen[i] / testSize;
            fitp[i] = (double) fb.getFitnessArray()[i] / fb.getFitnessSum();
            difference[i] = Math.abs(fitp[i] - chosenp[i]);
            dtotal += difference[i];
        }
        System.out.println("Roulette Selection Test:\n"
                + "Fitness Array:\t" + Arrays.toString(fb.getFitnessArray())
                + "\nFitness %:\t" + Arrays.toString(fitp)
                + "\nSelection %:\t" + Arrays.toString(chosenp)
                + "\nCounts:\t\t" + Arrays.toString(chosen)
                + "\nDiffernece:\t" + Arrays.toString(difference)
                + "\nDiff. Total:\t" + dtotal + "\n");

        //!!Test the crossover
        System.out.println("Crossover Test:");
        Chromosome p1 = bc.getRouletteChromosome(fb, store);
        Chromosome p2 = bc.getRouletteChromosome(fb, store);
        Chromosome c1 = store.getChromosome();
        Chromosome c2 = store.getChromosome();
        bc.singlePointCrossover(p1.getValues(), p2.getValues(), c1.getValues(), c2.getValues());
        System.out.println("Parent 1:\t" + Arrays.toString(p1.getValues()));
        System.out.println("Parent 2:\t" + Arrays.toString(p2.getValues()));
        System.out.println("Child 1:\t" + Arrays.toString(c1.getValues()));
        System.out.println("Child 2:\t" + Arrays.toString(c2.getValues()));
        int[] cr1 = new int[chromosomeSize];
        int[] cr2 = new int[chromosomeSize];
        for (int i = 0; i < chromosomeSize; i++) {
            if (p1.getValues()[i] == p2.getValues()[i]) {
                cr1[i] = 0;
                cr2[i] = 0;
            } else {
                if (c1.getValues()[i] == p1.getValues()[i]) {
                    cr1[i] = 1;
                } else {
                    cr1[i] = 2;
                }
                if (c2.getValues()[i] == p1.getValues()[i]) {
                    cr2[i] = 1;
                } else {
                    cr2[i] = 2;
                }
            }
        }
        System.out.println("CRep 1:\t\t" + Arrays.toString(cr1));
        System.out.println("CRep 2:\t\t" + Arrays.toString(cr2)+"\n");

        //!!Test the mutation
        System.out.println("Mutation Test:");
        Chromosome p = bc.getRouletteChromosome(fb, store);
        Chromosome c = store.getChromosome();
        bc.mutate(p.getValues(), c.getValues());
        System.out.println("Parent:\t"+Arrays.toString(p.getValues()));
        System.out.println("Child:\t"+Arrays.toString(c.getValues()));
    }

}
