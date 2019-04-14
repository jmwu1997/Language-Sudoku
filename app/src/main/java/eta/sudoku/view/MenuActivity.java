package eta.sudoku.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import eta.sudoku.R;
import eta.sudoku.controller.GameController;
import eta.sudoku.controller.PuzzleController;
import eta.sudoku.controller.VocabLibraryController;
import eta.sudoku.model.Game;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    public static final String EXTRA_SUDOKU_SIZE = "eta.sudoku.sudoku_size";
    public static final String EXTRA_SUDOKU_DIFFICULTY = "eta.sudoku.sudoku_difficulty";
    public static final String EXTRA_SUDOKU_IS_LISTEN = "eta.sudoku.sudoku_isListen";


    private static final VocabLibraryController vocabLibController = VocabLibraryController.getInstance();
    private static final GameController gameController = GameController.getInstance();
    private static final PuzzleController puzzleController = PuzzleController.getInstance();
    private int size = 9;
    private int difficulty = 0;

    // 0 = easy challenge, 1 = medium challenge, 2 = hard challenge
    private int challengeDifficulty = 0;
    // Challenge mode time values (in seconds)
    int easyTime = 7200; // 2hrs
    int medTime = 3600; // 1hr
    int hardTime = 1800; // 30min

    // To keep dialog open after orientation change
    // NOTE: settings will reset
    boolean isSettingsOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if( savedInstanceState != null) {
            isSettingsOpen = savedInstanceState.getBoolean("isSettingsOpen");
        }

        //initialize full vocab lib
        vocabLibController.newFullVocabLib();

        //setting dialog pop up
        LayoutInflater li = LayoutInflater.from(this);
        final View prompt = li.inflate(R.layout.game_setting, null);

        final Switch listenSwitch = (Switch) prompt.findViewById(R.id.setting_listen);
        final Switch challengeSwitch = (Switch) prompt.findViewById(R.id.challenge_switch);

        //Settings pop up
        final AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(prompt);

        a
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            ((ViewGroup) prompt.getParent()).removeView(prompt);
                            isSettingsOpen = false;
                            dialog.cancel();
                            return true;
                        }
                        return false;
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // This is done in setOnCancelListener()
                                //((ViewGroup) prompt.getParent()).removeView(prompt);
                                isSettingsOpen = false;
                                dialog.cancel();

                            }
                        })
                .setPositiveButton("SELECT VOCABULARY",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((ViewGroup) prompt.getParent()).removeView(prompt);
                            isSettingsOpen = false;
                            Intent i = new Intent(MenuActivity.this, SelectorActivity.class);
                            vocabLibController.newGameVocabLib();
                            puzzleController.newPuzzle(size, difficulty);
                            gameController.newGame(puzzleController.getPuzzle(), listenSwitch.isChecked(), challengeSwitch.isChecked(), challengeDifficulty);
                            //i.putExtra(EXTRA_SUDOKU_SIZE, size);
                            //i.putExtra(EXTRA_SUDOKU_DIFFICULTY, difficulty);
                            //i.putExtra(EXTRA_SUDOKU_IS_LISTEN, listenSwitch.isChecked());
                            startActivity(i);
                        }
                    });

        // Fix crash when user taps outside of box
        a.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface a) {
                        ((ViewGroup) prompt.getParent()).removeView(prompt);
                        isSettingsOpen = false;
                        a.cancel();
                    }
                }
        );

        final TextView textSize = (TextView) prompt.findViewById(R.id.setting_size);
        final TextView textDiff = (TextView) prompt.findViewById(R.id.setting_difficulty);
        final TextView textChallenge = (TextView) prompt.findViewById(R.id.challenge_text);


        Button buttonSize9 = (Button) prompt.findViewById(R.id.setting_size_9);
        buttonSize9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize.setText("9x9");
                size = 9;
            }
        });
        Button buttonSize4 = (Button) prompt.findViewById(R.id.setting_size_4);
        buttonSize4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize.setText("4x4");
                size = 4;
            }
        });
        Button buttonSize6 = (Button) prompt.findViewById(R.id.setting_size_6);
        buttonSize6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize.setText("6x6");
                size = 6;
            }
        });
        Button buttonSize12 = (Button) prompt.findViewById(R.id.setting_size_12);
        buttonSize12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize.setText("12x12");
                size = 12;
            }
        });
        Button buttonDifEasy = (Button) prompt.findViewById(R.id.setting_dif_easy);
        buttonDifEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDiff.setText("EASY");
                difficulty = 0;
            }
        });
        Button buttonDifMed = (Button) prompt.findViewById(R.id.setting_dif_med);
        buttonDifMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDiff.setText("MEDIUM");
                difficulty = 1;
            }
        });
        Button buttonDifHard = (Button) prompt.findViewById(R.id.setting_dif_hard);
        buttonDifHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDiff.setText("HARD");
                difficulty = 2;
            }
        });

        Button buttonChallengeEasy = (Button) prompt.findViewById(R.id.setting_challenge_easy);
        buttonChallengeEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textChallenge.setText("2 hours to complete the puzzle.");
                challengeDifficulty = easyTime;
            }
        });
        Button buttonChallangeMed = (Button) prompt.findViewById(R.id.setting_challenge_med);
        buttonChallangeMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textChallenge.setText("1 hour to complete the puzzle.");
                challengeDifficulty = medTime;
            }
        });
        Button buttonChallengeHard = (Button) prompt.findViewById(R.id.setting_challenge_hard);
        buttonChallengeHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textChallenge.setText("Only 30 minutes to complete the puzzle!");
                challengeDifficulty = hardTime;
            }
        });

        // if orientation changed and settings dialog was opened, reopen it with default settings
        if(isSettingsOpen) {
            AlertDialog dialog = a.create();
            dialog.show();
        }
        //menu controller
        Button mGameStart = (Button) findViewById(R.id.menu_game_start);
        Button mVocab = (Button) findViewById(R.id.menu_vocab);


        mGameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = a.create();
                isSettingsOpen = true;
                dialog.show();

            }
        });

        mVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, VocabActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isSettingsOpen", isSettingsOpen);
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstantState()");

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i(TAG, "onStart()");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"onPause() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }
}
