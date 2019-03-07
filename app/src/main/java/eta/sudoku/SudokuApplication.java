package eta.sudoku;

import android.app.Application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SudokuApplication extends Application {
    private static final SudokuApplication ourInstance = new SudokuApplication();

    public VocabLibrary getVocabList() {
        return mVocabList;
    }
    public VocabLibrary getVocabWeek(int week) {
        return mVocabWeeks.get(week);
    }
    public int getTotalWeek(){
        return mVocabWeeks.size();
    }
    private VocabLibrary mVocabList = new VocabLibrary();
    private ArrayList<VocabLibrary> mVocabWeeks = new ArrayList<>(2);
    private String[][] mVocabLib = {
            {"eleven","十一"},
            {"two","二"},
            {"three","三"},
            {"four","四"},
            {"five","五"},
            {"six","六"},
            {"seven","七"},
            {"eight","八"},
            {"nine","九"},
    };

    static SudokuApplication getInstance() {

        return ourInstance;
    }



    public SudokuApplication(){
        for(int i=0;i<mVocabLib.length;i++){
            mVocabList.add(new Vocab(mVocabLib[i]));
        }


    }
}
