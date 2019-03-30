package eta.sudoku.controller;

import android.util.Log;

import eta.sudoku.model.VocabLibrary;

public class VocabLibraryController {
    private static final VocabLibraryController ourInstance = new VocabLibraryController();



    private VocabLibrary mSelectedVocabs = new VocabLibrary();
    public VocabLibraryController() {

    }


    public static VocabLibraryController getInstance() {
        return ourInstance;
    }
    public String getGameVocab(int index, int lang){
        return mSelectedVocabs.get(index).getWord(lang);
    }
    public int getGameVocabIndex(int index){
        return mSelectedVocabs.get(index).getIndex();
    }
    public void setGameVocabs(VocabLibrary vocabs) {
        Log.e("size", vocabs.get(3).getWord(1));
        mSelectedVocabs = vocabs;
    }
    public int getSoundFile(int index){
        return mSelectedVocabs.get(index).getSoundFile();
    }

}
