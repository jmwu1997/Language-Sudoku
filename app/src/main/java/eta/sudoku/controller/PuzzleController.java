
package eta.sudoku.controller;

import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import eta.sudoku.SudokuApplication;
import eta.sudoku.model.Puzzle;
import eta.sudoku.model.Vocab;
import eta.sudoku.view.PuzzleActivity;
//controller with direct access to the instance of puzzle
public class PuzzleController {
    private static final PuzzleController ourInstance = new PuzzleController();

    private Puzzle mPuzzle;


    public PuzzleController() {

    }
    public Puzzle newPuzzle(int size, int difficulty){
        mPuzzle = new Puzzle(SudokuApplication.getInstance().getPuzzle(size), size, difficulty);
        mPuzzle.genRandomPuzzle();
        return mPuzzle;
    }


    public void fillCell(int word, int row, int col){
        mPuzzle.setCurrentCell(word, row, col);
        mPuzzle.setFilledCell(word, row, col);
    }


    public int getSize() {
        return mPuzzle.getSize();
    }
    public int getDifficulty(){return mPuzzle.getDifficulty();}
    public int[][] getPrefilledPuzzle(){
        return mPuzzle.getPrefilledPuzzle();
    }

    public int[][] getCurrentPuzzle(){
        return mPuzzle.getCurrentPuzzle();
    }

    public int[][] getFilledPuzzle(){
        return mPuzzle.getFilledPuzzle();
    }
    public int getFilledCell(int row, int col){
        return mPuzzle.getFilledPuzzle()[row][col];
    }
    public int getPrefilledCell(int row, int col){
        return mPuzzle.getPrefilledPuzzle()[row][col];
    }
    public int getCurrentCell(int row, int col){
        return mPuzzle.getCurrentPuzzle()[row][col];
    }

    public boolean isCompleted() { //check if all the cells are filled
        for (int i = 0; i < mPuzzle.getSize(); i++) {
            for (int j = 0; j < mPuzzle.getSize(); j++) {
                if (mPuzzle.isCellEmpty(i, j)) return false;
            }
        }
        return true;
    }
    public boolean isRowSolved(int row) {
        assert row >= 0 & row < mPuzzle.getSize();
        //convert int[] to Integer[]
        ArrayList<Integer> mRow = new ArrayList<>(0);
        for (int i = 0; i < mPuzzle.getSize(); i++) {
            if(getCurrentCell(row, i) != 0) mRow.add(Integer.valueOf(getCurrentCell(row, i)));
        }
        if (!mRow.containsAll(Arrays.asList(Puzzle.mRange))) { //mRange have to be Integer[]
            //checks if mRow contains 1-9
            System.out.print("row ");
            System.out.print(row);
            System.out.println(" unsolved");
            return false;
        }
        return true;
    }

    public boolean isColSolved(int col) {
        assert col >= 0 & col < mPuzzle.getSize();

        Integer[] mCol = new Integer[mPuzzle.getSize()];
        for (int i = 0; i < mPuzzle.getSize(); i++) {
            mCol[i] = Integer.valueOf(getCurrentCell(i,col));
        }
        if (!Arrays.asList(mCol).containsAll(Arrays.asList(Puzzle.mRange))) {
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
        assert sub >= 0 & sub < mPuzzle.getSize();
        int r = 0;
        int c = 0;
        switch(mPuzzle.getSize()) {
            case 4:
            case 9: r =(int)Math.sqrt(mPuzzle.getSize());
                c = r;
                break;
            case 6: r = 2;
                c = 3;
                break;
            case 12: r = 3;
                c = 4;
                break;
            default:
                System.out.print("Size out of range");
                break;
        }
        assert r == 0 || c == 0;
        Integer[] mSub = new Integer[mPuzzle.getSize()];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                mSub[i * c + j] = Integer.valueOf(getCurrentCell((sub / r) * r + i, (sub % r) * c + j));
            }
        }
        if (!Arrays.asList(mSub).containsAll(Arrays.asList(Puzzle.mRange))) {
            //checks if mSub contains 1-9
            System.out.print("sub ");
            System.out.print(sub);
            System.out.println(" unsolved");
            return false;
        }
        return true;
    }



    public boolean isDuplicateInRow(int row){
        boolean[] bitmap = new boolean[mPuzzle.getSize()];
        for(int i=0; i<mPuzzle.getSize(); i++){
           if(getCurrentCell(row,i) != 0)
                if(!(bitmap[getCurrentCell(row, i)-1] ^= true)) return true;
        }
        return false;
    }
    public boolean isDuplicateInCol(int col){
        boolean[] bitmap = new boolean[mPuzzle.getSize()];
        for(int i=0; i<mPuzzle.getSize(); i++){
            if(getCurrentCell(i,col) != 0)
                if(!(bitmap[getCurrentCell(i, col)-1] ^= true)) return true;
        }
        return false;
    }
    public boolean isDuplicateInSub(int sub){
        boolean[] bitmap = new boolean[mPuzzle.getSize()];
        assert sub >= 0 & sub < mPuzzle.getSize();
        int r = 0;
        int c = 0;
        switch(mPuzzle.getSize()) {
            case 4:
            case 9: r =(int)Math.sqrt(mPuzzle.getSize());
                c = r;
                break;
            case 6: r = 2;
                c = 3;
                break;
            case 12: r = 3;
                c = 4;
                break;
            default:
                System.out.print("Size out of range");
                break;
        }
        assert r == 0 || c == 0;

        for(int i=0; i<r; i++){
            for(int j=0; j<c; j++) {
                if (getCurrentCell((sub / r) * r + i,(sub % r) * c + j) != 0)
                    if (!(bitmap[getCurrentCell((sub / r) * r + i,(sub % r) * c + j)-1] ^= true)) return true;
            }
        }
        return false;
    }



    public static eta.sudoku.controller.PuzzleController getInstance() {
        return ourInstance;
    }
}


