package eta.sudoku;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class PuzzleActivity extends AppCompatActivity {

    private static final String TAG = "PuzzleActivity";
    private static final String KEY_LANG_INDEX = "langIndex";
    private static final String KEY_SEL_LANG_INDEX = "selLangIndex";
    private static final String KEY_PUZZLE = "testPuzzle";
    private static final String KEY_IS_COMP = "isCompMode";
    // 1 indexed
    private Button[] selectionButtons;
    // 0 or 1 to select language for selection Buttons, board language will be opposite
    private int langIndex = 0;
    private int selLangIndex = 1;
    private boolean onStartFlag = false;
    private boolean isLandscape; //useful?
    private boolean isCompMode = false;
    private int lastInsert[][] = new int[100][100];
    private int count = 0;
    private int puzzleSize;//4,6,9,12 for sudoku size
    private int puzzleDiffculty;//0,1,2
    // if you get at least 5 wrong, word is difficult for you
    private static final int maxError=5;
    private int[] incorrectCount;



    //Test variables for puzzle.java and vocab.java

    private VocabLibrary mVocabs = SudokuApplication.getInstance().getSelectedVocabs();
    private Button[][] mButtonArray;
    private TextView[][] mCells;

    private int[][] mPuzzle;
    private Puzzle mTestPuzzle;
    //Test variables end
    public transient Context ctx = this; //for testing with TOAST

    public Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        puzzleSize = getIntent().getIntExtra(SelectorActivity.EXTRA_SUDOKU_SIZE,9);
        mPuzzle = SudokuApplication.getInstance().getPuzzle(puzzleSize);
        puzzleDiffculty = getIntent().getIntExtra(SelectorActivity.EXTRA_SUDOKU_DIFFICULTY,0);
        isCompMode = getIntent().getBooleanExtra(SelectorActivity.EXTRA_SUDOKU_IS_LISTEN, false);
        mButtonArray = new Button[puzzleSize][puzzleSize];
        mCells = new TextView[puzzleSize][puzzleSize];
        incorrectCount = new int[puzzleSize];
        selectionButtons = new Button[puzzleSize];
        mTestPuzzle = new Puzzle(mPuzzle, mVocabs, puzzleSize, puzzleDiffculty);


        if (savedInstanceState != null) {

            langIndex = savedInstanceState.getInt(KEY_LANG_INDEX);
            selLangIndex = savedInstanceState.getInt(KEY_SEL_LANG_INDEX);
            mTestPuzzle = (Puzzle) savedInstanceState.getSerializable(KEY_PUZZLE);
            isCompMode = savedInstanceState.getBoolean(KEY_IS_COMP);
        }
        /* //currently not useful
        Configuration config = new Configuration();
        if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
            isLandscape = false;
        }else if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            isLandscape = true;
        }*/



        final GridLayout puzzleBoardGrid = (GridLayout) findViewById(R.id.puzzle_board_grid);


        if (savedInstanceState == null) {//only generate random puzzle once

            mTestPuzzle.genRandomPuzzle();
        }
        GridLayout selectionLayout = (GridLayout) findViewById(R.id.puzzle_select_pad);
        ImageView background = (ImageView) findViewById(R.id.puzzle_board);
        //createButton(puzzleBoardGrid, selectionLayout, background);
        layoutSetup(puzzleBoardGrid, selectionLayout, background);
        // Set listeners for all buttons in selection then store in selectionButton[]




        Button mSubmitButton = (Button) findViewById(R.id.puzzle_Submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        Button mDeleteButton = (Button) findViewById(R.id.puzzle_Delete);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWord();
            }
        });

        Button mSwitchButton = (Button) findViewById(R.id.puzzle_SwitchLanguage);
        mSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLang();
            }
        });
        playSound();
/*
        final ImageButton mComprehensionButton = (ImageButton) findViewById(R.id.puzzle_Comprehension);
        if(isCompMode){
            mComprehensionButton.setImageResource(R.drawable.nosoundicon);
        }else{
            mComprehensionButton.setImageResource(R.drawable.soundicon);
        }
        mComprehensionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCompMode){
                    playSound();
                    mComprehensionButton.setImageResource(R.drawable.nosoundicon);
                    Toast.makeText(getBaseContext(), "Comprehension Mode On" , Toast.LENGTH_SHORT ).show();
                    isCompMode=true;
                }
                else{
                    mComprehensionButton.setImageResource(R.drawable.soundicon);
                    playSound();
                    Toast.makeText(getBaseContext(), "Comprehension Mode Off" , Toast.LENGTH_SHORT ).show();
                    isCompMode=false;
                }
            }

        });*/

        Button mMenuButton = (Button) findViewById(R.id.puzzle_menu);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PuzzleActivity.this)
                        .setMessage(R.string.menu_alert_message)
                        .setTitle(R.string.menu_alert_title)
                        .setPositiveButton(R.string.menu_alert_pos, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.menu_alert_neg, null)
                        .show();
            }
        });

    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(PuzzleActivity.this)
                .setMessage(R.string.menu_alert_message)
                .setTitle(R.string.menu_alert_title)
                .setPositiveButton(R.string.menu_alert_pos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SudokuApplication.getInstance().setSelectedVocabs(new VocabLibrary());
                        finish();
                    }
                })
                .setNegativeButton(R.string.menu_alert_neg, null)
                .show();


    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
        onStartFlag = true;

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
        onStartFlag = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstantState()");
        savedInstanceState.putInt(KEY_LANG_INDEX, langIndex);
        savedInstanceState.putInt(KEY_SEL_LANG_INDEX, selLangIndex);
        savedInstanceState.putSerializable(KEY_PUZZLE, mTestPuzzle);
        savedInstanceState.putBoolean(KEY_IS_COMP, isCompMode);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            isLandscape = false;
        }
    }

    public void setLastInsert(int row, int col) {
        if(lastInsert[0][0]==0){
            lastInsert[0][0]=row;
            lastInsert[0][1]=col;
            count++;
        }
        if(lastInsert[count][0]==0){
            lastInsert[count][0]=row;
            lastInsert[count][1]=col;
            count++;
        }
    }


    public void checkDuplicate(int row,int col) {
        boolean rowWrong = mTestPuzzle.isDuplicateInRow(row);
        boolean colWrong = mTestPuzzle.isDuplicateInCol(col);
        boolean subWrong;
        int r=0; int c=0;
        switch(mTestPuzzle.getSize()) {
            case 4:
            case 9: r =(int)Math.sqrt(mTestPuzzle.getSize());
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
        int sub = (row/r)*r + col/c;
        subWrong = mTestPuzzle.isDuplicateInSub(sub);
        String msg = "";

        if(mTestPuzzle.getCurrentCell(row,col) == 0){
            mButtonArray[row][col].setBackgroundColor(Color.alpha(0));
        }else {
            if(rowWrong || colWrong || subWrong){
                incorrectCount[mTestPuzzle.getFilledCell(row, col) - 1]++;
                mButtonArray[row][col].setBackgroundColor(Color.RED);
                if(rowWrong){
                    msg = "Row";
                    if(colWrong){
                        msg += " & column";
                    }
                    if(subWrong){
                        msg += " & sub-table";
                    }
                }else if(colWrong){
                    msg = "Column";
                    if(subWrong){
                        msg += " & sub-table";
                    }
                }else if(subWrong){
                    msg = "Sub-table";
                }

                Toast toast = Toast.makeText(getApplicationContext(),
                        msg + " is wrong", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
                toast.show();

                if(incorrectCount[mTestPuzzle.getFilledCell(row, col) - 1] == maxError){
                    //set and alert difficult only if the word is not difficult
                    if(!SudokuApplication.getInstance().isVocabDifficult(mVocabs.get(mTestPuzzle.getFilledCell(row, col)).getmIndex())) {
                        toast = Toast.makeText(getApplicationContext(),
                                mVocabs.get(mTestPuzzle.getFilledCell(row, col)).getWord(selLangIndex) + " seems difficult", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
                        toast.show();
                        SudokuApplication.getInstance().setVocabDifficult(mVocabs.get(mTestPuzzle.getFilledCell(row, col)).getmIndex());
                    }
                }
            } else {
                mButtonArray[row][col].setBackgroundColor(Color.alpha(0));
            }
        }


    }

    public void createButton(GridLayout grid, GridLayout selector, ImageView background) {
        //programmatically create buttons in the table(layout)
        Resources r = ctx.getResources();
        int[][] prefilledPuzzle = mTestPuzzle.getPrefilledPuzzle();
        int[][] filledPuzzle = mTestPuzzle.getFilledPuzzle();
        grid.setRowCount(puzzleSize);
        grid.setColumnCount(puzzleSize);
        //convert dp to pixel
        float mDp2Px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());


        //building puzzle board
        for (int i = 0; i < puzzleSize; i++) {

            for (int j = 0; j < puzzleSize; j++) {
                final int row = i;
                final int col = j;
                final Button mButton = new Button(ctx);

                if (prefilledPuzzle[i][j] == 0) {

                    mButton.setTextColor(Color.BLUE);
                    mButton.setText(mVocabs.get(filledPuzzle[i][j]).getWord(selLangIndex));
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setPosition(row, col);
                            checkDuplicate(row,col);
                        }
                    });


                } else if (prefilledPuzzle[i][j] > 0) {
                    if (filledPuzzle[i][j] == 0) {
                        mButton.setTextColor(Color.BLACK);
                        if(!isCompMode) {
                            mButton.setText(mVocabs.get(prefilledPuzzle[i][j]).getWord(langIndex));
                        }else{
                            mButton.setText(Integer.toString(mTestPuzzle.getPrefilledCell(i, j)));
                        }
                        mButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hint(row, col);
                            }
                        });
                    } else {
                        //error
                    }
                }

                grid.addView(mButton);

                //set adaptable width and height
                ViewGroup.LayoutParams mButtonLayoutParams = mButton.getLayoutParams();
                mButtonLayoutParams.height = (int) (0 * mDp2Px);
                mButtonLayoutParams.width = (int) (0 * mDp2Px);
                ((GridLayout.LayoutParams) mButton.getLayoutParams()).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                ((GridLayout.LayoutParams) mButton.getLayoutParams()).rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);


                mButton.setGravity(Gravity.CENTER);
                mButton.setShadowLayer(0, 0, 0, Color.alpha(0));


                mButton.setPadding(0, 0, 0, 0);

                //decapitalize button text
                mButton.setTransformationMethod(null);

                mButton.setBackgroundColor(Color.alpha(0));

                // Fit text in button properly
                // check if device is tablet (a tablet is defined to have a diagonal of 6.5 inches or more here)
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                float yInches= displayMetrics.heightPixels/displayMetrics.ydpi;
                float xInches= displayMetrics.widthPixels/displayMetrics.xdpi;
                double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
                // if not tablet
                if (diagonalInches < 6.5) {
                    mButton.setTextSize(5 * mDp2Px);
                }
                mButton.setSingleLine(true);

                // add button to array
                mButtonArray[i][j] = mButton;
            }
        }
        //create selection pad and background img for puzzle board
        switch (puzzleSize){
            case 4: selector.setRowCount(2);
                    selector.setColumnCount(2);
                    background.setImageResource(R.drawable.sudoku4x4);
                    break;
            case 6: selector.setRowCount(2);
                    selector.setColumnCount(3);
                    background.setImageResource(R.drawable.sudoku6x6);
                    break;
            case 9: selector.setRowCount(3);
                    selector.setColumnCount(3);
                    background.setImageResource(R.drawable.sudokuboard);
                    break;
            case 12: selector.setRowCount(3);
                    selector.setColumnCount(4);
                    background.setImageResource(R.drawable.sudoku12x12);
                    break;
            default: assert puzzleSize == 4 || puzzleSize == 6 || puzzleSize ==9 || puzzleSize ==12;
        }
        for(int i=0; i<puzzleSize; i++){
            final Button mSelButton = new Button(ctx);
            mSelButton.setText(mVocabs.get(i+1).getWord(selLangIndex));
            mSelButton.setTransformationMethod(null);
            mSelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    int pos = findIndex(selectionButtons, button);
                    mTestPuzzle.setSelected(pos);// set global variable selected Position for position for selection
                }
            });
            selectionButtons[i] = mSelButton;

            selector.addView(mSelButton);
        }
    }
    public void layoutSetup(GridLayout grid, GridLayout selector, ImageView background) {
        //programmatically create buttons in the table(layout)
        Resources r = ctx.getResources();
        int[][] prefilledPuzzle = mTestPuzzle.getPrefilledPuzzle();
        int[][] filledPuzzle = mTestPuzzle.getFilledPuzzle();
        grid.setRowCount(puzzleSize);
        grid.setColumnCount(puzzleSize);
        //convert dp to pixel
        float mDp2Px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());


        //building puzzle board
        for (int i = 0; i < puzzleSize; i++) {

            for (int j = 0; j < puzzleSize; j++) {
                final int row = i;
                final int col = j;
                final TextView mCell = new TextView(ctx);

                if (prefilledPuzzle[i][j] == 0) {

                    mCell.setTextColor(Color.BLUE);
                    mCell.setText(mVocabs.get(filledPuzzle[i][j]).getWord(selLangIndex));
                    mCell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setPosition(row, col);
                            checkDuplicate(row,col);
                        }
                    });


                } else if (prefilledPuzzle[i][j] > 0) {
                    if (filledPuzzle[i][j] == 0) {
                        mCell.setTextColor(Color.BLACK);
                        if(!isCompMode) {
                            mCell.setText(mVocabs.get(prefilledPuzzle[i][j]).getWord(langIndex));
                        }else{
                            mCell.setText(Integer.toString(mTestPuzzle.getPrefilledCell(i, j)));
                        }
                        mCell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hint(row, col);
                            }
                        });
                    } else {
                        //error
                    }
                }

                grid.addView(mCell);

                //set adaptable width and height
                ViewGroup.LayoutParams mButtonLayoutParams = mCell.getLayoutParams();
                mButtonLayoutParams.height = (int) (0 * mDp2Px);
                mButtonLayoutParams.width = (int) (0 * mDp2Px);
                ((GridLayout.LayoutParams) mCell.getLayoutParams()).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                ((GridLayout.LayoutParams) mCell.getLayoutParams()).rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);


                mCell.setGravity(Gravity.CENTER);
                mCell.setShadowLayer(0, 0, 0, Color.alpha(0));


                mCell.setPadding(0, 0, 0, 0);

                //decapitalize button text
                mCell.setTransformationMethod(null);

                mCell.setBackgroundColor(Color.alpha(0));

                // Fit text in button properly
                // check if device is tablet (a tablet is defined to have a diagonal of 6.5 inches or more here)
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                float yInches= displayMetrics.heightPixels/displayMetrics.ydpi;
                float xInches= displayMetrics.widthPixels/displayMetrics.xdpi;
                double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
                // if not tablet
                if (diagonalInches < 6.5) {
                    //mButton.setTextSize(5 * mDp2Px);
                }
                mCell.setSingleLine(true);

                // add button to array
                mCells[i][j] = mCell;
            }
        }
        //create selection pad and background img for puzzle board
        switch (puzzleSize){
            case 4: selector.setRowCount(2);
                selector.setColumnCount(2);
                background.setImageResource(R.drawable.sudoku4x4);
                break;
            case 6: selector.setRowCount(2);
                selector.setColumnCount(3);
                background.setImageResource(R.drawable.sudoku6x6);
                break;
            case 9: selector.setRowCount(3);
                selector.setColumnCount(3);
                background.setImageResource(R.drawable.sudokuboard);
                break;
            case 12: selector.setRowCount(3);
                selector.setColumnCount(4);
                background.setImageResource(R.drawable.sudoku12x12);
                break;
            default: assert puzzleSize == 4 || puzzleSize == 6 || puzzleSize ==9 || puzzleSize ==12;
        }
        for(int i=0; i<puzzleSize; i++){
            final Button mSelButton = new Button(ctx);
            mSelButton.setText(mVocabs.get(i+1).getWord(selLangIndex));
            mSelButton.setTransformationMethod(null);
            mSelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    int pos = findIndex(selectionButtons, button);
                    mTestPuzzle.setSelected(pos);// set global variable selected Position for position for selection
                }
            });
            selectionButtons[i] = mSelButton;

            selector.addView(mSelButton);
        }
    }


    public void hint(int row, int col) {
        int wordIndex = mTestPuzzle.getPrefilledCell(row, col);
        Toast.makeText(getContext(), mTestPuzzle.getVocab(wordIndex, selLangIndex), Toast.LENGTH_LONG).show();
    }

    public void setPosition(int row, int col) {
        mTestPuzzle.setPosition(mButtonArray, row, col);
    }

    public void submit() {
        if (mTestPuzzle.isSolved()) {
            Toast.makeText(ctx, "Sudoku solved!", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(ctx, "Incorrect", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteWord() {
        mTestPuzzle.setSelected(0);

    }

    private void switchLang() {
        TableLayout mSelectionLayout = (TableLayout) findViewById(R.id.puzzle_selectionTable);
        if (langIndex == 1) {
            langIndex = 0;
            selLangIndex = 1;
        } else {
            langIndex = 1;
            selLangIndex = 0;
        }
        mTestPuzzle.switchLang();
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                if(!isCompMode) {
                    if (mTestPuzzle.getPrefilledCell(i, j) == 0) {
                        if (mTestPuzzle.getFilledCell(i, j) > 0) {
                            mButtonArray[i][j].setTextColor(Color.BLUE);
                            mButtonArray[i][j].setText(mTestPuzzle.getVocab(mTestPuzzle.getFilledCell(i, j), selLangIndex));
                        } else {

                        }

                    } else if (mTestPuzzle.getPrefilledCell(i, j) > 0) {
                        if (mTestPuzzle.getFilledCell(i, j) == 0) {
                            mButtonArray[i][j].setTextColor(Color.BLACK);
                            mButtonArray[i][j].setText(mTestPuzzle.getVocab(mTestPuzzle.getPrefilledCell(i, j), langIndex));
                        } else {
                            //error
                        }
                    }
                }
            }
        }


        for(int i=0; i<puzzleSize; i++){
            selectionButtons[i].setText(mTestPuzzle.getVocab(i+1, selLangIndex));
        }
    }
    
    private void switchToNum(boolean isComp) {
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                if(mTestPuzzle.getPrefilledCell(i,j) > 0){
                    mCells[i][j].setTextColor(Color.BLACK);
                    if(isComp) {
                        mCells[i][j].setText(Integer.toString(mTestPuzzle.getPrefilledCell(i, j)));
                    }
                    else{
                        mCells[i][j].setText(mTestPuzzle.getVocab(mTestPuzzle.getPrefilledCell(i, j), langIndex));
                    }
                }
            }
        }
    }
    // Find index of 1d array
    private int findIndex(Button[] buttonArray, Button button) {
        for (int i = 0; i < puzzleSize; i++) {
            if (buttonArray[i] == button) {
                return i+1;
            }
        }
        Log.e(TAG, "Number not found in selection.");
        return 0;
    }

    //play sound on comprehension mode
    private void playSound() {
        switchToNum(isCompMode);
        for (int i = 0; i < puzzleSize; i++) {
            for (int j = 0; j < puzzleSize; j++) {
                final int word = mTestPuzzle.getPrefilledCell(i,j);
                final Vocab w = mVocabs.get(word);
                if(word != 0){
                    if(!isCompMode){
                        final int finalJ = j;
                        final int finalI = i;
                        mCells[i][j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hint(finalI, finalJ);
                            }
                        });
                    }else{
                        mCells[i][j].setOnClickListener(new View.OnClickListener() {
                            MediaPlayer mp = MediaPlayer.create(PuzzleActivity.this, w.getSoundFile());
                            public void onClick(View v) {
                                mp.start();
                            }
                        });
                    }
                }
            }
        }
    }


}
