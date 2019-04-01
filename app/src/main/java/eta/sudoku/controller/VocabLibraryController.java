package eta.sudoku.controller;

import android.util.Log;

import java.util.ArrayList;

import eta.sudoku.SudokuApplication;
import eta.sudoku.model.Vocab;
import eta.sudoku.model.VocabLibrary;

public class VocabLibraryController {
    private static final VocabLibraryController ourInstance = new VocabLibraryController();


    private VocabLibrary mOverallVocabLib = new VocabLibrary();
    private VocabLibrary mSelectedVocabs = new VocabLibrary();
    private ArrayList<VocabLibrary> mVocabWeeks = new ArrayList<>();
    public VocabLibraryController() {



    }
    public void newGameVocabLib(){
        mSelectedVocabs = new VocabLibrary();
    }
    public void newFullVocabLib(){
        String[][] fullList = SudokuApplication.getInstance().getStringVocabLib();

        int[] clips = SudokuApplication.getInstance().getClips();
        for(int i=0;i<fullList.length;i++){
            Vocab v = new Vocab(fullList[i]);
            v.setIndex(i+1);
            addFullVocab(v);
            setSoundFile(i+1, clips[i]);
        }
    }
    public static VocabLibraryController getInstance() {
        return ourInstance;
    }
//---------------game vocabs
    public int getGameVocabListSize(){
        return mSelectedVocabs.size();
    }
    public int getGameVocabIndex(int index){
        return mSelectedVocabs.get(index).getIndex();
    }
    public void setGameVocabs(VocabLibrary vocabs) {
        mSelectedVocabs = vocabs;
    }
    public String getGameVocab(int index, int lang){
        return mSelectedVocabs.get(index).getWord(lang);
    }
    public void addGameVocab(Vocab vocab){ mSelectedVocabs.add(vocab);}
    public void removeGameVocab(Vocab vocab){ mSelectedVocabs.remove(vocab);}


//--------------overall library
    public void addFullVocab(Vocab vocab){ mOverallVocabLib.add(vocab);}

    public VocabLibrary getOverallVocabLib() {
        return mOverallVocabLib;
    }
    public int getOverallVocabLibSize(){
        return mOverallVocabLib.size();
    }
    public String getOverallVocab(int index, int lang){
        return mOverallVocabLib.get(index).getWord(lang);
    }
    public Vocab getOverallVocab(int index){
        return mOverallVocabLib.get(index);
    }
    public void setFullVocabLib(VocabLibrary fullVocabLib){
        mOverallVocabLib = fullVocabLib;
    }
    //-----------difficult vocab
    public boolean isVocabDifficult(int indexInOverall){
        return mOverallVocabLib.get(indexInOverall).isDifficult();
    }
    public void setVocabDifficult(int indexInOverall, boolean difficult){
        mOverallVocabLib.get(indexInOverall).setDifficult(difficult);
    }
    //---------------sound files
    public int getSoundFile(int index){
        return mOverallVocabLib.get(index).getSoundFile();
    }
    public void setSoundFile(int index, int file){ mOverallVocabLib.get(index).setSoundFile(file);}

//---------------for vocabs by week
    public VocabLibrary getVocabWeek(int week) {
        return mVocabWeeks.get(week);
    }
    public ArrayList<VocabLibrary> getAllWeekVocab(){return mVocabWeeks;}
    public int getTotalWeek(){
        return mVocabWeeks.size();
    }
    public void setVocabWeek(VocabLibrary WeekVocab, int weekInd){
        mVocabWeeks.set(weekInd, WeekVocab);
    }

    public void addVocabIntoWeek(int week, Vocab v){
        mVocabWeeks.get(week).add(v);
    }
}
