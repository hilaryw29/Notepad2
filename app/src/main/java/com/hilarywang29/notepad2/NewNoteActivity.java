package com.hilarywang29.notepad2;

/**
 * Created by Hilar on 2017-08-29.
 */

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;
import com.hilarywang29.notepad2.R;


public class NewNoteActivity extends AppCompatActivity {

    // Assigns variable to information in the EditText and MultiAutoCompleteTextView in the activity_new_note layout
    private EditText mEditTitle;
    private MultiAutoCompleteTextView mEditContent;
    private String mNoteFileName;
    private Note loadedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        mEditTitle = (EditText) findViewById(R.id.editTitle);
        mEditContent = (MultiAutoCompleteTextView) findViewById(R.id.editContent);

        mNoteFileName = getIntent().getStringExtra("NOTE_FILE");

        if (mNoteFileName != null && !mNoteFileName.isEmpty()){
            loadedNote = Utilities.getNoteByName(this, mNoteFileName);

            if (loadedNote!= null){
                mEditTitle.setText(loadedNote.getTitle());
                mEditContent.setText(loadedNote.getContent());

            }
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    // Creates menu in NewNoteActivity
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.new_note_menu, menu);
        return true;
    }

    // Depending on the icon the user clicks on in the toolbar, the respective functions to...
    // either save or delete the note is called.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_saveNote){
            saveNote();
            return true;
        }
        else if (item.getItemId() == R.id.action_deleteNote){
            deleteNote();
        }
        return true;
    }

    // Deletes Note
    private void deleteNote() {
        if (loadedNote == null){
            finish();
        } else {
            // Prompts user to confirm whether or not they wish to delete the note. If true, the note is deleted.
            AlertDialog.Builder dialog = new AlertDialog.Builder (this)
                    .setTitle("Delete Note")
                    .setMessage("Do you wish to delete \"" + mEditTitle.getText().toString() + "\"?")
                    .setNegativeButton("NO", null)
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilities.deleteNote(getApplicationContext(), loadedNote.getDateTime() + Utilities.FILE_EXTENSION);
                            Toast.makeText(NewNoteActivity.this, "Note has been deleted", Toast.LENGTH_LONG).show();
                            finish();
                        }

                    });
            dialog.show();

            Utilities.deleteNote (getApplicationContext(), loadedNote.getDateTime() + Utilities.FILE_EXTENSION);
        }
    }

    // Saves note
    private void saveNote (){
        Note note;

        // Checks to make sure that the title and content of the note are not both left blank.
        // If both fields are empty, the user is unable to save the note and is prompted to fill ...
        // In at least one of the fields in order to save.
        if (mEditTitle.getText().toString().isEmpty()&& mEditContent.getText().toString().isEmpty()){
            Toast.makeText(this, "Title and content cannot both be left blank", Toast.LENGTH_LONG).show();
            return;
        }

        if (loadedNote == null) {
            note = new Note(System.currentTimeMillis(), mEditTitle.getText().toString(),
                    mEditContent.getText().toString());
        } else {
            note = new Note(loadedNote.getDateTime(), mEditTitle.getText().toString(),
                    mEditContent.getText().toString());
        }

        // If Utilities.saveNote returns true, meaning the note was saved successfully, a toast is created...
        // to inform the user. If an error occured, a toast is created notifying the user of the error ...
        // likely caused by insufficient disk drive on the device.
        if (Utilities.saveNote(this, note)){
            Toast.makeText(NewNoteActivity.this, "Note Saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(NewNoteActivity.this, "Error. Insufficient disk drive on device.", Toast.LENGTH_LONG).show();
        }

        finish();
    }

}
