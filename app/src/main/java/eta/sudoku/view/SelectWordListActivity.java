package eta.sudoku.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.TextView;

import eta.sudoku.R;
import eta.sudoku.controller.GameController;
import eta.sudoku.controller.PuzzleController;
import eta.sudoku.controller.VocabLibraryController;
import eta.sudoku.model.Vocab;
import eta.sudoku.model.VocabLibrary;
import eta.sudoku.model.VocabStorage;

public class SelectWordListActivity extends AppCompatActivity {
    private static final SelectWordListActivity ourInstance = new SelectWordListActivity();
    private static final PuzzleController puzzleController = PuzzleController.getInstance();
    private static final GameController gameController = GameController.getInstance();
    private static final VocabLibraryController vocabLibController = VocabLibraryController.getInstance();
    private static final VocabStorage storageController = VocabStorage.getInstance();
    public static final String TAG = "SelectorWordListActivity";
    private VocabLibrary mFullVocab;
    private static String[] wordlists;

    private int size = 9;
    private int difficulty = 0;
    private boolean isListen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(storageController.getLanguage()==null){
            startActivity(new Intent(SelectWordListActivity.this, SelectLanguageActivity.class));//GO TO MENU
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = (LinearLayout) findViewById(R.id.wordlist_layout);

        //size = getIntent().getIntExtra(MenuActivity.EXTRA_SUDOKU_SIZE,9);
        //difficulty = getIntent().getIntExtra(MenuActivity.EXTRA_SUDOKU_DIFFICULTY, 0);
        //isListen = getIntent().getBooleanExtra(MenuActivity.EXTRA_SUDOKU_IS_LISTEN, false);
        mFullVocab = vocabLibController.getOverallVocabLib();
        //show all vocab from overall vocab library
        wordlists = storageController.getWordLists();

        if(wordlists==null||wordlists.length<1){
            wordlists = storageController.getWordLists();
        }
        wordlists = storageController.getWordLists();
        if(wordlists!=null){
        for(int i=0;i<wordlists.length;i++) {
            Log.d("lang", wordlists[i]);
        }

        for(int i=0; i<wordlists.length; i++) {
            final int ind = i;
            final LinearLayout wordLayout = new LinearLayout(SelectWordListActivity.this);
            LinearLayout.LayoutParams wordLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            wordLayoutParam.setMargins(30, 20, 30, 15);

            wordLayout.setBackgroundColor(Color.WHITE);

            wordLayout.setOrientation(LinearLayout.VERTICAL);
            wordLayout.setOutlineProvider(ViewOutlineProvider.BOUNDS);
            wordLayout.setLayoutParams(wordLayoutParam);
            wordLayout.setElevation(4);
            layout.addView(wordLayout);

            ViewGroup.LayoutParams langParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView language = new TextView(SelectWordListActivity.this);
            language.setText(wordlists[ind]);
            language.setLayoutParams(langParam);
            language.setTextSize(23);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(langParam);
            marginLayoutParams.setMarginStart(30);
            language.setLayoutParams(marginLayoutParams);
            wordLayout.addView(language);
            wordLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VocabLibrary list = storageController.loadList(wordlists[ind]);
                    vocabLibController.setFullVocabLib(list);
                    startActivity(new Intent(SelectWordListActivity.this, MenuActivity.class));//GO TO MENU
                }
            });
        }




        }



    }
    @Override
    public void onBackPressed() {
        finish();
    }

}
