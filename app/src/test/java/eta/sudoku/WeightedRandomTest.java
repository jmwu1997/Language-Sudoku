package eta.sudoku;
import org.junit.Test;

import java.util.Random;

import eta.sudoku.model.Vocab;
import eta.sudoku.model.VocabLibrary;
import eta.sudoku.model.WeightedRandom;
import static org.junit.Assert.*;
public class WeightedRandomTest {
    String[][] mVocabLib = {
            {"Apple", "苹果"},
            {"Pear", "梨"},
            {"Banana", "香蕉"},
            {"Peach", "桃子"},
            {"Grape", "葡萄"},
            {"Haw", "山楂"},
            {"Guava", "番石榴"},
            {"Papaya", "木瓜"},
            {"Lemon", "柠檬"},
            {"Orange", "橙子"},
            {"Mango", "芒果"},
            {"Fig", "无花果"},
            {"Coconut", "椰子"},
            {"Berry", "浆果"},
            {"Almond", "杏仁"},
            {"Tomato", "番茄"},
            {"Date", "枣子"},
            {"Durian", "榴莲"},
            {"Longan", "龙眼"},
            {"Melon", "香瓜"}
    };
    final int[] clips= { R.raw.apple, R.raw.pear, R.raw.banana, R.raw.peach, R.raw.grape, R.raw.haw, R.raw.guava,
            R.raw.papaya,R.raw.lemon, R.raw.orange, R.raw.mango, R.raw.fig, R.raw.coconut, R.raw.berry, R.raw.almond,
            R.raw.tomato, R.raw.date, R.raw.durian, R.raw.longan, R.raw.melon };
    VocabLibrary fullLib = new VocabLibrary();
    WeightedRandom<Integer> r;
    int randomWeightBound = 20;
    int randomSize;
    int testSize = 20;
    Random random = new Random();
    public WeightedRandomTest() {
        for(int i=0;i<mVocabLib.length;i++){
            Vocab v = new Vocab(mVocabLib[i]);
            v.setIndex(i+1);
            fullLib.add(v);
            fullLib.get(i+1).setSoundFile(clips[i]);
        }

        r = new WeightedRandom<>();

        randomSize = random.nextInt(testSize-1)+1;
    }

    @Test
    public void WeightedRandomAddCheck(){
        double totalKey = 0;
        double tempKey;
        int randomWeight;
        Integer randomInt;
        for(int i=0; i<randomSize; i++){
            randomWeight = random.nextInt(randomWeightBound);
            randomInt = new Integer(random.nextInt());
            totalKey += randomWeight;
            r.add(randomWeight, randomInt);
            assertEquals(randomInt, r.getMap().get(totalKey));
        }

    }
    @Test
    public void WeightedRandomRemoveCheck(){

    }
}
