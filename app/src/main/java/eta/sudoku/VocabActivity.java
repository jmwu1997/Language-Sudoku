package eta.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VocabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab);


        Button mFullVocabButton = (Button) findViewById(R.id.vocab_full_vocab);
        Button mWeekVocabButton = (Button) findViewById(R.id.vocab_week_vocab);

        mFullVocabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VocabActivity.this, VocabFullActivity.class);
                startActivity(i);
            }
        });

        mWeekVocabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VocabActivity.this, VocabWeekActivity.class);
                startActivity(i);
            }
        });
    }
}