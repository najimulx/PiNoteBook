package com.example.dell.pinotebook;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<String> arrayList ;
    public static ArrayAdapter<String> arrayAdapter;
    public static ArrayList<String> noteList;
    ListView listView;
    public static SQLiteDatabase myDatabase;
    public static boolean clickCheck,editCheck;
    public static int pos;
    LinearLayout popUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase = this.openOrCreateDatabase("Notes",MODE_PRIVATE,null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS notes (full_note VARCHAR,note_title VARCHAR)");
        arrayList = new ArrayList<String>();
        noteList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        syncFromDatabase();
        popUp = (LinearLayout) findViewById(R.id.popView);

        clickCheck=true;








        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteActivity.text=noteList.get(position);
                Intent intent = new Intent(getApplicationContext(),NoteActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
                clickCheck = false;
                pos=position;
                editCheck = true;





            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are You Sure?")
                        .setMessage("Once Deleted Cannot Be Recovered")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete(arrayList.get(position));

                            }
                        }).setNegativeButton("No",null).show();


                return true;
            }
        });
    
    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addNote:
                Intent intent = new Intent(getApplicationContext(),NoteActivity.class);
                startActivity(intent);
                NoteActivity.text="";
                clickCheck = true;
                editCheck=false;
                return true;
            case R.id.exit:
                Intent intent2 = new Intent(getApplicationContext(),ExitSplashAcitvity.class);
                startActivity(intent2);
                return true;

            case R.id.about:
                popUp.setVisibility(VISIBLE);
                listView.setVisibility(INVISIBLE);

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),ExitSplashAcitvity.class);
        startActivity(intent);

    }

    public static void syncFromDatabase(){

        try {


            Cursor cursor = myDatabase.rawQuery("SELECT * FROM notes ", null);

            int full_note = cursor.getColumnIndex("full_note");
            int note_title = cursor.getColumnIndex("note_title");
            int id = cursor.getColumnIndex("id");
            int i=0;
            cursor.moveToFirst();
            arrayList.clear();
            noteList.clear();
            while (cursor != null) {

                arrayList.add(cursor.getString(note_title));
                noteList.add(cursor.getString(full_note));
                Log.i("Sync",cursor.getString(full_note)+"  "+cursor.getString(note_title));
                cursor.moveToNext();
            }



        }catch(Exception e){e.printStackTrace();}

        arrayAdapter.notifyDataSetChanged();

    }


    public static void insert( String full_note1,String note_title1){
       try {

           myDatabase.execSQL("INSERT INTO notes(full_note,note_title) VALUES ('"+full_note1+"','"+note_title1+"')");
       }catch(Exception e){e.printStackTrace();}
    }
    public static void delete(String title){
        try {

            myDatabase.execSQL("DELETE FROM notes WHERE note_title = '"+title+"'");

        }catch (Exception e){
            e.printStackTrace();
        }
        syncFromDatabase();
    }
    public static void update(String oldTitle,String fulledit,String newTitle){
        try{
            delete(oldTitle);
            insert(fulledit,newTitle);
            syncFromDatabase();
            editCheck = true;
        }catch (Exception e){
            e.printStackTrace();
            Log.i("Update","Failed update");
        }
    }

    public void popClose(View view){
        popUp.setVisibility(LinearLayout.INVISIBLE);
        listView.setVisibility(VISIBLE);
    }

}
