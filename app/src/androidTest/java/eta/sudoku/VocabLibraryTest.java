package eta.sudoku;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import eta.sudoku.model.VocabLibrary;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class VocabLibraryTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("eta.sudoku", appContext.getPackageName());
    }

    @Test
    public void testVocabLibrary() {
        VocabLibrary test = new VocabLibrary();
        test.getRandomVocabs(5);
    }
}
