package eta.sudoku.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import eta.sudoku.R;
import eta.sudoku.controller.VocabLibraryController;
import eta.sudoku.model.VocabLibrary;
import eta.sudoku.model.VocabStorage;

public class VocabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab);


        Button mFullVocabButton = (Button) findViewById(R.id.vocab_full_vocab);
        Button mWeekVocabButton = (Button) findViewById(R.id.vocab_week_vocab);
        Button mNewListButton = (Button) findViewById(R.id.vocab_new_list);
        Button mImportButton = (Button) findViewById(R.id.vocab_import);

        mFullVocabButton.setText("View Current list");
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
                Intent i = new Intent(VocabActivity.this, SelectWordListActivity.class);
                startActivity(i);
            }
        });
        mNewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newList();
            }
        });
        mImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    storageController.deleteList("Library");
//                    storageController.deleteList("testing");
                Intent i = new Intent(VocabActivity.this, ImportListActivity.class);
                startActivityForResult(i, ImportListActivity.IMPORT_REQUEST_CODE);
            }
        });
    }

    public void newList() {
        final String listName = null;
        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.add_word_list, null);

        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setView(prompt);

        final EditText listname = (EditText) prompt.findViewById(R.id.add_name);

        a
                .setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                VocabLibrary newlist = new VocabLibrary();
                                VocabStorage.getInstance().saveList(newlist, listname.getText().toString());
                                VocabLibraryController.getInstance().setFullVocabLib( VocabStorage.getInstance().loadList(listname.getText().toString()));
                                VocabLibraryController.getInstance().setName(listname.getText().toString());

                                //Refresh Activity
                                finish();
                                startActivity(getIntent());
                                Toast.makeText(VocabActivity.this, "new list created", Toast.LENGTH_LONG);
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
}