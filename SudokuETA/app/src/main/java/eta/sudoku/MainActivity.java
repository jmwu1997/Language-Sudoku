package eta.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //Test variables for puzzle.java and vocab.java
    private String[][] mVocabLib = {
            {" "," "},
            {"A","a"},
            {"B","b"},
            {"C","c"},
            {"D","d"},
            {"E","e"},
            {"F","f"},
            {"G","g"},
            {"H","h"},
            {"I","i"}
    };
    //String[] a = getResources().getStringArray(R.array.EngAlpha);
    private Vocab mVocabs[] = new Vocab[]{
            new Vocab(mVocabLib[0]),
            new Vocab(mVocabLib[1]),
            new Vocab(mVocabLib[2]),
            new Vocab(mVocabLib[3]),
            new Vocab(mVocabLib[4]),
            new Vocab(mVocabLib[5]),
            new Vocab(mVocabLib[6]),
            new Vocab(mVocabLib[7]),
            new Vocab(mVocabLib[8]),
            new Vocab(mVocabLib[9]),
    };
    private int[][] mPuzzle = {
            {6,8,2,9,4,7,5,1,3},
            {3,1,4,6,2,5,7,9,8},
            {9,7,5,8,3,1,4,6,2},
            {2,5,7,3,8,6,9,4,1},
            {1,4,6,7,9,2,3,8,5},
            {8,9,3,1,5,4,6,2,7},
            {7,6,9,2,1,3,8,5,4},
            {4,2,8,5,7,9,1,3,6},
            {5,3,1,4,6,8,2,7,9}
    };
    private Puzzle mTestPuzzle = new Puzzle(mPuzzle);
    //Test variables end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout tblLayout = (TableLayout)findViewById(R.id.tableLayout);

        for(int i=0;i<9;i++)
        {
            TableRow row = (TableRow)tblLayout.getChildAt(i);
            for(int j=0;j<9;j++){
                Button button = (Button)row.getChildAt(j); // get child index on particular row
                String buttonText = button.getText().toString();
                Log.i("Button index: "+(i+j), buttonText);
            }
        }

        MichaelTestProgram();
        //test vocab and puzzle ends here
    }




    public void MichaelTestProgram(){
        //test vocab and puzzle starts here
        //generate a random puzzle
        /*
        Random r = new Random();
        for(int i=0;i<9;i++){
            for(int j=0; j<9; j++){
                mTestPuzzle.putWord(r.nextInt(10),i,j);
            }
        }
        */

        //getting buttons from layout and set words
        TableLayout mTableLayout = (TableLayout)findViewById(R.id.tableLayout);
        mTestPuzzle.genPuzzle(mVocabs, 0, mTableLayout);

        System.out.println(mTestPuzzle.isSolved());
    }
}
