package com.hilarywang29.notepad2;

/**
 * Created by Hilar on 2017-08-29.
 */

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// Serializable saves notes to hard drive/storage
public class Note implements Serializable {

    // Initializes variables to store note creation time, title, and content
    private long mDateTime;
    private String mTitle;
    private String mContent;

    public Note (long dateTime, String title, String content){
        mContent = content;
        mDateTime = dateTime;
        mTitle = title;
    }

    // Gets note creation time, content, and title of note. Stores them inside respective variables
    public void setContent (String content){
        mContent = content;
    }

    public void setDateTime (long dateTime){
        mDateTime = dateTime;
    }

    public void setTitle (String title){
        mTitle = title;
    }

    public long getDateTime () {
        return mDateTime;
    }

    public String getContent (){
        return mContent;
    }

    public String getTitle () {
        return mTitle;
    }

    // Formats creation date and time of note
    public String getDateTimeFormatted (Context context){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(mDateTime));
    }

}
