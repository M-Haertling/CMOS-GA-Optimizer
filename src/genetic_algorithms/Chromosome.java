package genetic_algorithms;



/**
 * The primary storage unit for chromosome values.
 * @author Michael
 */
public class Chromosome {
    
    private double[] values;
    
    public Chromosome(){
        
    }
    
    /**
     * 
     * @return The values stored in this chromosome.
     */
    public double[] getValues(){
        return values;
    }
    
    /**
     * Copies all values from this chromosome to the specified chromosome.
     * @param c 
     */
    public void copyTo(Chromosome c){
        System.arraycopy(values, 0, c.values, 0, c.values.length);
    }
    
}
