package com.example.dell.pinotebook;

import android.content.Intent;
import android.support.transition.Transition;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class NoteActivity extends AppCompatActivity {
   public static String text="";
    public static EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        editText = (EditText)findViewById(R.id.editText);
        editText.setText(text);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
                MainActivity.noteList.add(editText.getText().toString());
                String x = editText.getText().toString();
                String line[] = x.split("\\r?\\.");

                if(MainActivity.editCheck==true){
                    int pos = MainActivity.pos;
                    String temp = MainActivity.arrayList.get(pos);
                    MainActivity.update(temp,editText.getText().toString(),line[0]);
                    MainActivity.editCheck = false;
                }
                if (MainActivity.clickCheck!=false){
                    MainActivity.insert(editText.getText().toString(), line[0]);
                }
                MainActivity.syncFromDatabase();






    }
}
