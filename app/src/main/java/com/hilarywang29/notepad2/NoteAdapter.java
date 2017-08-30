package com.hilarywang29.notepad2;

/**
 * Created by Hilar on 2017-08-29.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import com.hilarywang29.notepad2.R;



public class NoteAdapter extends ArrayAdapter <Note> {

    public NoteAdapter(Context context, int resource, ArrayList<Note> notes) {
        super(context, resource, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_format,null);
        }

        Note note = getItem(position);

        if (note != null){
            TextView title = (TextView) convertView.findViewById(R.id.listNoteTitle);
            TextView date = (TextView) convertView.findViewById(R.id.listNoteDate);
            TextView content = (TextView) convertView.findViewById(R.id.listNoteContent);

            title.setText(note.getTitle());
            date.setText(note.getDateTimeFormatted(getContext()));
            content.setText(note.getContent());
        }

        return convertView;
    }
}
