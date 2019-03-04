package eta.sudoku;
//model class
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;



public class Puzzle implements Serializable {
    // 1-9 for words, 0 for blank
    private int[][] mPrefilledPuzzle;
    private int[][] mCurrentPuzzle;
    private int[][] mFilledPuzzle = new int[9][9];
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

    public Puzzle(int[][] savedPuzzle, Vocab[] vocab) {//construct with a pre-generated puzzle
        createPuzzle(savedPuzzle);
        mVocabs = vocab;

    }



    public void createPuzzle(int[][] savedPuzzle) {//create puzzle with a pre-generated puzzle(number ranging from 1-9, 0 for blank
        mPrefilledPuzzle = savedPuzzle;
        mCurrentPuzzle = savedPuzzle;
    }


    public int[][] getPrefilledPuzzle(){
        return mPrefilledPuzzle;
    }

    public void createButton(int initLang, GridLayout grid, final Context context) {
        //programmatically create buttons in the table(layout)
        Resources r = context.getResources();
        //convert dp to pixel
        float mDp2Px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());
        int height;
        int width;



        for (int i = 0; i < 9; i++) {
            //TableRow mTblRow = new TableRow(context);
            //mTblRow.setGravity(Gravity.CENTER); //set gravity:center
            //grid.addView(mTblRow);

            for (int j = 0; j < 9; j++) {
                final int row = i;
                final int col = j;
                final Button mButton = new Button(context);
                mButton.setText(mVocabs[mPrefilledPuzzle[i][j]].getWord(initLang));

                grid.addView(mButton);

                //set adaptable width and height
                ViewGroup.LayoutParams mButtonLayoutParams = mButton.getLayoutParams();
                mButtonLayoutParams.height = (int) (0 * mDp2Px);
                mButtonLayoutParams.width = (int) (0 * mDp2Px);
                ((GridLayout.LayoutParams) mButton.getLayoutParams()).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                ((GridLayout.LayoutParams) mButton.getLayoutParams()).rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);


                mButton.setGravity(Gravity.CENTER);
                mButton.setShadowLayer(0,0,0, Color.alpha(0));


                mButton.setPadding(0,0,0,0);
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
                //mButton.setBackgroundColor(Color.alpha(0));

                mButtonArray[i][j] = mButton;
            }
        }

    }


    public void switchLang(TableLayout selectionTable , int newPuzzleLang, int newSelLang){
        mChosenLang = newSelLang;
        mPuzzleLang = newPuzzleLang;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(mButtonArray[i][j].getText()==mVocabs[mPrefilledPuzzle[i][j]].getWord(mChosenLang)) {
                    mButtonArray[i][j].setText(mVocabs[mPrefilledPuzzle[i][j]].getWord(mPuzzleLang));
                }
                else{
                    mButtonArray[i][j].setText(mVocabs[mPrefilledPuzzle[i][j]].getWord(mChosenLang));//write word on the button at position(i,j) from vocabs in "initial" language used for the puzzle
                }
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
            mCurrentPuzzle[row][col] = selectedInd;
            mFilledPuzzle[row][col] = selectedInd;
            mButtonArray[row][col].setText(mVocabs[selectedInd].getWord(mChosenLang)); //TODO: refactor
        }
        return;
    }

    public void setSelected(int wordInd) {
        selectedInd = wordInd;
    }

    public boolean isBoxEmpty(int row, int col) {//check if (row,col) box is blank
        if (mCurrentPuzzle[row][col] == 0) return true;
        else return false;
    }


    // For testing only
    public void genFullPuzzle(Vocab[] vocabs, int initLang, TableLayout table) {//gen test puzzle
        for (int i = 0; i < 9; i++) {
            TableRow mTblRow = (TableRow) table.getChildAt(i); //get table row element
            for (int j = 0; j < 9; j++) {
                Button mButton = (Button) mTblRow.getChildAt(j); //get button view
                mButton.setText(vocabs[mCurrentPuzzle[i][j]].getWord(initLang)); //write word on the button at position(i,j) from vocabs in "initial" language used for the puzzle
            }
        }
    }

    // TODO: Can be more efficient? (positions generated might overlap)
    public void genRandomPuzzle(Vocab[] vocabs, int initLang) {//generate puzzle from random difficulty with bounds defined in this class
        Random r = new Random();
        // pick random difficulty(relating the # of blank cells)
        int difficulty = r.nextInt(max-min) + min;

        // pick random positions
        mRandomPositions = new int[difficulty][2];
        for (int i = 0; i < difficulty; i++) {
            int x = mRandomPositions[i][0] = r.nextInt(9);
            int y = mRandomPositions[i][1] = r.nextInt(9);

            mPrefilledPuzzle[x][y] = 0;
            mCurrentPuzzle[x][y] = 0;
            Button button = mButtonArray[x][y];

            button.setText(vocabs[mPrefilledPuzzle[x][y]].getWord(initLang));
            button.setTextColor(Color.BLUE);

            //set empty cell to be clickable
            button.setClickable(true);
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
            mRow[i] = Integer.valueOf(mCurrentPuzzle[row][i]);
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
            mCol[i] = Integer.valueOf(mCurrentPuzzle[i][col]);
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
                mSub[i * 3 + j] = Integer.valueOf(mCurrentPuzzle[(sub / 3) * 3 + i][(sub % 3) * 3 + j]);
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


