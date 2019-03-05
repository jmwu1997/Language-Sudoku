package eta.sudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        Button mGameStart = (Button) findViewById(R.id.menu_game_start);
        Button mVocab = (Button) findViewById(R.id.menu_vocab);


        mGameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, PuzzleActivity.class);
                startActivity(i);
            }
        });

    }
}
