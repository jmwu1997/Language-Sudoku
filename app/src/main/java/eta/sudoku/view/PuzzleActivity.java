package eta.sudoku.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.BatchUpdateException;
import java.util.Stack;

import eta.sudoku.R;
import eta.sudoku.SudokuApplication;
import eta.sudoku.controller.GameController;
import eta.sudoku.controller.PuzzleController;
import eta.sudoku.controller.VocabLibraryController;
import eta.sudoku.model.Game;
import eta.sudoku.model.Puzzle;
import eta.sudoku.model.PuzzleInput;
import eta.sudoku.model.Vocab;
import eta.sudoku.model.VocabLibrary;

import static android.support.v4.view.ViewCompat.getBackgroundTintList;


public class PuzzleActivity extends AppCompatActivity {

    private static final String TAG = "PuzzleActivity";


    private static final PuzzleController puzzleController= PuzzleController.getInstance();
    private static final VocabLibraryController vocabLibController = VocabLibraryController.getInstance();
    private static final GameController gameController = GameController.getInstance();
    // 1 indexed
    private Button[] selectionButtons;
    // 0 or 1 to select language for selection Buttons, board language will be opposite

    private boolean onStartFlag = false;
    private boolean isLandscape; //useful?

    private int lastInsert[][] = new int[100][100];
    private int count = 0;

    // if you get at least 5 wrong, word is difficult for you
    private static final int maxError=5;
    private Toast mToast ;

    // for keeping track of time
    Thread timerThread;
    int challengeTime = 0;
    int secondsElapsed = 0;
    int hr = 0;
    int min = 0;
    int sec = 0;

    private TextView[][] mCells;

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

        if( savedInstanceState != null) {
            secondsElapsed = savedInstanceState.getInt("timeElapsed");
        }

        mCells = new TextView[puzzleController.getSize()][puzzleController.getSize()];

        selectionButtons = new Button[puzzleController.getSize()];
        mToast = new Toast(this);
        /* //currently not useful
        Configuration config = new Configuration();
        if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
            isLandscape = false;
        }else if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            isLandscape = true;
        }*/



        final GridLayout puzzleBoardGrid = (GridLayout) findViewById(R.id.puzzle_board_grid);


        GridLayout selectionLayout = (GridLayout) findViewById(R.id.puzzle_select_pad);
        ImageView background = (ImageView) findViewById(R.id.puzzle_board);
        //createButton(puzzleBoardGrid, selectionLayout, background);
        layoutSetup(puzzleBoardGrid, selectionLayout, background);
        // Set listeners for all buttons in selection then store in selectionButton[]


        Button mUndoButton = (Button) findViewById(R.id.puzzle_Undo);
        mUndoButton.setVisibility(View.INVISIBLE);
        mUndoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });
        Button mRedoButton = (Button) findViewById(R.id.puzzle_Redo);
        mRedoButton.setVisibility(View.INVISIBLE);
        mRedoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redo();
            }
        });

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
        if(!gameController.isListenMode()) {
            mSwitchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchLang();
                }
            });
            if (gameController.isListenMode()) {
                playSound();
            }
        }else{
            mSwitchButton.setVisibility(View.INVISIBLE);
        }


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

        // Keep track of time and update timeText
        final TextView timerText = findViewById(R.id.timerText);
        timerThread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!timerThread.isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String secString;
                                String minString;
                                String hrString;
                                if (gameController.isChallenge()) {
                                    secondsElapsed++;
                                    if((gameController.getChallengeDifficulty() - secondsElapsed) == 0) {
                                        showChallengeLoseDialog();
                                        timerThread.interrupt();
                                    }
                                    sec = (gameController.getChallengeDifficulty() - secondsElapsed) % 60;
                                    min = (gameController.getChallengeDifficulty() - secondsElapsed) / 60;
                                    hr = min / 60;
                                }
                                else {
                                    secondsElapsed++;
                                    sec = secondsElapsed % 60;
                                    min = secondsElapsed / 60;
                                    hr = min / 60;
                                }

                                if (sec < 10) {
                                    secString = "0" + Integer.toString(sec);
                                }
                                else {
                                    secString = Integer.toString(sec);
                                }
                                if (min < 10) {
                                    minString = "0" + Integer.toString(min);
                                }
                                else {
                                    minString = Integer.toString(min);
                                }
                                if (hr < 10) {
                                    hrString = "0" + Integer.toString(hr);
                                }
                                else {
                                    hrString = Integer.toString(hr);
                                }
                                String time = hrString + ":" + minString + ":" + secString;
                                timerText.setText(time);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        timerThread.start();
    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(PuzzleActivity.this)
                .setMessage(R.string.menu_alert_message)
                .setTitle(R.string.menu_alert_title)
                .setPositiveButton(R.string.menu_alert_pos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //SudokuApplication.getInstance().setSelectedVocabs(new VocabLibrary());
                        vocabLibController.newGameVocabLib();
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
        savedInstanceState.putInt("timeElapsed", secondsElapsed);
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstantState()");

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
        boolean[] isDuplicate = gameController.checkDuplicate(row, col);
        String msg = "";

        if(puzzleController.getCurrentCell(row,col) == 0){
            mCells[row][col].setBackgroundColor(Color.alpha(0));
        }else {
            if(isDuplicate[0] || isDuplicate[1] || isDuplicate[2]){
                //incorrectCount[mTestPuzzle.getFilledCell(row, col) - 1]++;
                gameController.incorrectInc(row, col);
                mCells[row][col].setBackgroundColor(Color.RED);
                if(isDuplicate[0]){
                    msg = "Row";
                    if(isDuplicate[1]){
                        msg += " & column";
                    }
                    if(isDuplicate[2]){
                        msg += " & sub-table";
                    }
                }else if(isDuplicate[1]){
                    msg = "Column";
                    if(isDuplicate[2]){
                        msg += " & sub-table";
                    }
                }else if(isDuplicate[2]){
                    msg = "Sub-table";
                }

                mToast.cancel();
                mToast = Toast.makeText(getApplicationContext(),
                        msg + " is wrong", Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);


                mToast.show();



                if(gameController.getIncorrectCount(row,col) == maxError){
                    //set and alert difficult only if the word is not difficult
                    //if(!SudokuApplication.getInstance().isVocabDifficult(vocabLibController.getGameVocabIndex(puzzleController.getFilledCell(row,col)))){
                    if(!vocabLibController.isVocabDifficult(vocabLibController.getGameVocabIndex(puzzleController.getFilledCell(row,col)))){
                        mToast.cancel();
                        mToast = Toast.makeText(ctx,
                                vocabLibController.getGameVocab(puzzleController.getFilledCell(row,col), gameController.getSelectLang()) + " seems difficult", Toast.LENGTH_LONG);
                        mToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
                        mToast.show();
                        vocabLibController.setVocabDifficult(vocabLibController.getGameVocabIndex(puzzleController.getFilledCell(row,col)), true);
                    }
                }
            } else {
                mCells[row][col].setBackgroundColor(Color.alpha(0));
            }
        }


    }
    public void checkDuplicateWithoutWarning(int row,int col) {
        boolean[] isDuplicate = gameController.checkDuplicate(row, col);


        if(puzzleController.getCurrentCell(row,col) == 0){
            mCells[row][col].setBackgroundColor(Color.alpha(0));
        }else {
            if(isDuplicate[0] || isDuplicate[1] || isDuplicate[2]){
                mCells[row][col].setBackgroundColor(Color.RED);

            } else {
                mCells[row][col].setBackgroundColor(Color.alpha(0));
            }
        }


    }

    public void layoutSetup(GridLayout grid, GridLayout selector, ImageView background) {
        //programmatically create buttons in the table(layout)
        Resources r = ctx.getResources();
        int[][] prefilledPuzzle = puzzleController.getPrefilledPuzzle();
        int[][] filledPuzzle = puzzleController.getFilledPuzzle();
        final int size = puzzleController.getSize();
        grid.setRowCount(size);
        grid.setColumnCount(size);
        //convert dp to pixel
        float mDp2Px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());


        //building puzzle board
        for (int i = 0; i < size; i++) {

            for (int j = 0; j < size; j++) {
                final int row = i;
                final int col = j;
                final TextView mCell = new TextView(ctx);
                // add button to array
                mCells[i][j] = mCell;
                mCell.setBackgroundColor(Color.alpha(0));
                if (prefilledPuzzle[i][j] == 0) {

                    mCell.setTextColor(Color.BLUE);
                    //mCell.setText(mVocabs.get(filledPuzzle[i][j]).getWord(selLangIndex));
                    if(filledPuzzle[i][j] > 0){
                        if(gameController.isDuplicate(i,j))
                            mCell.setBackgroundColor(Color.RED);
                    }
                    mCell.setText(vocabLibController.getGameVocab(filledPuzzle[i][j], gameController.getSelectLang()));
                    mCell.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fillPosition(row, col);
                            checkDuplicate(row,col);

                        }
                    });


                } else if (prefilledPuzzle[i][j] > 0) {
                    if (filledPuzzle[i][j] == 0) {
                        mCell.setTextColor(Color.BLACK);
                        if(!gameController.isListenMode()) {
                            mCell.setText(vocabLibController.getGameVocab(prefilledPuzzle[i][j], gameController.getPuzzleLang()));
                        }else{
                            mCell.setText(Integer.toString(prefilledPuzzle[i][j]));
                        }
                        mCell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showHint(row, col);

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



                // Fit text in button properly
                // check if device is tablet (a tablet is defined to have a diagonal of 6.5 inches or more here)
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                float yInches= displayMetrics.heightPixels/displayMetrics.ydpi;
                float xInches= displayMetrics.widthPixels/displayMetrics.xdpi;
                double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
                // if not tablet
                if (diagonalInches < 6.5) {
                    mCell.setTextSize(5 * mDp2Px);
                }
                mCell.setSingleLine(true);


            }
        }
        //create selection pad and background img for puzzle board
        switch (size){
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
            default: assert size == 4 || size == 6 || size ==9 || size ==12;
        }
        Button example = new Button(ctx);
        Button hl = new Button(ctx);
        final Drawable buttonBack = example.getBackground();
        ViewCompat.setBackgroundTintList(hl, ContextCompat.getColorStateList(getApplicationContext(), android.R.color.darker_gray));
        final Drawable buttonHl = hl.getBackground();
        for(int i=0; i<size; i++){
            final Button mSelButton = new Button(ctx);
            mSelButton.setText(vocabLibController.getGameVocab(i+1, gameController.getSelectLang()));
            if(gameController.getSelectedIndex() == i) mSelButton.setBackground(buttonHl);
            else mSelButton.setBackground(buttonBack);

            mSelButton.setTransformationMethod(null);
            mSelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    int pos = findIndex(selectionButtons, button);
                    gameController.setSelectedIndex(pos);
                    for(int i=0; i<size; i++){
                        selectionButtons[i].setBackground(buttonBack);
                    }
                    v.setBackground(buttonHl);

                }
            });
            selectionButtons[i] = mSelButton;
            selector.addView(mSelButton);
        }
    }

    public void redo(){
        if(gameController.isUndoHistoryEmpty()) undoActivate();
        int[] redo = gameController.redo();
        puzzleController.fillCell(redo[2], redo[0], redo[1]);
        mCells[redo[0]][redo[1]].setText(vocabLibController.getGameVocab(redo[2], gameController.getSelectLang()));
        checkDuplicateWithoutWarning(redo[0], redo[1]);
        if(gameController.isRedoHistoryEmpty()) redoDeactivate();
    }
    public void undo(){
        if(gameController.isRedoHistoryEmpty()) redoActivate();
        int[] undo = gameController.undo();
        puzzleController.fillCell(undo[2],undo[0],undo[1]);
        mCells[undo[0]][undo[1]].setText(vocabLibController.getGameVocab(undo[2], gameController.getSelectLang()));
        checkDuplicateWithoutWarning(undo[0], undo[1]);
        if(gameController.isUndoHistoryEmpty()) undoDeactivate();
    }
    public void fillPosition(int row, int col) {
        if(gameController.isUndoHistoryEmpty()) undoActivate();
        redoDeactivate();
        if(gameController.getSelectedIndex() != -1) {
            gameController.fillCell(row, col);
            mCells[row][col].setText(vocabLibController.getGameVocab(gameController.getSelectedIndex(), gameController.getSelectLang()));
        }

    }

    public void submit() {
        if (gameController.isSolved()) {
           showWinDialog();

        } else {
            mToast.cancel();
            mToast = Toast.makeText(ctx, "Incorrect", Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    private void deleteWord() {
        gameController.setSelectedIndex(0);

    }
    private void showHint(int row, int col){
        mToast.cancel();
        Toast.makeText(ctx, vocabLibController.getGameVocab(puzzleController.getPrefilledCell(row,col), gameController.getSelectLang()), Toast.LENGTH_LONG).show();
    }
    private void switchLang() {

        int size = puzzleController.getSize();
        gameController.swapLang();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(!gameController.isListenMode()) {
                    if(puzzleController.getPrefilledCell(i,j) == 0){
                        if(puzzleController.getFilledCell(i,j) > 0){
                            mCells[i][j].setTextColor(Color.BLUE);
                            mCells[i][j].setText(vocabLibController.getGameVocab(puzzleController.getFilledCell(i,j), gameController.getSelectLang()));
                        } else {

                        }

                    } else if (puzzleController.getPrefilledCell(i,j) > 0) {
                        if (puzzleController.getFilledCell(i,j) == 0) {
                            mCells[i][j].setTextColor(Color.BLACK);
                            mCells[i][j].setText(vocabLibController.getGameVocab(puzzleController.getPrefilledCell(i,j), gameController.getPuzzleLang()));
                        } else {
                            //error
                            //throw an exception here
                        }
                    }
                }
            }
        }


        for(int i=0; i<size; i++){
            selectionButtons[i].setText(vocabLibController.getGameVocab(i+1, gameController.getSelectLang()));
        }
    }
    
    private void switchToNum(){//boolean isComp) {
        for (int i = 0; i < puzzleController.getSize(); i++) {
            for (int j = 0; j < puzzleController.getSize(); j++) {
                if(puzzleController.getPrefilledCell(i,j) > 0){
                    mCells[i][j].setTextColor(Color.BLACK);
                    //if(isComp) {
                        mCells[i][j].setText(Integer.toString(puzzleController.getPrefilledCell(i,j)));
                    //}
                    //else{
                    //    mCells[i][j].setText(vocabLibController.getGameVocab(puzzleController.getPrefilledCell(i,j), gameController.getPuzzleLang()));
                    //}
                }
            }
        }
    }
    // Find index of 1d array
    private int findIndex(Button[] buttonArray, Button button) {
        for (int i = 0; i < puzzleController.getSize(); i++) {
            if (buttonArray[i] == button) {
                return i+1;
            }
        }
        Log.e(TAG, "Number not found in selection.");
        return 0;
    }

    //play sound on comprehension mode
    private void playSound() {
        //switchToNum(gameController.isListenMode());
        switchToNum();
        for (int i = 0; i < puzzleController.getSize(); i++) {
            for (int j = 0; j < puzzleController.getSize(); j++) {
                final int word = puzzleController.getPrefilledCell(i,j);
                if(word != 0){

                        mCells[i][j].setOnClickListener(new View.OnClickListener() {
                            MediaPlayer mp = MediaPlayer.create(PuzzleActivity.this, vocabLibController.getSoundFile(word));
                            public void onClick(View v) {
                                mp.start();
                            }
                        });
                    //}
                }
            }
        }
    }
    public void showWinDialog(){
        LayoutInflater li = LayoutInflater.from(this);
        View v = li.inflate(R.layout.game_win, null);
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(v);
        LinearLayout linearLayout = findViewById(R.id.puzzle_win);
        a
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog.cancel();
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
        final AlertDialog dialog = a.create();
        for(int i=1; i<vocabLibController.getGameVocabListSize(); i++) {
            if(vocabLibController.isVocabDifficult(vocabLibController.getGameVocabIndex(i))){
                TextView t = new TextView(this);
                t.setText(vocabLibController.getGameVocab(i,1));
                linearLayout.addView(t);
                vocabLibController.setVocabDifficult(vocabLibController.getGameVocabIndex(i), false);
            }
        }
        dialog.show();



        Button menu = v.findViewById(R.id.puzzle_win_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vocabLibController.newGameVocabLib();
                dialog.dismiss();
                finish();
            }
        });


    }

    public void showChallengeLoseDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View v = li.inflate(R.layout.game_win, null);
        AlertDialog.Builder a = new AlertDialog.Builder(this);

        TextView title = v.findViewById(R.id.endTitleText);
        TextView endMessage = v.findViewById(R.id.endMessageText);
        //TextView endWordDifficultyText = v.findViewById(R.id.endWordDifficultyText);

        a.setView(v);
        LinearLayout linearLayout = findViewById(R.id.puzzle_win);
        a
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog.cancel();
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
        final AlertDialog dialog = a.create();

        title.setText("Oh No!");
        endMessage.setText("You have run out of time.");
        for(int i=1; i<vocabLibController.getGameVocabListSize(); i++) {
            if(vocabLibController.isVocabDifficult(vocabLibController.getGameVocabIndex(i))){
                TextView t = new TextView(this);
                t.setText(vocabLibController.getGameVocab(i,1));
                linearLayout.addView(t);
                vocabLibController.setVocabDifficult(vocabLibController.getGameVocabIndex(i), false);
            }
        }
        dialog.show();

        Button menu = v.findViewById(R.id.puzzle_win_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vocabLibController.newGameVocabLib();
                dialog.dismiss();
                finish();
            }
        });
    }

    public void undoActivate(){
        Button undoButton = (Button) findViewById(R.id.puzzle_Undo);
        undoButton.setVisibility(View.VISIBLE);


    }
    public void undoDeactivate(){
        Button undoButton = (Button) findViewById(R.id.puzzle_Undo);
        undoButton.setVisibility(View.INVISIBLE);
    }
    public void redoActivate(){
        Button redoButton = (Button) findViewById(R.id.puzzle_Redo);
        redoButton.setVisibility(View.VISIBLE);
    }
    public void redoDeactivate(){
        Button redoButton = (Button) findViewById(R.id.puzzle_Redo);
        redoButton.setVisibility(View.INVISIBLE);
    }
    //for debugging
    /*
    public void showNumber(){
        Resources r = ctx.getResources();
        int[][] prefilledPuzzle = puzzleController.getPrefilledPuzzle();
        int[][] filledPuzzle = puzzleController.getFilledPuzzle();
        for (int i = 0; i < puzzleController.getSize(); i++) {

            for (int j = 0; j < puzzleController.getSize(); j++) {
                final int row = i;
                final int col = j;
                if (prefilledPuzzle[i][j] > 0) {
                    if (filledPuzzle[i][j] == 0) {
                        mCells[row][col].setTextColor(Color.BLACK);

                            mCells[row][col].setText(Integer.toString(prefilledPuzzle[i][j]));


                    } else {

                        //error
                    }
                } else if (prefilledPuzzle[i][j] == 0 ) {
                    mCells[row][col].setTextColor(Color.BLUE);
                    mCells[row][col].setText(Integer.toString(prefilledPuzzle[i][j]));
                }
                checkDuplicateWithoutWarning(i,j);//resets cells with red background
            }
        }
    }*/
}
