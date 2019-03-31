package eta.sudoku;

import org.junit.Test;

import eta.sudoku.model.Vocab;

import static org.junit.Assert.*;

public class VocabTest {
    @Test
    public void VocabDifficultSetCheck(){
        Vocab testVocab = new Vocab();
        boolean diffResult;
        testVocab.setDifficult(true);
        diffResult=testVocab.isDifficult();
        assertEquals(true,diffResult);
        testVocab.setDifficult(false);
        diffResult=testVocab.isDifficult();
        assertEquals(false,diffResult);

    }
    @Test
    public void VocabSoundSetCheck(){
        Vocab testVocab = new Vocab();
        assertEquals(true,testVocab.isSoundMissing());
        testVocab.setSoundFile(R.raw.apple);
        int clip= testVocab.getSoundFile();
        assertEquals(R.raw.apple,clip);
    }
    @Test
    public void VocabIndexSetCheck() {
        Vocab testVocab = new Vocab();
        testVocab.setIndex(3);
        int mIndex = testVocab.getIndex();
        assertEquals(3, mIndex);
    }
    @Test
    public void VocabWordSetCheck() {
        Vocab testVocab = new Vocab();
        testVocab.setWord(0,"test0");
        assertEquals("test0",testVocab.getWord(0));
        testVocab.setWord(1,"test1");
        assertEquals("test1",testVocab.getWord(1));

    }
}
