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
    public int []size = new int[]{};
    @Test

    public void ListeningModeTest() {

       Game gameTest = new Game(true,size);

    }
}