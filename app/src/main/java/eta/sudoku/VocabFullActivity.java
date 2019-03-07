package eta.sudoku;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class VocabFullActivity extends AppCompatActivity {
    private VocabLibrary mFullVocab = SudokuApplication.getInstance().getVocabList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_vocab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = (LinearLayout) findViewById(R.id.fullVocab_layout);





        for(int i=1; i<mFullVocab.size(); i++) {
            final int ind = i;
            final LinearLayout wordLayout = new LinearLayout(VocabFullActivity.this);
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
                    if(mFullVocab.get(ind).isDifficult()) {
                        wordLayout.setBackgroundColor(Color.WHITE);
                        mFullVocab.get(ind).setDifficult(false);
                    }else {
                        wordLayout.setBackgroundColor(Color.YELLOW);
                        mFullVocab.get(ind).setDifficult(true);
                    }
                }
            });

            ViewGroup.LayoutParams wordParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            TextView word0 = new TextView(VocabFullActivity.this);
            word0.setText(mFullVocab.get(i).getWord(0));
            word0.setLayoutParams(wordParam);
            word0.setTextSize(23);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(30);
            word0.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word0);

            TextView word1 = new TextView(VocabFullActivity.this);
            word1.setText(mFullVocab.get(i).getWord(1));
            word1.setLayoutParams(wordParam);
            word1.setTextSize(16);
            //ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(60);
            word1.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word1);
        }

    }

    public void addWord(){
        final String[] newWords = new String[2];
        final int libSize = mFullVocab.size();
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.add_word_prompt, null);

        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(prompt);

        final EditText addEng = (EditText) prompt.findViewById(R.id.add_eng);
        final EditText addChn = (EditText) prompt.findViewById(R.id.add_chn);

        a
                .setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newWords[0] = addEng.getText().toString();
                                newWords[1] = addChn.getText().toString();
                                Vocab v = new Vocab(newWords);
                                v.setIndex(libSize);
                                mFullVocab.add(v);


                                //Refresh Activity
                                finish();
                                startActivity(getIntent());
                                Toast.makeText(VocabFullActivity.this, "Vocabulary Added To Week Successfully!", Toast.LENGTH_LONG);
                            }
                        })
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_full_vocab, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.fullVocab_add_word:
                addWord();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}