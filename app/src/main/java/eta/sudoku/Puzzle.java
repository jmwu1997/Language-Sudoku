package eta.sudoku;
//model class

import android.support.annotation.MainThread;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;



public class Puzzle implements Serializable{
    // 1-9 for words, 0 for blank
    private int[][] mPrefilledPuzzle;
    private int[][] mCurrentPuzzle;
    private int[][] mFilledPuzzle;
    private int mPuzzleLang = 0;
    private int mChosenLang = 1;
    public Integer[] mRange;
    private ArrayList<Vocab> mVocabs;
    private int mSize; //4,6,9,12
    // difficulty
    private int mDifficulty;//0,1,2
    private int min; // for difficulty
    private int max;

    private int selectedInd = -1;

    public Puzzle(int[][] savedPuzzle, ArrayList<Vocab> vocab, int size, int difficulty) {//construct with a pre-generated puzzle
        this.mPrefilledPuzzle = new int[size][size];
        this.mCurrentPuzzle = new int[size][size];
        this.mFilledPuzzle = new int[size][size];
        mRange = new Integer[size];
        for(int i=0; i<size; i++){
            mRange[i] = i+1;
        }
        this.mDifficulty = difficulty;
        this.min = size*size*(difficulty+1)/5;

        this.max =  (int) Math.ceil(min*1.1);
        this.mSize = size;
        this.mVocabs = vocab;
        createPuzzle(savedPuzzle);
    }
/*

    protected Puzzle(Parcel in){
        in.readList(mVocabs, getClass().getClassLoader());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mVocabs);
    }





    public static final Parcelable.Creator<Puzzle> CREATOR = new Parcelable.Creator<Puzzle>(){
        @Override
        public Puzzle createFromParcel(Parcel source) {
            return new Puzzle(source);
        }

        @Override
        public Puzzle[] newArray(int size) {
            return new Puzzle[0];
        }
    };*/


    public int getSize() {
        return mSize;
    }

    public String getVocab(int index, int lang){
        return mVocabs.get(index).getWord(lang);
    }
    public void createPuzzle(int[][] savedPuzzle) {//create puzzle with a pre-generated puzzle(number ranging from 1-9, 0 for blank
        for(int i=0; i<mSize; i++) {
            this.mPrefilledPuzzle[i] = savedPuzzle[i].clone(); //copy by value
            this.mCurrentPuzzle[i] = savedPuzzle[i].clone();
        }
    }


    public int[][] getPrefilledPuzzle(){
        return this.mPrefilledPuzzle;
    }
    public int[][] getCurrentPuzzle(){
        return this.mCurrentPuzzle;
    }
    public int[][] getFilledPuzzle() {
        return this.mFilledPuzzle;
    }
    public ArrayList<Vocab> getFullVocab() {
        return mVocabs;
    }

    public void switchLang(){
        int temp = mChosenLang;
        mChosenLang = mPuzzleLang;
        mPuzzleLang = temp;
    }

    public void playsound(){
        int temp = mChosenLang;
        mChosenLang = mPuzzleLang;
        mPuzzleLang = temp;
    }

    public int getPrefilledCell(int row, int col){
        return mPrefilledPuzzle[row][col];
    }
    public int getFilledCell(int row, int col){
        return mFilledPuzzle[row][col];
    }
    public int getCurrentCell(int row, int col){
        return mCurrentPuzzle[row][col];
    }

    public void setPosition(Button[][] ButtonArray, int row, int col) {
        if (this.selectedInd != -1) {
            this.mCurrentPuzzle[row][col] = this.selectedInd;
            this.mFilledPuzzle[row][col] = this.selectedInd;
            ButtonArray[row][col].setText(this.mVocabs.get(this.selectedInd).getWord(this.mChosenLang));
        }
    }
    public void setPosition(TextView[][] cells, int row, int col) {
        if (this.selectedInd != -1) {
            this.mCurrentPuzzle[row][col] = this.selectedInd;
            this.mFilledPuzzle[row][col] = this.selectedInd;
            cells[row][col].setText(this.mVocabs.get(this.selectedInd).getWord(this.mChosenLang));
        }
    }
    public void setSelected(int wordInd) {
        this.selectedInd = wordInd;
    }

    public boolean isBoxEmpty(int row, int col) {//check if (row,col) box is blank
        return this.mCurrentPuzzle[row][col] == 0;
    }


    // For testing only
    public void genFullPuzzle(Vocab[] vocabs, int initLang, TableLayout table) {//gen test puzzle
        for (int i = 0; i < 9; i++) {
            TableRow mTblRow = (TableRow) table.getChildAt(i); //get table row element
            for (int j = 0; j < 9; j++) {
                Button mButton = (Button) mTblRow.getChildAt(j); //get button view
                mButton.setText(vocabs[this.mCurrentPuzzle[i][j]].getWord(initLang)); //write word on the button at position(i,j) from vocabs in "initial" language used for the puzzle
            }
        }
    }

    // TODO: Can be more efficient? (positions generated might overlap)

    public void genRandomPuzzle() {//generate puzzle from random difficulty with bounds defined in this class
        Random r = new Random();
        // pick random difficulty(relating the # of blank cells)
        //TODO: bound negative when easy 6x6
        Log.e(SelectorActivity.TAG, Integer.toString(max) +" "+ Integer.toString(min));
        int difficulty = r.nextInt(this.max-this.min) + this.min;

        // pick random positions
        //mRandomPositions = new int[difficulty][2];
        for (int i = 0; i < difficulty; i++) {
            int x = /*mRandomPositions[i][0] =*/ r.nextInt(mSize);
            int y = /*mRandomPositions[i][1] =*/ r.nextInt(mSize);

            this.mPrefilledPuzzle[x][y] = 0;
            this.mCurrentPuzzle[x][y] = 0;
        }
    }

    public boolean isCompleted() { //check if all the cells are filled
        for (int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {
                if (isBoxEmpty(i, j)) return false;
            }
        }
        return true;
    }

    public boolean isRowSolved(int row) {
        assert row >= 0 & row < mSize;
        //convert int[] to Integer[]
        ArrayList<Integer> mRow = new ArrayList<>(0);
        for (int i = 0; i < mSize; i++) {
            if(mCurrentPuzzle[row][i] != 0) mRow.add(Integer.valueOf(this.mCurrentPuzzle[row][i]));
        }
        if (!mRow.containsAll(Arrays.asList(mRange))) { //mRange have to be Integer[]
            //checks if mRow contains 1-9
            System.out.print("row ");
            System.out.print(row);
            System.out.println(" unsolved");
            return false;
        }
        return true;
    }

    public boolean isColSolved(int col) {
        assert col >= 0 & col < mSize;

        Integer[] mCol = new Integer[mSize];
        for (int i = 0; i < mSize; i++) {
            mCol[i] = Integer.valueOf(this.mCurrentPuzzle[i][col]);
        }
        if (!Arrays.asList(mCol).containsAll(Arrays.asList(mRange))) {
            //checks if mCol contains 1-9
            System.out.print("col ");
            System.out.print(col);
            System.out.println(" unsolved");
            return false;
        }
        return true;
    }

    public boolean isSubSolved(int sub) {
        //sub index:
        //0 1 2   0 1    0 1    0 1 2
        //3 4 5   2 3    2 3    3 4 5
        //6 7 8          4 5    6 7 8
        //                      9 1011
        assert sub >= 0 & sub < mSize;
        int r = 0;
        int c = 0;
        switch(mSize) {
            case 4:
            case 9: r =(int)Math.sqrt(mSize);
                c = r;
                break;
            case 6: r = 2;
                c = 3;
                break;
            case 12: r = 3;
                c = 4;
                break;
            default:
                System.out.print("mSize out of range");
                break;
        }
        assert r == 0 || c == 0;
        Integer[] mSub = new Integer[mSize];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                mSub[i * c + j] = Integer.valueOf(this.mCurrentPuzzle[(sub / r) * r + i][(sub % r) * c + j]);
            }
        }
        if (!Arrays.asList(mSub).containsAll(Arrays.asList(mRange))) {
            //checks if mSub contains 1-9
            System.out.print("sub ");
            System.out.print(sub);
            System.out.println(" unsolved");
            return false;
        }
        return true;
    }

    public boolean isSolved() {
        if (!isCompleted()) return false;
        for (int i = 0; i < mSize; i++) {
            if (!isRowSolved(i) || !isColSolved(i) || !isSubSolved(i)) return false;
        }
        return true;
    }


    public boolean isDuplicateInRow(int row){
        boolean[] bitmap = new boolean[mSize];
        for(int i=0; i<mSize; i++){
            if(mCurrentPuzzle[row][i] != 0)
                if(!(bitmap[mCurrentPuzzle[row][i]-1] ^= true)) return true;
        }
        return false;
    }
    public boolean isDuplicateInCol(int col){
        boolean[] bitmap = new boolean[mSize];
        for(int i=0; i<mSize; i++){
            if(mCurrentPuzzle[i][col] != 0)
                if(!(bitmap[mCurrentPuzzle[i][col]-1] ^= true)) return true;
        }
        return false;
    }
    public boolean isDuplicateInSub(int sub){
        boolean[] bitmap = new boolean[mSize];
        assert sub >= 0 & sub < mSize;
        int r = 0;
        int c = 0;
        switch(mSize) {
            case 4:
            case 9: r =(int)Math.sqrt(mSize);
                c = r;
                break;
            case 6: r = 2;
                c = 3;
                break;
            case 12: r = 3;
                c = 4;
                break;
            default:
                System.out.print("mSize out of range");
                break;
        }
        assert r == 0 || c == 0;

        for(int i=0; i<r; i++){
            for(int j=0; j<c; j++) {
                if (mCurrentPuzzle[(sub / r) * r + i][(sub % r) * c + j] != 0)
                    if (!(bitmap[mCurrentPuzzle[(sub / r) * r + i][(sub % r) * c + j]-1] ^= true)) return true;
            }
        }
        return false;
    }
}


