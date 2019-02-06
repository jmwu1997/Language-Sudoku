package eta.sudoku;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;


import java.util.Arrays;
import java.util.Random;


public class Puzzle {
    // 1-9 for words, 0 for blank
    private int[][] mPuzzle;
    private int mPuzzleLang = 0;
    //private int[] mRange = {1,2,3,4,5,6,7,8,9};
    private Integer[] mRange = {1,2,3,4,5,6,7,8,9};
    private Vocab[] mVocabs;

    // Not needed in current implementation
    //private Button mButtonArray[][] = new Button[9][9];

    // difficulty
    private int min = 26;
    private int max = 35;

    private String selected;

    public Puzzle(int[][] savedPuzzle, Vocab[] vocab, TableLayout table, Context context){//construct with a pre-generated puzzle
        createPuzzle(savedPuzzle);
        mVocabs = vocab;
        createButton(mPuzzleLang, table, context);
    }
    public Puzzle(){
        mPuzzle = new int[9][9];
    }

    public void createPuzzle(int[][] savedPuzzle){//create puzzle with a pre-generated puzzle(number ranging from 1-9, 0 for blank
        mPuzzle = savedPuzzle;
    }

    public void createButton(int initLang, TableLayout table, Context context){
        //programmatically create buttons in the table(layout)
        Resources r = context.getResources();
        //converting dp to pixel
        float mDp2Px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());

        for(int i=0;i<9;i++) {
            TableRow mTblRow = new TableRow(context);
            mTblRow.setGravity(Gravity.CENTER); //set gravity:center
            table.addView(mTblRow);
            ViewGroup.MarginLayoutParams mTRParams = (ViewGroup.MarginLayoutParams) mTblRow.getLayoutParams();

            //mTRParams.setMargins(0,0,0,0);

            for(int j=0;j<9;j++){
                Button mButton = new Button(context);

                // This will be done in genRandomPuzzle
                //mButton.setText(mVocabs[mPuzzle[i][j]].getWord(initLang));//place word onto the button

                mTblRow.addView(mButton);
                ViewGroup.LayoutParams mButtonLayoutParams = mButton.getLayoutParams();
                mButtonLayoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics()); //not dp, should check the factor later
                mButtonLayoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
                ViewGroup.MarginLayoutParams mButtonMarginLayoutParams = (ViewGroup.MarginLayoutParams) mButton.getLayoutParams();

                int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, r.getDisplayMetrics());
                mButtonMarginLayoutParams.setMargins(margin,margin,margin,margin);
                mButton.setLayoutParams(mButtonMarginLayoutParams);
                mButton.setTransformationMethod(null);//decapitalize button text

                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        setPosition(button);
                    }
                });

                // Not needed in current implementation
                //mButtonArray[i][j] = mButton;

            }
        }

    }

    // set button text to selected
    private void setPosition(Button button) {
        if(selected != null) {
            button.setText(selected);
        }
        //Log.d(TAG, "selectPosition() with selected: " + selected + " called");
        return;
    }

    public void setSelected(String word) {
        selected = word;
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

    // For testing only
    public void genFullPuzzle(Vocab[] vocabs, int initLang, TableLayout table){//gen test puzzle
        for(int i=0;i<9;i++){
            TableRow mTblRow = (TableRow)table.getChildAt(i); //get table row element
            for(int j=0;j<9;j++) {
                Button mButton = (Button)mTblRow.getChildAt(j); //get button view
                mButton.setText(vocabs[mPuzzle[i][j]].getWord(initLang)); //write word on the button at position(i,j) from vocabs in "initial" language used for the puzzle
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
            boardButtons[x][y].setText(vocabs[mPuzzle[x][y]].getWord(initLang));
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
