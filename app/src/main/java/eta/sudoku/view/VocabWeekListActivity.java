package eta.sudoku.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.TextView;

import eta.sudoku.R;
import eta.sudoku.SudokuApplication;
import eta.sudoku.controller.VocabLibraryController;
import eta.sudoku.model.VocabLibrary;

public class VocabWeekListActivity extends AppCompatActivity {

    private static final String TAG = "VocabWeekListActivity";
    private static final VocabLibraryController vocabLibController = VocabLibraryController.getInstance();
    //private VocabLibrary mVocabLibrary = SudokuApplication.getInstance().getVocabList();
    public static final String EXTRA_WEEK_NUM = "eta.sudoku.weekNum";
    private VocabLibrary mWeekVocab;
    private int weekNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_week_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);




        LinearLayout layout = (LinearLayout) findViewById(R.id.weekList_layout);

        //getting week number from caller
        weekNum = getIntent().getIntExtra(EXTRA_WEEK_NUM, 0);
        mWeekVocab = vocabLibController.getVocabWeek(weekNum);

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
                        //mVocabLibrary.get(mWeekVocab.get(ind).getIndex()).setDifficult(false);
                        vocabLibController.setVocabDifficult(mWeekVocab.get(ind).getIndex(), false);
                    }else {
                        wordLayout.setBackgroundColor(Color.YELLOW);
                        //mVocabLibrary.get(mWeekVocab.get(ind).getIndex()).setDifficult(true);
                        vocabLibController.setVocabDifficult(mWeekVocab.get(ind).getIndex(), true);
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
        LayoutInflater li = LayoutInflater.from(this);

        final View lib = li.inflate(R.layout.add_word_from_lib, null);
        LinearLayout layout = lib.findViewById(R.id.weekList_add_word_lib);


        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(lib);

        a
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            //((ViewGroup) lib.getParent()).removeView(lib);
                            dialog.cancel();
                            return true;
                        }
                        return false;
                    }
                });
        final AlertDialog dialog = a.create();



        for(int i=1; i<vocabLibController.getOverallVocabLibSize(); i++) {
            final int ind = i;
            final LinearLayout wordLayout = new LinearLayout(VocabWeekListActivity.this);
            LinearLayout.LayoutParams wordLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            wordLayoutParam.setMargins(30,20,30,15);
            if(vocabLibController.isVocabDifficult(ind)) {
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
                    vocabLibController.addVocabIntoWeek(week, vocabLibController.getOverallVocab(ind));
                    //refresh activity
                    //((ViewGroup) lib.getParent()).removeView(lib);
                    dialog.dismiss();
                    finish();
                    startActivity(getIntent());
                }
            });

            ViewGroup.LayoutParams wordParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            TextView word0 = new TextView(VocabWeekListActivity.this);
            word0.setText(vocabLibController.getOverallVocab(i,0));
            word0.setLayoutParams(wordParam);
            word0.setTextSize(23);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(wordParam);
            marginLayoutParams.setMarginStart(30);
            word0.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word0);

            TextView word1 = new TextView(VocabWeekListActivity.this);
            word1.setText(vocabLibController.getOverallVocab(i,1));
            word1.setLayoutParams(wordParam);
            word1.setTextSize(16);
            marginLayoutParams.setMarginStart(60);
            word1.setLayoutParams(marginLayoutParams);
            wordLayout.addView(word1);
        }



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
