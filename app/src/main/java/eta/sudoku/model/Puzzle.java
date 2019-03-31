package eta.sudoku.model;
//model class


import java.io.Serializable;
import java.util.Random;

import eta.sudoku.controller.GameController;


public class Puzzle implements Serializable{
    private static final GameController gameController = GameController.getInstance();
    // 1-9 for words, 0 for blank
    private int[][] mPrefilledPuzzle;
    private int[][] mCurrentPuzzle;
    private int[][] mFilledPuzzle;

    public static Integer[] mRange;

    private int mSize; //4,6,9,12 for sudoku size
    // difficulty
    private int mDifficulty;//0,1,2
    private int min; // for difficulty
    private int max;



    public Puzzle(int[][] savedPuzzle, int size, int difficulty) {//construct with a pre-generated puzzle
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

    public int getDifficulty() {return mDifficulty;}
    public int getSize() {
        return mSize;
    }


    public void createPuzzle(int[][] savedPuzzle) {//create puzzle with a pre-generated puzzle(number ranging from 1-9, 0 for blank
        for(int i=0; i<mSize; i++) {
            this.mPrefilledPuzzle[i] = savedPuzzle[i].clone(); //copy by value
            this.mCurrentPuzzle[i] = savedPuzzle[i].clone();
        }
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
    public void setCurrentCell(int word, int row, int col){
        mCurrentPuzzle[row][col] = word;
    }
    public void setFilledCell(int word, int row, int col){
        mFilledPuzzle[row][col] = word;
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
    public boolean isCellEmpty(int row, int col) {//check if (row,col) box is blank
        return this.mCurrentPuzzle[row][col] == 0;
    }


    // TODO: Can be more efficient? (positions generated might overlap)

    public void genRandomPuzzle() {//generate puzzle from random difficulty with bounds defined in this class
        Random r = new Random();
        // pick random difficulty(relating the # of blank cells)
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




}


