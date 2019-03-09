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
import android.widget.TableLayout;
import android.widget.TableRow;
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
    // 1 indexed
    private Button[] selectionButtons = new Button[10];
    // 0 or 1 to select language for selection Buttons, board language will be opposite
    private int langIndex = 0;
    private int selLangIndex = 1;
    private boolean onStartFlag = false;
    private boolean isLandscape; //useful?
    private boolean isCompMode = false;
    public int lastInsert[][] = new int[100][100];
    private int count = 0;
    // if you get at least 5 wrong, word is difficult for you
    private static final int maxError=5;
    private int[] incorrectCount = new int[9];



    //Test variables for puzzle.java and vocab.java
       //private String[][] mVocabLib = SudokuApplication.getInstance().getVocabList().getRandomVocabs(9);
    //String[] a = getResources().getStringArray(R.array.EngAlpha);
    private VocabLibrary mVocabs = SudokuApplication.getInstance().getVocabList().getRandomVocabs(9);
    private Button[][] mButtonArray = new Button[9][9];

    private int[][] mPuzzle = {
            {6, 8, 2, 9, 4, 7, 5, 1, 3},
            {3, 1, 4, 6, 2, 5, 7, 9, 8},
            {9, 7, 5, 8, 3, 1, 4, 6, 2},
            {2, 5, 7, 3, 8, 6, 9, 4, 1},
            {1, 4, 6, 7, 9, 2, 3, 8, 5},
            {8, 9, 3, 1, 5, 4, 6, 2, 7},
            {7, 6, 9, 2, 1, 3, 8, 5, 4},
            {4, 2, 8, 5, 7, 9, 1, 3, 6},
            {5, 3, 1, 4, 6, 8, 2, 7, 9}
    };
    private Puzzle mTestPuzzle = new Puzzle(mPuzzle, mVocabs);
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


        if (savedInstanceState != null) {

            langIndex = savedInstanceState.getInt(KEY_LANG_INDEX);
            selLangIndex = savedInstanceState.getInt(KEY_SEL_LANG_INDEX);
            mTestPuzzle = (Puzzle) savedInstanceState.getSerializable(KEY_PUZZLE);

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

        createButton(mTestPuzzle, puzzleBoardGrid, ctx);

        // Set listeners for all buttons in selection then store in selectionButton[]
        TableLayout selectionLayout = (TableLayout) findViewById(R.id.puzzle_selectionTable);
        int counter = 1;
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) selectionLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button button = (Button) row.getChildAt(j);
                button.setText(mVocabs.get(counter).getWord(selLangIndex));
                selectionButtons[counter] = button;
                button.setTransformationMethod(null);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        int pos = findIndex(selectionButtons, button);
                        mTestPuzzle.setSelected(pos);// set global variable selected Position for position for selection
                    }
                });
                counter++;
            }
        }


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

        final ImageButton mComprehensionButton = (ImageButton) findViewById(R.id.puzzle_Comprehension);
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

        });

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

    public void check(int row,int col) {
        int rowcount=0;
        int colcount=0;
        for(int i=0;i<9;i++) {
            if(mTestPuzzle.mCurrentPuzzle[i][col]==0) {
                colcount=1;
            }
            if(mTestPuzzle.mCurrentPuzzle[row][i]==0) {
                rowcount=1;
            }
        }
        if(!mTestPuzzle.isRowSolved(row)&rowcount==0&!mTestPuzzle.isColSolved(col)&colcount==0){
            incorrectCount[mTestPuzzle.getFilledCell(row,col)-1]++;
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Row and Col is wrong", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
            toast.show();
        }
        else if (!mTestPuzzle.isColSolved(col)&colcount==0) {
            incorrectCount[mTestPuzzle.getFilledCell(row,col)-1]++;
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Col is wrong", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
            toast.show();

        }
        else if (!mTestPuzzle.isRowSolved(row)&rowcount==0) {
            //mVocabs.get(mTestPuzzle.getFilledCell(row,col));
            incorrectCount[mTestPuzzle.getFilledCell(row,col)-1]++;
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Row is wrong", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
            toast.show();
        }

        for(int i=0; i<9; i++){
                if(incorrectCount[i] == maxError){
                    SudokuApplication.getInstance().setVocabDifficult(mVocabs.get(i).getmIndex());
                }
        }
    }


    public void createButton(Puzzle puzzle, GridLayout grid, final Context context) {
        //programmatically create buttons in the table(layout)
        Resources r = context.getResources();
        int[][] prefilledPuzzle = puzzle.getPrefilledPuzzle();
        int[][] filledPuzzle = puzzle.getFilledPuzzle();

        //convert dp to pixel
        float mDp2Px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());


        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {
                final int row = i;
                final int col = j;
                final Button mButton = new Button(context);

                if (prefilledPuzzle[i][j] == 0) {

                    mButton.setTextColor(Color.BLUE);
                    mButton.setText(mVocabs.get(filledPuzzle[i][j]).getWord(selLangIndex));
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setPosition(row, col);
                            check(row,col);
                        }
                    });


                } else if (prefilledPuzzle[i][j] > 0) {
                    if (filledPuzzle[i][j] == 0) {
                        mButton.setTextColor(Color.BLACK);
                        mButton.setText(mVocabs.get(prefilledPuzzle[i][j]).getWord(langIndex));
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
            Toast.makeText(ctx, "solved", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(ctx, "incorrect", Toast.LENGTH_LONG).show();
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
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

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

        for (int i = 0; i < 3; i++) {
            TableRow mTblRow = (TableRow) mSelectionLayout.getChildAt(i); //get table row element
            for (int j = 0; j < 3; j++) {
                Button mButton = (Button) mTblRow.getChildAt(j); //get button view
                mButton.setText(mTestPuzzle.getVocab(i * 3 + j + 1, selLangIndex)); //write word on the button at position(i,j) from vocabs in "initial" language used for the puzzle
            }
        }
    }

    // Find index of 1d array
    private int findIndex(Button[] buttonArray, Button button) {
        for (int i = 0; i < 10; i++) {
            if (buttonArray[i] == button) {
                return i;
            }
        }
        Log.e(TAG, "Number not found in selection.");
        return 0;
    }

    //play sound on comprehension mode
    private void playSound() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                final int word = mTestPuzzle.getPrefilledCell(i,j);
                final Vocab w = mVocabs.get(word);
                if(word != 0){
                    if(!isCompMode){
                        mButtonArray[i][j].setOnClickListener(new View.OnClickListener() {
                             MediaPlayer mp = MediaPlayer.create(PuzzleActivity.this, w.getSoundFile());
                             public void onClick(View v) {
                                 mp.start();
                             }
                        });
                    }
                    if(isCompMode){
                        final int finalJ = j;
                        final int finalI = i;
                        mButtonArray[i][j].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hint(finalI, finalJ);
                            }
                        });
                    }
                }
            }
        }
    }


}
