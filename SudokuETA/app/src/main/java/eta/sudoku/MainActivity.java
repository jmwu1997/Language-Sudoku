package eta.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    // 1 indexed
    private Button[] selectionButtons = new Button[10];
    // 0 or 1 to select language for selection Buttons, board language will be opposite
    private int langIndex = 1;

    //Test variables for puzzle.java and vocab.java
    private String[][] mVocabLib = {
            {" "," "},
            {"1","a"},
            {"2","b"},
            {"3","c"},
            {"4","d"},
            {"5","e"},
            {"6","f"},
            {"7","g"},
            {"8","h"},
            {"9","i"},
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
    private Puzzle mTestPuzzle;
    //Test variables end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout tblLayout = (TableLayout)findViewById(R.id.boardTable);

        mTestPuzzle = new Puzzle(mPuzzle, mVocabs, tblLayout, this);

        // Set listeners for all buttons in selection then store in selectionButton[]
        // TODO: Bug: text set for button is always capitalized for some reason
        TableLayout selectionLayout = (TableLayout)findViewById(R.id.selectionTable);
        int counter = 1;
        for(int i=0;i<3; i++) {
            TableRow row = (TableRow) selectionLayout.getChildAt(i);
            for(int j=0;j<3;j++) {
                Button button = (Button) row.getChildAt(j);
                button.setText(mVocabs[counter].getWord(langIndex));
                selectionButtons[counter] = button;
                button.setTransformationMethod(null);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button button = (Button) v;
                        int pos = findIndex(selectionButtons, button);
                        selectNumber(pos);
                    }
                });
                counter++;
            }
        }

        generatePuzzle();
        //test vocab and puzzle ends here
    }

    // set global variable selected Position for position for selection
    private void selectNumber(int pos) {
        mTestPuzzle.setSelected((String) selectionButtons[pos].getText());
        //Log.d(TAG, "selectNumber() with selected: " + selected + " called");
    }


    // Find index of 1d array
    private int findIndex(Button[] buttonArray, Button button) {
        for (int i = 0 ; i < 10; i++) {
            if (buttonArray[i] == button) {
                return i;
            }
        }
        Log.e(TAG, "Number not found in selection.");
        return 0;
    }


    public void generatePuzzle(){

        //getting buttons from layout and set words
        TableLayout mTableLayout = (TableLayout)findViewById(R.id.boardTable);

        // TODO: Disable ability to edit chosen random positions
        // flip chosen language to be different from selection buttons
        int chosenLang;
        if (langIndex == 0) {
            chosenLang = 1;
        }
        else {
            chosenLang = 0;
        }
        int[][] randomPositions = mTestPuzzle.genRandomPuzzle(mVocabs, chosenLang, mTableLayout);

        // isSolved() test
//        mTestPuzzle.genFullPuzzle(mVocabs, chosenLang, mTableLayout);
//        Log.d(TAG, "Result: " + mTestPuzzle.isSolved() );

        // TODO: add check mTestPuzzle.isSolved() when user is done with puzzle
    }
}
