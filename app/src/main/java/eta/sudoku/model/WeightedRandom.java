//TODO: WIP!

package eta.sudoku.model;

import android.util.Log;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class WeightedRandom<E> {
    private static final String TAG = "WeightedRandom";
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    public WeightedRandom() {
        this(new Random());
    }

    public WeightedRandom(Random random) {
        this.random = random;
    }

    public void add(double weight, E result) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, result);
        return;
    }
    public void remove(E result){
        double tempKey=0;
        while(map.higherKey(tempKey) != null){
            tempKey = map.higherKey(tempKey);
            if(map.get(tempKey) == result){
                map.remove(tempKey);

            }
        }

    }

    public NavigableMap<Double, E> getMap(){
        return map;
    }
    public E next() {
        double value = random.nextDouble() * total;
        //Log.d(TAG, "random.nextDouble() * total: " + Double.toString(value));
        // possible fix for when value is somehow higher than largest key
        value = Math.floor(value);
        return map.higherEntry(value).getValue();
    }
}
