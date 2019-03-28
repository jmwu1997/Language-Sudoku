package eta.sudoku;

import android.app.Application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SudokuApplication extends Application {
    private static final SudokuApplication ourInstance = new SudokuApplication();

    private int[][] mPuzzle9 = {
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
    private int[][] mPuzzle6 = {
            {5,3,1,2,6,4},
            {2,4,6,1,5,3},
            {1,6,3,5,4,2},
            {4,5,2,3,1,6},
            {3,1,4,6,2,5},
            {2,6,5,4,3,1}
    };
    private int[][] mPuzzle4 = {
            {1,3,4,2},
            {4,2,1,3},
            {2,4,3,1},
            {3,1,2,4},
    };
    private int[][] mPuzzle12 = {
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
            {10,7,3,8,12,5,4,1,6,2,9,11},
            {4,9,12,2,6,11,3,10,5,1,7,8}
    };


    private VocabLibrary mVocabList = new VocabLibrary();
    private ArrayList<VocabLibrary> mVocabWeeks = new ArrayList<>();

    public boolean[] getSelected() {
        return selected;
    }

    public void setSelected( int i, boolean selected) {
        this.selected[i] = selected;
    }

    private boolean[] selected = new boolean[mVocabList.size()];

    public VocabLibrary getSelectedVocabs() {
        return selectedVocabs;
    }

    public void setSelectedVocabs(VocabLibrary selectedVocabs) {
        this.selectedVocabs = selectedVocabs;
    }

    public int[][] getPuzzle(int size) {
        if(size == 4) return mPuzzle4;
        else if(size == 6) return mPuzzle6;
        else if(size == 9) return mPuzzle9;
        else if(size == 12) return mPuzzle12;
        else
            return null;

    }

    private VocabLibrary selectedVocabs = new VocabLibrary();


    //VocabularyMenu
    //    FullVocabList=> filter(isDifficult)
     //   VocabListByWeek
     //           VocabListWeek# => filter(isDifficult)

    private String[][] mVocabLib = {
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

    final int[] clips= { R.raw.apple, R.raw.pear, R.raw.banana, R.raw.peach, R.raw.grape, R.raw.haw, R.raw.guava,
            R.raw.papaya,R.raw.lemon, R.raw.orange, R.raw.mango, R.raw.fig, R.raw.coconut, R.raw.berry, R.raw.almond,
            R.raw.tomato, R.raw.date, R.raw.durian, R.raw.longan, R.raw.melon };
    static SudokuApplication getInstance() {

        return ourInstance;
    }
    public boolean isVocabDifficult(int index){
        return mVocabList.get(index).isDifficult();
    }
    public void setVocabDifficult(int index){
        mVocabList.get(index).setDifficult(true);
    }


    public SudokuApplication(){
        for(int i=0;i<mVocabLib.length;i++){
            Vocab v = new Vocab(mVocabLib[i]);
            v.setIndex(i+1);
            mVocabList.add(v);

            mVocabList.get(i+1).setSoundFile(clips[i]);

        }
        //for(int i=0; i<mVocabLib.length; i++){
         //   mVocabList.get(i+1).setSoundFile(clips[i]);

        //}

    }
    public VocabLibrary getVocabList() {
        return mVocabList;
    }
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
    public void addVocabIntoLib(Vocab v){
        mVocabList.add(v);
    }
    public void addVocabIntoWeek(int week, Vocab v){
        mVocabWeeks.get(week).add(v);
    }

}
