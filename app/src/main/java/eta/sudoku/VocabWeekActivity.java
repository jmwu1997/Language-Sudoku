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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class VocabWeekActivity extends AppCompatActivity {
    private ArrayList<VocabLibrary> mAllWeekVocab = SudokuApplication.getInstance().getAllWeekVocab();
    private int numWeeks = SudokuApplication.getInstance().getTotalWeek();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_week);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = (LinearLayout) findViewById(R.id.weekVocab_layout);
        for(int i=0; i<numWeeks; i++){
            final int ind = i;
            final LinearLayout weekLayout = new LinearLayout(VocabWeekActivity.this);
            LinearLayout.LayoutParams wordLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            wordLayoutParam.setMargins(30,20,30,15);
            weekLayout.setBackgroundColor(Color.WHITE);
            weekLayout.setOrientation(LinearLayout.VERTICAL);
            weekLayout.setOutlineProvider(ViewOutlineProvider.BOUNDS);
            weekLayout.setLayoutParams(wordLayoutParam);
            weekLayout.setElevation(4);
            layout.addView(weekLayout);
            weekLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(VocabWeekActivity.this, VocabWeekListActivity.class);
                    startActivity(i);
                    i.putExtra(VocabWeekListActivity.EXTRA_WEEK_NUM, ind);
                }
            });



            TextView weekNum = new TextView(VocabWeekActivity.this);
            weekNum.setText("Week #"+Integer.toString(i+1));
            weekNum.setGravity(Gravity.CENTER);
            weekNum.setTextSize(23);
            weekLayout.addView(weekNum);
        }

        //add new week
        LinearLayout addWeekLayout = new LinearLayout(VocabWeekActivity.this);
        LinearLayout.LayoutParams wordLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wordLayoutParam.setMargins(30,20,30,15);
        addWeekLayout.setBackgroundColor(Color.WHITE);
        addWeekLayout.setOrientation(LinearLayout.VERTICAL);
        addWeekLayout.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        addWeekLayout.setLayoutParams(wordLayoutParam);
        addWeekLayout.setElevation(4);
        layout.addView(addWeekLayout);
        addWeekLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeek();
            }
        });
    }
    public void addWeek(){

        new AlertDialog.Builder(this)
                .setMessage("Add a New Week Vocabulary List?")
                .setTitle("Add Week")
                .setPositiveButton(R.string.menu_alert_pos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAllWeekVocab.add(new VocabLibrary());
                        //refresh activity
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton(R.string.menu_alert_neg, null)
                .show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_vocab_week, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.week_add_week:
                addWeek();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
