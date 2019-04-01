package eta.sudoku.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import eta.sudoku.R;
import eta.sudoku.SudokuApplication;
import eta.sudoku.controller.GameController;
import eta.sudoku.controller.PuzzleController;
import eta.sudoku.controller.VocabLibraryController;
import eta.sudoku.model.VocabLibrary;

public class SelectorActivity extends AppCompatActivity {
    private static final SelectorActivity ourInstance = new SelectorActivity();
    private static final PuzzleController puzzleController = PuzzleController.getInstance();
    private static final GameController gameController = GameController.getInstance();
    private static final VocabLibraryController vocabLibController = VocabLibraryController.getInstance();
    public static final String TAG = "SelectorActivity";
    private VocabLibrary mFullVocab;
    private boolean[] selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = (LinearLayout) findViewById(R.id.selector_Vocab_layout);

        mFullVocab = vocabLibController.getOverallVocabLib();
        selected = new boolean[vocabLibController.getOverallVocabLibSize()];
        Button start = (Button) findViewById(R.id.selector_start_game);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vocabLibController.getGameVocabListSize()<puzzleController.getSize()+1){
                    Toast.makeText(SelectorActivity.this, "Please select "+ Integer.toString(puzzleController.getSize()+1-vocabLibController.getGameVocabListSize()) + " more words", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(SelectorActivity.this, PuzzleActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        Button startRandom = (Button) findViewById(R.id.selector_start_random);
        startRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vocabLibController.setGameVocabs(mFullVocab.getRandomVocabs(puzzleController.getSize()));
                Intent i = new Intent(SelectorActivity.this, PuzzleActivity.class);
                startActivity(i);
                finish();
            }
        });

        //show all vocab from overall vocab library
        for(int i=1; i<mFullVocab.size(); i++) {
            final int ind = i;
            final LinearLayout wordLayout = new LinearLayout(SelectorActivity.this);
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

                        selected[ind] = false;
                        vocabLibController.removeGameVocab(mFullVocab.get(ind));
                        if(mFullVocab.get(ind).isDifficult()) {
                            wordLayout.setBackgroundColor(Color.YELLOW);

                        } else {
                            wordLayout.setBackgroundColor(Color.WHITE);
                        }
                    }else {
                        if(vocabLibController.getGameVocabListSize()<puzzleController.getSize()+1) {
                            selected[ind] = true;
                            vocabLibController.addGameVocab(mFullVocab.get(ind));
                            wordLayout.setBackgroundColor(Color.GREEN);
                        }else{
                            Toast.makeText(SelectorActivity.this, "You have already selected "+ Integer.toString(puzzleController.getSize())+" words", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

            ViewGroup.LayoutParams wordParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            TextView word0 = new TextView(SelectorActivity.this);
            word0.setText(mFullVocab.get(i).getWord(0));
            word0.setLayoutParams(wordParam);
            word0.setTextSize(23);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(30);
            word0.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word0);

            TextView word1 = new TextView(SelectorActivity.this);
            word1.setText(mFullVocab.get(i).getWord(1));
            word1.setLayoutParams(wordParam);
            word1.setTextSize(16);
            //ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(60);
            word1.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word1);
        }



    }
    @Override
    public void onBackPressed() {

        vocabLibController.newGameVocabLib();
        finish();
    }

}
