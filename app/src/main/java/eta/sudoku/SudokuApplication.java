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
            {"Melon", "香瓜"},
    };

    final int[] clips= { R.raw.apple, R.raw.pear, R.raw.banana, R.raw.peach, R.raw.grape, R.raw.haw, R.raw.guava,
            R.raw.papaya,R.raw.lemon, R.raw.orange, R.raw.mango, R.raw.fig, R.raw.coconut, R.raw.berry, R.raw.almond,
            R.raw.tomato, R.raw.date, R.raw.durian, R.raw.longan, R.raw.melon };
    static SudokuApplication getInstance() {

        return ourInstance;
    }
    public void setVocabDifficult(int index){
        mVocabList.get(index).setDifficult(true);
    }


    public SudokuApplication(){
        for(int i=0;i<mVocabLib.length;i++){
            mVocabList.add(new Vocab(mVocabLib[i]));
            mVocabList.get(i+1).setSoundFile(clips[i]);
        }
        //for(int i=0; i<mVocabLib.length; i++){
         //   mVocabList.get(i+1).setSoundFile(clips[i]);

        //}

    }
}
