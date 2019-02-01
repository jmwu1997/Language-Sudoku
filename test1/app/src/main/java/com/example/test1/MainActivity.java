package com.example.test1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {

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
    }
}
