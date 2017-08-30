package com.hilarywang29.notepad2;

/**
 * Created by Hilar on 2017-08-29.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.hilarywang29.notepad2.R;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mListNotes = (ListView) findViewById(R.id.mainNotesListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_setting) {
//            Toast.makeText(MainActivity.this, "New Settings Updated", Toast.LENGTH_LONG).show();
        if (item.getItemId() == R.id.action_addNote) {
            Intent intent = new Intent(this, NewNoteActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListNotes.setAdapter(null);

        ArrayList<Note> notes = Utilities.getAllSavedNotes(getApplicationContext());

        if (notes != null || notes.size() > 0) {
            final NoteAdapter na = new NoteAdapter(this, R.layout.item_format, notes);
            mListNotes.setAdapter(na);

            mListNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fileName = ((Note) mListNotes.getItemAtPosition(position)).getDateTime() + Utilities.FILE_EXTENSION;

                    Intent viewNote = new Intent(getApplicationContext(), NewNoteActivity.class);
                    viewNote.putExtra ("NOTE_FILE", fileName);
                    startActivity(viewNote);
                }
            });
        } else{
            Toast.makeText(MainActivity.this, "No Saved Notes Available", Toast.LENGTH_SHORT).show();
        }
    }
}

