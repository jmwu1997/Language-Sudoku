package eta.sudoku;

import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VocabWeekListActivity extends AppCompatActivity {
    int weekNum;
    private VocabLibrary mVocabLibrary = SudokuApplication.getInstance().getVocabList();
    public static final String EXTRA_WEEK_NUM = "eta.sudoku.weekNum";
    private VocabLibrary mWeekVocab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_week_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);




        LinearLayout layout = (LinearLayout) findViewById(R.id.weekList_layout);

        //getting week number from caller
        weekNum = getIntent().getIntExtra(EXTRA_WEEK_NUM, 0);
        mWeekVocab = SudokuApplication.getInstance().getVocabWeek(weekNum);


        //set action bar title
        //getActionBar().setTitle();
        getSupportActionBar().setTitle("Week #"+ Integer.toString(weekNum+1));

        for(int i=1; i<mWeekVocab.size(); i++) {
            final int ind = i;
            final LinearLayout wordLayout = new LinearLayout(VocabWeekListActivity.this);
            LinearLayout.LayoutParams wordLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            wordLayoutParam.setMargins(30,20,30,15);
            if(mWeekVocab.get(ind).isDifficult()) {
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
                    if(mWeekVocab.get(ind).isDifficult()) {
                        wordLayout.setBackgroundColor(Color.WHITE);
                        mVocabLibrary.get(mWeekVocab.get(ind).getIndex()).setDifficult(false);
                    }else {
                        wordLayout.setBackgroundColor(Color.YELLOW);
                        mVocabLibrary.get(mWeekVocab.get(ind).getIndex()).setDifficult(true);
                    }
                }
            });

            ViewGroup.LayoutParams wordParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            TextView word0 = new TextView(VocabWeekListActivity.this);
            word0.setText(mWeekVocab.get(i).getWord(0));
            word0.setLayoutParams(wordParam);
            word0.setTextSize(23);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(30);
            word0.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word0);

            TextView word1 = new TextView(VocabWeekListActivity.this);
            word1.setText(mWeekVocab.get(i).getWord(1));
            word1.setLayoutParams(wordParam);
            word1.setTextSize(16);
            //ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(60);
            word1.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word1);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_vocab_week_list, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void addWord(final int week){
        //final String[] newWords = new String[2];
        //final int libSize = mVocabLibrary.size();
        LayoutInflater li = LayoutInflater.from(this);

        View lib = li.inflate(R.layout.add_word_from_lib, null);
        LinearLayout layout = lib.findViewById(R.id.weekList_add_word_lib);
        for(int i=1; i<mVocabLibrary.size(); i++) {
            final int ind = i;
            final LinearLayout wordLayout = new LinearLayout(VocabWeekListActivity.this);
            LinearLayout.LayoutParams wordLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            wordLayoutParam.setMargins(30,20,30,15);
            if(mVocabLibrary.get(ind).isDifficult()) {
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
                    SudokuApplication.getInstance().addVocabIntoWeek(week, mVocabLibrary.get(ind));
                    //refresh activity
                    finish();
                    startActivity(getIntent());
                }
            });

            ViewGroup.LayoutParams wordParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            TextView word0 = new TextView(VocabWeekListActivity.this);
            word0.setText(mVocabLibrary.get(i).getWord(0));
            word0.setLayoutParams(wordParam);
            word0.setTextSize(23);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(30);
            word0.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word0);

            TextView word1 = new TextView(VocabWeekListActivity.this);
            word1.setText(mVocabLibrary.get(i).getWord(1));
            word1.setLayoutParams(wordParam);
            word1.setTextSize(16);
            //ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(60);
            word1.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word1);
        }

        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(lib);
        AlertDialog dialog = a.create();
        dialog.show();

        /*
        View prompt = li.inflate(R.layout.add_word_prompt, null);

        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(prompt);

        final EditText addEng = (EditText) prompt.findViewById(R.id.add_eng);
        final EditText addChn = (EditText) prompt.findViewById(R.id.add_chn);
        //add new word
        a
                .setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newWords[0] = addEng.getText().toString();
                                newWords[1] = addChn.getText().toString();
                                Vocab v = new Vocab(newWords);
                                v.setIndex(libSize);
                                mVocabLibrary.add(v);
                                SudokuApplication.getInstance().addVocabIntoWeek(week, v);
                                Toast.makeText(VocabWeekListActivity.this, "Vocabulary Added To Week Successfully!", Toast.LENGTH_LONG);
                                //Refresh Activity
                                finish();
                                startActivity(getIntent());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
        //add from library

        AlertDialog dialog = a.create();
        dialog.show();
*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.weekList_add_word:
                addWord(weekNum);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
