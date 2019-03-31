package eta.sudoku;

import org.junit.Test;

import eta.sudoku.model.Vocab;

import static org.junit.Assert.*;

public class VocabTest {
    Vocab testVocab = new Vocab();

    @Test
    public void VocabDifficultSetCheck(){
       //check is difficult
        testVocab.setDifficult(true);
        assertTrue(testVocab.isDifficult());
        // check is not difficult
        testVocab.setDifficult(false);
        assertFalse(testVocab.isDifficult());


    }
    @Test
    public void VocabSoundSetCheck(){
        //check sound is missing
        assertTrue(testVocab.isSoundMissing());
        //set sound and check
        testVocab.setSoundFile(R.raw.apple);
        assertEquals(R.raw.apple,testVocab.getSoundFile());
    }
    @Test
    public void VocabIndexSetCheck() {
        //check set and get index
        testVocab.setIndex(3);
        assertEquals(3, testVocab.getIndex());
    }
    @Test
    public void VocabWordSetCheck() {
        //check both set word
        testVocab.setWord(0,"test0");
        assertEquals("test0",testVocab.getWord(0));

        testVocab.setWord(1,"test1");
        assertEquals("test1",testVocab.getWord(1));

        testVocab.setWords(new String[]{"test 0,test1,test2"});
        assertArrayEquals(new String[]{"test 0,test1,test2"},testVocab.getWords());

    }

    @Test
    public void VocabmIndexSetCheck() {
        //check set and get mIndex
        testVocab.setmIndex(4);
        assertEquals(4, testVocab.getmIndex());
    }
}
