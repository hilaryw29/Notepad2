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

    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.new_note_menu, menu);
        return true;
    }

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

    private void deleteNote() {
        if (loadedNote == null){
            finish();
        } else {

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

    private void saveNote (){
        Note note;

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


        if (Utilities.saveNote(this, note)){
            Toast.makeText(NewNoteActivity.this, "Note Saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(NewNoteActivity.this, "Error. Insufficient disk drive on device.", Toast.LENGTH_LONG).show();
        }

        finish();
    }

}
