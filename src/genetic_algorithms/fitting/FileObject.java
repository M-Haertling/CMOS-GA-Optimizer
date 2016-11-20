package genetic_algorithms.fitting;



/**
 * 
 * @author Michael
 * @param <T> 
 */
public abstract class FileObject <T>{
    
    private final int index;
    
    public FileObject(int index){
        this.index = index;
    }
    
    public abstract double[] encode();
    public abstract T decode();
}
