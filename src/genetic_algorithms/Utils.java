package genetic_algorithms;



//@author Michael Haertling

public class Utils {
    
    /**
     * A convenience method for the rouletteBinarySearch function.
     *
     * @param target The chosen roulette value.
     * @param sums The incremental list of fitness sums.
     * @return The index of the largest sum less than the target value.
     */
    public static int rouletteBinarySearch(double target, double[] sums) {
        //Start in the center of the array
        return rouletteBinarySearch(target, sums, 0, sums.length - 1);
    }

    /**
     * A modified, recursive, binary search algorithm that looks for the largest
     * sum that is less than the target value.
     *
     * @param target The chosen roulette value.
     * @param sums The incremental list of fitness sums.
     * @param indexL
     * @param indexH
     * @return The index of the largest sum less than the target value.
     */
    public static int rouletteBinarySearch(double target, double[] sums, int indexL, int indexH) {
        //Base Case Logic: if sums[index] < target 
        //  -> if at end of array return index 
        //  -> if next sum is greater than target return index
        //Base Case Logic: if sums[index] < target
        //  -> if index == 0 return index
        int center = (indexL + indexH) / 2;
        if (sums[center] < target) {
            if (sums[center + 1] > target) {
                return center + 1;
            }
            //Go forward
            return rouletteBinarySearch(target, sums, center, indexH);
        } else {
            if (center == 0) {
                return center;
            }
            //Go backward
            return rouletteBinarySearch(target, sums, indexL, center);
        }
    }
    
    /**
     * Normalizes an array of sums. The maximum value will be set to 1 and all
     * other values will be linearly shifted in accordance to this change. This
     * allows Math.random to be directly used when using roulette binary search.
     * NOTE: The array of sums must be ordered.
     *
     * @param sums
     */
    public static void normalizeSumArray(double[] sums) {
        double max = sums[sums.length - 1];
        sums[sums.length - 1] = 1;
        for (int i = 0; i < sums.length - 1; i++) {
            sums[i] /= max;
        }
    }
}
