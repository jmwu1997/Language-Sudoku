package eta.sudoku;

import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Puzzle {
    private int[][] mPuzzle;

    public Puzzle(int[][] savedPuzzle){//construct with a pre-generated puzzle
        createPuzzle(savedPuzzle);
    }
    public Puzzle(){
        mPuzzle = new int[9][9];
    }

    public void createPuzzle(int[][] savedPuzzle){//create puzzle with a pre-generated puzzle(number ranging from 1-9, 0 for blank
        mPuzzle = savedPuzzle;
    }
    public boolean isBoxEmpty(int row, int col){//check if (row,col) box is blank
        if(mPuzzle[row][col]==0) return true;
        else return false;
    }
    public void putWord(int wordIndex, int row, int col){//put specific word into position(row,col) using its index from vocab library
        //put 1-9 for words, 0 for blank
        //Assumed: the position is for user to put a word into, not for the given puzzle
        //it's not checking for validity
        mPuzzle[row][col] = wordIndex;
    }
    /*
    public void genPuzzle(Vocab[] vocabs, int initLang){//for generating test puzzle in console
        System.out.println("test puzzle");
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++) {
                System.out.print(vocabs[mPuzzle[i][j]].getWord(initLang));
            }
            System.out.println();
        }
    }
    */
    public void genPuzzleDevice(Vocab[] vocabs, int initLang, TableLayout table){//for gen test puzzle on device
        //!!!method name will be changed later!!!
        for(int i=0;i<9;i++){
            TableRow mTblRow = (TableRow)table.getChildAt(i); //get table row element
            for(int j=0;j<9;j++) {
                Button mButton = (Button)mTblRow.getChildAt(j); //get button view
                mButton.setText(vocabs[mPuzzle[i][j]].getWord(initLang)); //write word on the button at position(i,j) from vocabs in "initial" language used for the puzzle
            }
        }
    }
}
