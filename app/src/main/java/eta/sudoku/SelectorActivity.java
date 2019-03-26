package eta.sudoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SelectorActivity extends AppCompatActivity {
    //private static final SelectorActivity ourInstance = new SelectorActivity();
    public static final String TAG = "SelectorActivity";
    private VocabLibrary mFullVocab = SudokuApplication.getInstance().getVocabList();
    private boolean[] selected = new boolean[mFullVocab.size()];
    private int size = 9;
    private int difficulty = 0;
    private boolean isListen = false;
    public static final String EXTRA_SUDOKU_SIZE = "eta.sudoku.sudoku_size";
    public static final String EXTRA_SUDOKU_DIFFICULTY = "eta.sudoku.sudoku_difficulty";
    public static final String EXTRA_SUDOKU_IS_LISTEN = "eta.sudoku.sudoku_isListen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = (LinearLayout) findViewById(R.id.selector_Vocab_layout);
        //setting dialog pop up
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.game_setting, null);

        final Switch listenSwitch = (Switch) prompt.findViewById(R.id.setting_listen);


        final AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(prompt);
        a
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });


        Button start = (Button) findViewById(R.id.selector_start_game);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SudokuApplication.getInstance().getSelectedVocabs().size()<10){
                    Toast.makeText(SelectorActivity.this, "Please select "+ Integer.toString(10-SudokuApplication.getInstance().getSelectedVocabs().size()) + " more words", Toast.LENGTH_LONG).show();
                } else {
                    //pop up
                    a.setPositiveButton("SELECT VOCABULARY",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(SelectorActivity.this, PuzzleActivity.class);
                                    i.putExtra(EXTRA_SUDOKU_SIZE, size);
                                    i.putExtra(EXTRA_SUDOKU_DIFFICULTY, difficulty);
                                    i.putExtra(EXTRA_SUDOKU_IS_LISTEN, listenSwitch.isChecked());
                                    startActivity(i);
                                    finish();
                                }
                            });

                    AlertDialog dialog = a.create();
                    dialog.show();

                }
            }
        });
        Button startRandom = (Button) findViewById(R.id.selector_start_random);
        startRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pop up
                a.setPositiveButton("START",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SudokuApplication.getInstance().setSelectedVocabs(SudokuApplication.getInstance().getVocabList().getRandomVocabs(size));
                                Intent i = new Intent(SelectorActivity.this, PuzzleActivity.class);
                                i.putExtra(EXTRA_SUDOKU_SIZE, size);
                                i.putExtra(EXTRA_SUDOKU_DIFFICULTY, difficulty);
                                i.putExtra(EXTRA_SUDOKU_IS_LISTEN, listenSwitch.isChecked());
                                startActivity(i);
                                finish();
                            }
                        });
                AlertDialog dialog = a.create();
                dialog.show();

                //SudokuApplication.getInstance().setSelectedVocabs(SudokuApplication.getInstance().getVocabList().getRandomVocabs(9));
                //Intent i = new Intent(SelectorActivity.this, PuzzleActivity.class);

                //startActivity(i);
                //finish();
            }
        });


        for(int i=1; i<mFullVocab.size(); i++) {
            final int ind = i;
            final LinearLayout wordLayout = new LinearLayout(eta.sudoku.SelectorActivity.this);
            LinearLayout.LayoutParams wordLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            wordLayoutParam.setMargins(30,20,30,15);
            if(mFullVocab.get(ind).isDifficult()) {
                wordLayout.setBackgroundColor(Color.YELLOW);

            }else{
                wordLayout.setBackgroundColor(Color.WHITE);

            }
            wordLayout.setOrientation(LinearLayout.VERTICAL);
            wordLayout.setOutlineProvider(ViewOutlineProvider.BOUNDS);
            wordLayout.setLayoutParams(wordLayoutParam);
            wordLayout.setElevation(4);
            layout.addView(wordLayout);
            wordLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected[ind]){
                        //SudokuApplication.getInstance().setSelected(ind, false);
                        selected[ind] = false;
                        SudokuApplication.getInstance().getSelectedVocabs().remove(mFullVocab.get(ind));
                        if(mFullVocab.get(ind).isDifficult()) {
                            wordLayout.setBackgroundColor(Color.YELLOW);

                        } else {
                            wordLayout.setBackgroundColor(Color.WHITE);
                        }
                    }else {
                        if(SudokuApplication.getInstance().getSelectedVocabs().size()<10) {
                            //SudokuApplication.getInstance().setSelected(ind, true);
                            selected[ind] = true;
                            SudokuApplication.getInstance().getSelectedVocabs().add(mFullVocab.get(ind));
                            wordLayout.setBackgroundColor(Color.GREEN);
                        }else{
                            Toast.makeText(SelectorActivity.this, "You have already selected 9 words", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            ViewGroup.LayoutParams wordParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            TextView word0 = new TextView(eta.sudoku.SelectorActivity.this);
            word0.setText(mFullVocab.get(i).getWord(0));
            word0.setLayoutParams(wordParam);
            word0.setTextSize(23);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(30);
            word0.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word0);

            TextView word1 = new TextView(eta.sudoku.SelectorActivity.this);
            word1.setText(mFullVocab.get(i).getWord(1));
            word1.setLayoutParams(wordParam);
            word1.setTextSize(16);
            //ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(60);
            word1.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word1);
        }


        final TextView textSize = (TextView) prompt.findViewById(R.id.setting_size);
        final TextView textDiff = (TextView) prompt.findViewById(R.id.setting_difficulty);


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
    }
    @Override
    public void onBackPressed() {

        SudokuApplication.getInstance().setSelectedVocabs(new VocabLibrary());
        finish();
    }
    public void settings(){

        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.game_setting, null);

        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(prompt);


        a
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
        AlertDialog dialog = a.create();
        dialog.show();

    }
    public void settingsRandom(){
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.game_setting, null);

        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(prompt);
        a
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                        }
                        })
                .setPositiveButton("START",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SudokuApplication.getInstance().setSelectedVocabs(SudokuApplication.getInstance().getVocabList().getRandomVocabs(size));
                        Intent i = new Intent(SelectorActivity.this, PuzzleActivity.class);

                        startActivity(i);
                        finish();
                    }
                });
        AlertDialog dialog = a.create();
        dialog.show();
    }
    public void settingsSelected(){
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.game_setting, null);

        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(prompt);
        a
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("SELECT VOCABULARY",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(SelectorActivity.this, PuzzleActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });

    AlertDialog dialog = a.create();
        dialog.show();
    }
}
