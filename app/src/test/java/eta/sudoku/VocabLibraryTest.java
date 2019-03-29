package eta.sudoku;

import org.junit.Test;

import eta.sudoku.model.Vocab;
import eta.sudoku.model.VocabLibrary;

import static org.junit.Assert.*;

public class VocabLibraryTest {
    @Test
    public void VocabLibConstructorEmpty() {
        VocabLibrary testLib = new VocabLibrary();
        assertEquals("",testLib.get(0).getWord(0));
        assertEquals(testLib.get(0).getWord(0),testLib.get(0).getWord(1));
    }
    @Test
    public void randomListCorrectLength() {
        int numRand = 12;
        VocabLibrary mVocabList = new VocabLibrary();
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
        for (int i = 0; i < mVocabLib.length; i++) {
            Vocab v = new Vocab(mVocabLib[i]);
            v.setIndex(i + 1);
            mVocabList.add(v);
        }
        VocabLibrary testList = mVocabList.getRandomVocabs(numRand);
        assertEquals(numRand+1,testList.size());
    }
    @Test
    public void randListUnique() {
        int numRand = 12;
        VocabLibrary mVocabList = new VocabLibrary();
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
        for (int i = 0; i < mVocabLib.length; i++) {
            Vocab v = new Vocab(mVocabLib[i]);
            v.setIndex(i + 1);
            mVocabList.add(v);
        }
        VocabLibrary testList = mVocabList.getRandomVocabs(numRand);
        VocabLibrary emptyLib = new VocabLibrary();
        emptyLib.remove(0);
        for (int i = 0; i < numRand; i++) {
            for (int j = 0; j < i; j++) {
                assertNotEquals(emptyLib.get(j), testList.get(i));
            }
            emptyLib.add(testList.get(i));


        }
    }
}



