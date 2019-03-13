package eta.sudoku;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PuzzleTest {
    @Test
    public void testVocabLibrary() {
        // Test constructor
        int[][] mPuzzle = {
                {6, 8, 2, 9, 4, 7, 5, 1, 3},
                {3, 1, 4, 6, 2, 5, 7, 9, 8},
                {9, 7, 5, 8, 3, 1, 4, 6, 2},
                {2, 5, 7, 3, 8, 6, 9, 4, 1},
                {1, 4, 6, 7, 9, 2, 3, 8, 5},
                {8, 9, 3, 1, 5, 4, 6, 2, 7},
                {7, 6, 9, 2, 1, 3, 8, 5, 4},
                {4, 2, 8, 5, 7, 9, 1, 3, 6},
                {5, 3, 1, 4, 6, 8, 2, 7, 9}
        };

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

        // assumes SudokuApplication.java and VocabLibrary.java is working
        VocabLibrary vocab = SudokuApplication.getInstance().getVocabList().getRandomVocabs(9);
        Puzzle puzzleTest = new Puzzle(mPuzzle, vocab);

        assertEquals(9, puzzleTest.getPrefilledPuzzle()[0].length);
        assertEquals(9, puzzleTest.getPrefilledPuzzle()[1].length);
        assertArrayEquals(mPuzzle,  puzzleTest.getPrefilledPuzzle());
        assertEquals(vocab, puzzleTest.getFullVocab());
    }
}
