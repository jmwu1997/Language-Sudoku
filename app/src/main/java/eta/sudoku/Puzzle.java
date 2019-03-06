package eta.sudoku;
//model class

import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;



public class Puzzle implements Serializable{
    // 1-9 for words, 0 for blank
    private int[][] mPrefilledPuzzle = new int[9][9];
    private int[][] mCurrentPuzzle = new int[9][9];
    private int[][] mFilledPuzzle = new int[9][9];
    private int mPuzzleLang = 0;
    private int mChosenLang = 1;
    private Integer[] mRange = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private ArrayList<Vocab> mVocabs;
    // difficulty
    private int min = 26; // for difficulty
    private int max = 35;

    private int selectedInd = -1;

    public Puzzle(int[][] savedPuzzle, ArrayList<Vocab> vocab) {//construct with a pre-generated puzzle
        createPuzzle(savedPuzzle);
        this.mVocabs = vocab;

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




    public String getVocab(int index, int lang){
        return mVocabs.get(index).getWord(lang);
    }
    public void createPuzzle(int[][] savedPuzzle) {//create puzzle with a pre-generated puzzle(number ranging from 1-9, 0 for blank
        for(int i=0; i<9; i++) {
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
        int difficulty = r.nextInt(this.max-this.min) + this.min;

        // pick random positions
        //mRandomPositions = new int[difficulty][2];
        for (int i = 0; i < difficulty; i++) {
            int x = /*mRandomPositions[i][0] =*/ r.nextInt(9);
            int y = /*mRandomPositions[i][1] =*/ r.nextInt(9);

            this.mPrefilledPuzzle[x][y] = 0;
            this.mCurrentPuzzle[x][y] = 0;


        }
    }

    public boolean isCompleted() { //check if all the cells are filled
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (isBoxEmpty(i, j)) return false;
            }
        }
        return true;
    }

    private boolean isRowSolved(int row) {
        assert row >= 0 & row < 9;
        //convert int[] to Integer[]
        Integer[] mRow = new Integer[9];
        for (int i = 0; i < 9; i++) {
            mRow[i] = Integer.valueOf(this.mCurrentPuzzle[row][i]);
        }
        if (!Arrays.asList(mRow).containsAll(Arrays.asList(mRange))) { //mRow and mRange have to be Integer[]
            //checks if mRow contains 1-9
            System.out.print("row ");
            System.out.print(row);
            System.out.println(" unsolved");
            return false;
        }
        return true;
    }

    private boolean isColSolved(int col) {
        assert col >= 0 & col < 9;

        Integer[] mCol = new Integer[9];
        for (int i = 0; i < 9; i++) {
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

    private boolean isSubSolved(int sub) {
        //sub index:
        //0 1 2
        //3 4 5
        //6 7 8
        assert sub >= 0 & sub < 9;

        Integer[] mSub = new Integer[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mSub[i * 3 + j] = Integer.valueOf(this.mCurrentPuzzle[(sub / 3) * 3 + i][(sub % 3) * 3 + j]);
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
        for (int i = 0; i < 9; i++) {
            if (!isRowSolved(i) || !isColSolved(i) || !isSubSolved(i)) return false;
        }
        return true;
    }





}


