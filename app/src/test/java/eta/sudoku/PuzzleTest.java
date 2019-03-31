package eta.sudoku;

import org.junit.Test;

import eta.sudoku.model.Puzzle;
import eta.sudoku.model.VocabLibrary;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <10 href="http://d.android.com/tools/testing">Testing documentation</10>
 */
public class PuzzleTest {

    @Test
    //create a 9 x 9 medium mode and check
    public void testVocabLibrary9x9() {
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

        int size9x9 = 9;
        int medium = 1;
        Puzzle puzzleTest = new Puzzle(mPuzzle, size9x9, medium);
        for (int i = 0; i < 9; i++) {
            assertEquals(9, puzzleTest.getPrefilledPuzzle()[i].length);
            assertEquals(9, puzzleTest.getCurrentPuzzle()[i].length);
        }

        //compare function on a filled puzzle
        assertArrayEquals(puzzleTest.getCurrentPuzzle(),puzzleTest.getPrefilledPuzzle());

        //check puzzle value are correct
        assertArrayEquals(mPuzzle,  puzzleTest.getPrefilledPuzzle());
        assertArrayEquals(mPuzzle,  puzzleTest.getCurrentPuzzle());
        assertEquals(9,puzzleTest.getSize());
        assertEquals(1,puzzleTest.getDifficulty());
        assertFalse(puzzleTest.isCellEmpty(1,1));

        //prefilled = current
        assertEquals(9,puzzleTest.getCurrentCell(8,8));
        assertEquals(9,puzzleTest.getPrefilledCell(8,8));

        //change the last cell to 4
        puzzleTest.setCurrentCell(4,8,8);
        assertEquals(4,puzzleTest.getCurrentCell(8,8));

        // prefilled != current after change
        assertEquals(4,puzzleTest.getCurrentCell(8,8));
        assertEquals(9,puzzleTest.getPrefilledCell(8,8));


    }
    @Test
    //create a 12 x 12 hard mode and check
    public void testVocabLibrary12x12() {
        // Test constructor
        int[][] mPuzzle = {
                {9,2,10,1,4,7,11,5,3,8,12,6},
                {3,5,11,6,8,10,12,9,1,4,2,7},
                {8,12,4,7,3,1,6,2,11,5,10,9},
                {11,8,6,12,5,4,2,7,10,9,1,3},
                {2,3,9,4,1,12,10,6,8,7,11,5},
                {7,1,5,10,11,9,8,3,2,12,6,4},
                {12,4,8,9,10,6,1,11,7,3,5,2},
                {6,10,7,3,2,8,5,12,9,11,4,1},
                {1,11,2,5,9,3,7,4,12,6,8,10},
                {5,6,1,11,7,2,9,8,4,10,3,12},
                {2,7,3,8,12,5,4,1,6,2,9,11},
                {4,9,12,2,6,11,3,10,5,1,7,8},


        };


        // assumes SudokuApplication.java and VocabLibrary.java is working

        int hard = 2;
        int size12x12 = 12;
        Puzzle puzzleTest = new Puzzle(mPuzzle, size12x12, hard);

        for (int i = 0; i < 12; i++) {
            assertEquals(12, puzzleTest.getPrefilledPuzzle()[i].length);
            assertEquals(12, puzzleTest.getCurrentPuzzle()[i].length);
        }

        //compare function on a filled puzzle
        assertArrayEquals(puzzleTest.getCurrentPuzzle(),puzzleTest.getPrefilledPuzzle());

        //check puzzle value are correct
        assertArrayEquals(mPuzzle,  puzzleTest.getPrefilledPuzzle());
        assertArrayEquals(mPuzzle,  puzzleTest.getCurrentPuzzle());
        assertEquals(12,puzzleTest.getSize());
        assertEquals(2,puzzleTest.getDifficulty());
        assertFalse(puzzleTest.isCellEmpty(1,1));

        //prefilled = current
        assertEquals(8,puzzleTest.getCurrentCell(11,11));
        assertEquals(8,puzzleTest.getPrefilledCell(11,11));

        //change the last cell to 4
        puzzleTest.setCurrentCell(4,11,11);
        assertEquals(4,puzzleTest.getCurrentCell(11,11));

        // prefilled != current after change
        assertEquals(4,puzzleTest.getCurrentCell(11,11));
        assertEquals(8,puzzleTest.getPrefilledCell(11,11));
    }

    @Test
    //create puzzle 4x4 easy mode and check
    public void testVocabLibrary4x4() {
        // Test constructor
        int[][] mPuzzle = {
                {1,4,3,2},
                {3,2,4,1},
                {4,1,2,3},
                {2,3,1,4},

        };


        // assumes SudokuApplication.java and VocabLibrary.java is working
        int easy = 0;
        int size4x4 = 4;
        Puzzle puzzleTest = new Puzzle(mPuzzle, size4x4, easy);

        for (int i = 0; i < 4; i++) {
            assertEquals(4, puzzleTest.getPrefilledPuzzle()[i].length);
            assertEquals(4, puzzleTest.getCurrentPuzzle()[i].length);
        }

        //compare function on a filled puzzle
        assertArrayEquals(puzzleTest.getCurrentPuzzle(),puzzleTest.getPrefilledPuzzle());


        //check puzzle value are correct
        assertArrayEquals(mPuzzle,  puzzleTest.getPrefilledPuzzle());
        assertArrayEquals(mPuzzle,  puzzleTest.getCurrentPuzzle());
        assertEquals(4,puzzleTest.getSize());
        assertEquals(0,puzzleTest.getDifficulty());
        assertFalse(puzzleTest.isCellEmpty(1,1));

        //prefilled = current
        assertEquals(4,puzzleTest.getCurrentCell(3,3));
        assertEquals(4,puzzleTest.getPrefilledCell(3,3));

        //change the last cell to 1
        puzzleTest.setCurrentCell(1,3,3);
        assertEquals(1,puzzleTest.getCurrentCell(3,3));

        // prefilled != current after change
        assertEquals(1,puzzleTest.getCurrentCell(3,3));
        assertEquals(4,puzzleTest.getPrefilledCell(3,3));



    }
}
