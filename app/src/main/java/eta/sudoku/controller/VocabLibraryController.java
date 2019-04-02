package eta.sudoku.controller;

import android.util.Log;

import java.util.ArrayList;

import eta.sudoku.SudokuApplication;
import eta.sudoku.model.Vocab;
import eta.sudoku.model.VocabLibrary;
import eta.sudoku.model.VocabStorage;

public class VocabLibraryController {
    private static final VocabLibraryController ourInstance = new VocabLibraryController();
    private static final VocabStorage storageController = VocabStorage.getInstance();




    private VocabLibrary mVocabLib = new VocabLibrary();

    private VocabLibrary mOverallVocabLib = new VocabLibrary();
    private VocabLibrary mSelectedVocabs = new VocabLibrary();


    private String name;
    private ArrayList<VocabLibrary> mVocabWeeks = new ArrayList<>();
    public VocabLibraryController() {



    }
    public void updateLibrary(){
        String[] lists=storageController.getWordLists();
        int length = lists.length;
        int length2;
        Vocab v;
        for(int i=0;i<length;i++){
            mSelectedVocabs=storageController.loadList(lists[i]);
            length2=mSelectedVocabs.size();
            for(int j=0;j<length2;j++){
                v=mSelectedVocabs.get(j);
                addLibrary(v);
            }
        }
    }
    public VocabLibrary getmVocabLib() {
        return mVocabLib;
    }
    private boolean inLibrary(Vocab v){
        int length = mVocabLib.size();
        boolean inlib=false;
        for( int i =0;i<length;i++){
            if( getLibVocab(i,0)==v.getWord(0)){
                inlib=true;
            }
        }
        return inlib;
    }
    public boolean addLibrary(Vocab v){
        boolean inlib=false;

        if(!inlib){
            mVocabLib.add(v);
        }
        return !inlib;
    }
    public void setmVocabLib(VocabLibrary mVocabLib) {
        this.mVocabLib = mVocabLib;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void saveCurrentList(){
        storageController.saveList(mOverallVocabLib,name);
    }


    public void newGameVocabLib(){
        mSelectedVocabs = new VocabLibrary();
    }
    public void newFullVocabLib(){
        String[][] fullList = SudokuApplication.getInstance().getStringVocabLib();
        if(fullList.length == 0){
            Log.e("not good",Integer.toString(fullList.length));
        }
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
    public VocabLibrary getGameVocabs(VocabLibrary vocabs) {
        return mSelectedVocabs;
    }

    public String getGameVocab(int index, int lang){
        return mSelectedVocabs.get(index).getWord(lang);
    }
    public String getLibVocab(int index, int lang){
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
