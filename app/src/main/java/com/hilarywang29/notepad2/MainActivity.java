package com.hilarywang29.notepad2;

/**
 * Created by Hilar on 2017-08-29.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private ListView mListNotes;

    // Boolean variables used to store user preferences for how to order their existing notes
    boolean alpha = false;
    boolean oldToNew = true;
    boolean newToOld = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mListNotes = (ListView) findViewById(R.id.mainNotesListView);
    }

    // Creates the menu in Main Activity
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

        // Checks if the icon to start a new note has been clicked. If yes, the user is taken ...
        // to the NewNoteActivity to create a new note.
        if (item.getItemId() == R.id.action_addNote) {
            Intent intent = new Intent(this, NewNoteActivity.class);
            startActivity(intent);
            return true;
        }
        // Checks if the icon to sort existing notes has been clicked. If yes, an AlertDialog ...
        // is created to get the user preferences for note order and sort existing notes accordingly
        else if (item.getItemId() == R.id.action_sortOrder){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sort Notes");
            builder.setMessage("How would you like to sort your notes?");
            builder.setPositiveButton("Alphabetically", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alpha = true;
                    newToOld = false;
                    oldToNew = false;
                    onResume();
                }
            });
            builder.setNeutralButton("Date Created (Old-New)", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alpha = false;
                    newToOld = false;
                    oldToNew = true;
                    onResume();
                }
            });
            builder.setNegativeButton("Date Created (New-Old)", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alpha = false;
                    newToOld = true;
                    oldToNew = false;
                    onResume();
                }
            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListNotes.setAdapter(null);

        // Gets the arraylist of notes taken from the utilities method
        ArrayList<Note> notes = Utilities.getAllSavedNotes(getApplicationContext());

        // Sorts the notes according to the order preference selected by the user
        if (newToOld == true) {
            Collections.sort(notes, new Comparator<Note>() {
                @Override
                public int compare(Note o1, Note o2) {
                    if (o1.getDateTime() > o2.getDateTime()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }
        else if (alpha == true) {
            Collections.sort(notes, new Comparator<Note>() {
                @Override
                public int compare(Note o1, Note o2) {
                    if (o1.getTitle().compareToIgnoreCase(o2.getTitle()) <= 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        } else if (oldToNew == true){
            Collections.sort(notes, new Comparator<Note>() {
                @Override
                public int compare(Note o1, Note o2) {
                    if (o1.getDateTime() < o2.getDateTime()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        }

        // If there are no notes in the Note arraylist, make toast informing the user that there are ...
        // no saved notes available.
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

