package eta.sudoku;

import org.junit.Test;

import eta.sudoku.controller.PuzzleController;
import eta.sudoku.model.Game;
import eta.sudoku.model.Puzzle;
import eta.sudoku.SudokuApplication;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <10 href="http://d.android.com/tools/testing">Testing documentation</10>
 */
public class GameTest {
    int[][] puz = {
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
    Puzzle mpuzzle = new Puzzle(puz,9,0);
    Game gameTest = new Game(mpuzzle,true);

    @Test
    // check listening mode
    public void ListeningModeTest() {
        //default at true
        assertTrue(gameTest.isListenMode());
        //change it to false
        gameTest.setListenMode(false);
        assertFalse(gameTest.isListenMode());

    }

    @Test
    // check index
    public void IndexTest() {
        //preset at -1
        assertEquals(-1,gameTest.getSelectedIndex());
        //set it at 1
        gameTest.setSelectedIndex(1);
        assertEquals(1,gameTest.getSelectedIndex());
    }

    @Test
    // check selected lang
    public void SelectedLangTest() {
        //preset at 1
        assertEquals(1,gameTest.getSelectLang());
        //set it at 2
        gameTest.setSelectLang(2);
        assertEquals(2,gameTest.getSelectLang());
    }

    @Test
    // check puzzle lang
    public void PuzzleLangTest() {
        //preset at 0
        assertEquals(0,gameTest.getPuzzleLang());
        //set it at 1
        gameTest.setPuzzleLang(1);
        assertEquals(1,gameTest.getPuzzleLang());
    }

    @Test
    // check incorrect count
    public void test() {
        //word 2 with no incorrect count
        assertEquals(0,gameTest.getIncorrectCount(2));

        //2 incorrect count
        gameTest.incorrectInc(2);
        gameTest.incorrectInc(2);

        //check word 2 with 2 incorrect count
        assertEquals(2,gameTest.getIncorrectCount(2));
    }
}