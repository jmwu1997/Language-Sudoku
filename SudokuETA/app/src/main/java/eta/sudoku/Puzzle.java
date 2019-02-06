package eta.sudoku;


import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;


import java.util.Arrays;
import java.util.Random;


public class Puzzle {
    // 1-9 for words, 0 for blank
    private int[][] mPuzzle;
    //private int[] mRange = {1,2,3,4,5,6,7,8,9};
    private Integer[] mRange = {1,2,3,4,5,6,7,8,9};

    // Difficulty: randomly select number of given numbers in range min and max
    private int max = 35;
    private int min = 26;

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
        //Assumed: the position is for user to put a word into, not for the given puzzle
        //it's not checking for validity
        mPuzzle[row][col] = wordIndex;
    }

    public void genFullPuzzle(Vocab[] vocabs, int initLang, TableLayout table){//gen test puzzle
        for(int i=0;i<9;i++){
            TableRow mTblRow = (TableRow)table.getChildAt(i); //get table row element
            for(int j=0;j<9;j++) {
                Button mButton = (Button)mTblRow.getChildAt(j); //get button view
                mButton.setText(vocabs[mPuzzle[i][j] - 1].getWord(initLang)); //write word on the button at position(i,j) from vocabs in "initial" language used for the puzzle
            }
        }
    }

    // TODO: Can be more efficient?
    public int[][] genRandomPuzzle(Vocab[] vocabs, int initLang, TableLayout table){//generate puzzle from random difficulty with bounds defined in this class
        Button[][] boardButtons = new Button[9][9];
        for(int i=0;i<9;i++){
            TableRow mTblRow = (TableRow)table.getChildAt(i); //get table row element
            for(int j=0;j<9;j++) {
                Button mButton = (Button)mTblRow.getChildAt(j); //get button view
                boardButtons[i][j] = mButton;
            }
        }
        Random r = new Random();
        // pick random difficulty
        int difficulty = r.nextInt(max-min) + min;
        // pick random positions
        int[][] randomPositions = new int[difficulty][2];
        for(int i = 0; i<difficulty; i++) {
            int x = randomPositions[i][0] = r.nextInt(9);
            int y = randomPositions[i][1] = r.nextInt(9);
            boardButtons[x][y].setText(vocabs[mPuzzle[x][y] - 1].getWord(initLang));
        }
        return randomPositions;
    }

    public boolean isCompleted(){ //check if all the cells are filled
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++) {
                if(isBoxEmpty(i,j)) return false;
            }
        }
        return true;
    }

    private boolean isRowSolved(int row){
        assert row>=0 & row<9;
        //convert int[] to Integer[]
        Integer[] mRow = new Integer[9];
        for(int i=0; i<9; i++){
            mRow[i] = Integer.valueOf(mPuzzle[row][i]);
        }
        if( !Arrays.asList(mRow).containsAll(Arrays.asList(mRange)) ){ //mRow and mRange have to be Integer[]
            //checks if mRow contains 1-9
            System.out.print("row ");
            System.out.print(row);
            System.out.println(" unsolved");
            return false;
        }
        return true;
    }
    private boolean isColSolved(int col) {
        assert col>=0 & col<9;

        Integer[] mCol = new Integer[9];
        for(int i=0; i<9; i++){
            mCol[i] = Integer.valueOf(mPuzzle[i][col]);
        }
        if( !Arrays.asList(mCol).containsAll(Arrays.asList(mRange)) ){
            //checks if mCol contains 1-9
            System.out.print("col ");
            System.out.print(col);
            System.out.println(" unsolved");
            return false;
        }
        return true;
    }
    private boolean isSubSolved(int sub){
        //sub index:
        //0 1 2
        //3 4 5
        //6 7 8
        assert sub>=0 & sub<9;

        Integer[] mSub = new Integer[9];
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                mSub[i*3+j] = Integer.valueOf(mPuzzle[(sub/3)*3+i][(sub%3)*3+j]);
            }
        }
        if( !Arrays.asList(mSub).containsAll(Arrays.asList(mRange)) ){
            //checks if mSub contains 1-9
            System.out.print("sub ");
            System.out.print(sub);
            System.out.println(" unsolved");
            return false;
        }
        return true;
    }
    public boolean isSolved(){
        if(!isCompleted()) return false;
        for(int i=0; i<9; i++){
            if(!isRowSolved(i) || !isColSolved(i) || !isSubSolved(i)) return false;
        }
        return true;
    }
}
