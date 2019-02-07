package eta.sudoku;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
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
    private int mChosenLang = 1;
    //private int[] mRange = {1,2,3,4,5,6,7,8,9};
    private Integer[] mRange = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Vocab[] mVocabs;
    private int[][] mRandomPositions;


    // Not needed in current implementation
    private Button mButtonArray[][] = new Button[9][9];
    // difficulty
    private int min = 26; // for difficulty
    private int max = 35;

    private int selectedInd = -1;

    public Puzzle(int[][] savedPuzzle, Vocab[] vocab, TableLayout table, Context context) {//construct with a pre-generated puzzle
        createPuzzle(savedPuzzle);
        mVocabs = vocab;
        createButton(mPuzzleLang, table, context);
    }

    public void createPuzzle(int[][] savedPuzzle) {//create puzzle with a pre-generated puzzle(number ranging from 1-9, 0 for blank
        mPuzzle = savedPuzzle;
    }

    public void createButton(int initLang, TableLayout table, Context context) {
        //programmatically create buttons in the table(layout)
        Resources r = context.getResources();
        //converting dp to pixel
        float mDp2Px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());

        for (int i = 0; i < 9; i++) {
            TableRow mTblRow = new TableRow(context);
            mTblRow.setGravity(Gravity.CENTER); //set gravity:center
            table.addView(mTblRow);

            for (int j = 0; j < 9; j++) {
                final int row = i;
                final int col = j;
                Button mButton = new Button(context);
                mButton.setText(mVocabs[mPuzzle[i][j]].getWord(initLang));

                mTblRow.addView(mButton);
                //set layout height and width for puzzle buttons
                ViewGroup.LayoutParams mButtonLayoutParams = mButton.getLayoutParams();
                mButtonLayoutParams.height = (int) (40 * mDp2Px); //android:layout_height="40dp"
                mButtonLayoutParams.width = (int) (40 * mDp2Px); //android:layout_width="40dp"
                //set layout margin for puzzle buttons
                ViewGroup.MarginLayoutParams mButtonMarginLayoutParams = (ViewGroup.MarginLayoutParams) mButton.getLayoutParams();
                mButtonMarginLayoutParams.setMargins((int) (3 * mDp2Px), (int) (3 * mDp2Px), (int) (3 * mDp2Px), (int) (3 * mDp2Px));
                mButton.setLayoutParams(mButtonMarginLayoutParams);
                //decapitalize button text
                mButton.setTransformationMethod(null);

                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setPosition(row, col);
                    }
                });

                //all puzzle buttons not clickable
                mButton.setClickable(false);
                mButton.setBackgroundColor(Color.alpha(0));

                mButtonArray[i][j] = mButton;
            }
        }

    }



    public void switchLang(TableLayout selectionTable , int newPuzzleLang, int newSelLang){
        mChosenLang = newSelLang;
        mPuzzleLang = newPuzzleLang;
        for (int i = 0; i < 9; i++) {
            //TableRow mTblRow = (TableRow) puzzleTable.getChildAt(i); //get table row element
            for (int j = 0; j < 9; j++) {
                //Button mButton = (Button) mTblRow.getChildAt(j); //get button view
                mButtonArray[i][j].setText(mVocabs[mPuzzle[i][j]].getWord(mPuzzleLang)); //write word on the button at position(i,j) from vocabs in "initial" language used for the puzzle
            }
        }

        for (int i = 0; i < 3; i++) {
            TableRow mTblRow = (TableRow) selectionTable.getChildAt(i); //get table row element
            for (int j = 0; j < 3; j++) {
                Button mButton = (Button) mTblRow.getChildAt(j); //get button view
                mButton.setText(mVocabs[i*3+j+1].getWord(mChosenLang)); //write word on the button at position(i,j) from vocabs in "initial" language used for the puzzle
            }
        }
    }

    // set button text to selected
    private void setPosition(int row, int col) {
        if (selectedInd != -1) {
            mPuzzle[row][col] = selectedInd;
            mButtonArray[row][col].setText(mVocabs[mPuzzle[row][col]].getWord(mChosenLang));
        }
        return;
    }

    public void setSelected(int wordInd) {
        selectedInd = wordInd;
    }

    public boolean isBoxEmpty(int row, int col) {//check if (row,col) box is blank
        if (mPuzzle[row][col] == 0) return true;
        else return false;
    }


    // For testing only
    public void genFullPuzzle(Vocab[] vocabs, int initLang, TableLayout table) {//gen test puzzle
        for (int i = 0; i < 9; i++) {
            TableRow mTblRow = (TableRow) table.getChildAt(i); //get table row element
            for (int j = 0; j < 9; j++) {
                Button mButton = (Button) mTblRow.getChildAt(j); //get button view
                mButton.setText(vocabs[mPuzzle[i][j]].getWord(initLang)); //write word on the button at position(i,j) from vocabs in "initial" language used for the puzzle
            }
        }
    }

    // TODO: Can be more efficient? (positions generated might overlap)
    public void genRandomPuzzle(Vocab[] vocabs, int initLang, TableLayout table) {//generate puzzle from random difficulty with bounds defined in this class
        Random r = new Random();
        // pick random difficulty(relating the # of blank cells)
        int difficulty = r.nextInt(max-min) + min;

        // pick random positions
        mRandomPositions = new int[difficulty][2];
        for (int i = 0; i < difficulty; i++) {
            int x = mRandomPositions[i][0] = r.nextInt(9);
            int y = mRandomPositions[i][1] = r.nextInt(9);

            mPuzzle[x][y] = 0;
            mButtonArray[x][y].setText(vocabs[mPuzzle[x][y]].getWord(initLang));

            //set empty array to be clickable
            mButtonArray[x][y].setClickable(true);
        }


        //return randomPositions;
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
            mRow[i] = Integer.valueOf(mPuzzle[row][i]);
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
            mCol[i] = Integer.valueOf(mPuzzle[i][col]);
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
                mSub[i * 3 + j] = Integer.valueOf(mPuzzle[(sub / 3) * 3 + i][(sub % 3) * 3 + j]);
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


