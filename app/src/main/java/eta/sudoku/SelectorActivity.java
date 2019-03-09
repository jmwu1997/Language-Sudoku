package eta.sudoku;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectorActivity extends AppCompatActivity {
    //private static final SelectorActivity ourInstance = new SelectorActivity();
    private VocabLibrary mFullVocab = SudokuApplication.getInstance().getVocabList();
    private boolean[] selected = new boolean[mFullVocab.size()];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = (LinearLayout) findViewById(R.id.selector_Vocab_layout);

        Button start = (Button) findViewById(R.id.selector_start_game);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SudokuApplication.getInstance().getSelectedVocabs().size()<10){
                    Toast.makeText(SelectorActivity.this, "Please select "+ Integer.toString(10-SudokuApplication.getInstance().getSelectedVocabs().size()) + " more words", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(SelectorActivity.this, PuzzleActivity.class);
                    startActivity(i);
                }
            }
        });
        Button startRandom = (Button) findViewById(R.id.selector_start_random);
        startRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SudokuApplication.getInstance().setSelectedVocabs(SudokuApplication.getInstance().getVocabList().getRandomVocabs(9));
                Intent i = new Intent(SelectorActivity.this, PuzzleActivity.class);
                startActivity(i);
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
    }
    @Override
    public void onBackPressed() {

        SudokuApplication.getInstance().setSelectedVocabs(new VocabLibrary());
        finish();
    }

}
