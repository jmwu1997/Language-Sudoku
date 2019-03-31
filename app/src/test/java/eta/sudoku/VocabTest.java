package eta.sudoku;

import org.junit.Test;

import java.util.Arrays;

import eta.sudoku.model.Vocab;

import static org.junit.Assert.*;

public class VocabTest {
    @Test
    public void VocabDifficultSetCheck(){
        Vocab testVocab = new Vocab();

        //set difficult check
        testVocab.setDifficult(true);
        assertTrue(testVocab.isDifficult());

        //set not difficult check
        testVocab.setDifficult(false);
        assertFalse(testVocab.isDifficult());

    }
    @Test
    public void VocabSoundSetCheck(){

        //is sound missing check
        Vocab testVocab = new Vocab();
        assertTrue(testVocab.isSoundMissing());

        //set sound file check
        testVocab.setSoundFile(R.raw.apple);
        int clip= testVocab.getSoundFile();
        assertEquals(R.raw.apple,clip);

        //is sound missing check
        assertFalse(testVocab.isSoundMissing());

    }
    @Test
    public void VocabIndexSetCheck() {
        Vocab testVocab = new Vocab();

        //set index test
        testVocab.setIndex(2);
        assertEquals(2, testVocab.getIndex());
        testVocab.setIndex(3);
        assertEquals(3, testVocab.getIndex());
        //set mIndex test
        testVocab.setmIndex(2);
        assertEquals(2, testVocab.getmIndex());
        testVocab.setmIndex(3);
        assertEquals(3, testVocab.getmIndex());



    }
    @Test
    public void VocabWordSetCheck() {
        Vocab testVocab = new Vocab();
        //set words test
        testVocab.setWord(0,"test0");
        assertEquals("test0",testVocab.getWord(0));
        testVocab.setWord(1,"test1");
        assertEquals("test1",testVocab.getWord(1));

        //set word array test
        testVocab.setWords(new String[]{"test1,test2,test3"});
        assertArrayEquals(new String[]{"test1,test2,test3"}, testVocab.getWords());


    }
}
