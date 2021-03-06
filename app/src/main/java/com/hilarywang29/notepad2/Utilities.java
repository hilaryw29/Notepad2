package com.hilarywang29.notepad2;

/**
 * Created by Hilar on 2017-08-29.
 */

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;



public class Utilities {
    public static final String FILE_EXTENSION = ".bin";

    public static boolean saveNote(Context context, Note note){

        // Saves notes with a file name composed of their date/time of creation and the file extension
        String fileName = String.valueOf(note.getDateTime()) + FILE_EXTENSION;

        FileOutputStream fos;
        ObjectOutputStream oos;

        // Creates a new file on the device and writes data from the note to the file created
        // If an error occurs, a StackTrace is printed and the user is notified
        try {
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(note);
            oos.close();
            fos.close();
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static ArrayList <Note> getAllSavedNotes (Context context){
        ArrayList <Note> notes = new ArrayList<>();

        File filesDirectory = context.getFilesDir();
        ArrayList <String> noteFiles = new ArrayList<>();

        // Adds all files in the filesDirectory list that ends with the FILE_EXTENSION constant ...
        // into the noteFiles arraylist
        for (String file : filesDirectory.list()){
            if (file.endsWith(FILE_EXTENSION)){
                noteFiles.add(file);
            }
        }

        FileInputStream fis;
        ObjectInputStream ois;

        // For all the files in the noteFiles list
        for (int i = 0; i < noteFiles.size(); i++){
            try {
                fis = context.openFileInput(noteFiles.get(i));
                ois = new ObjectInputStream(fis);

                // Casts files from the noteFiles list into a Note object
                notes.add((Note)ois.readObject());

                fis.close();
                ois.close();
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
        }

        return notes;
    }

    // Checks if file exists, if so, retrives file from disk drive/storage
    public static Note getNoteByName (Context context, String fileName){
        File file = new File (context.getFilesDir(), fileName);
        Note note;

        if (file.exists()){
            FileInputStream fis;
            ObjectInputStream ois;

            try {
                fis = context.openFileInput(fileName);
                ois = new ObjectInputStream(fis);

                note = (Note) ois.readObject();

                fis.close();
                ois.close();
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
            return note;
        }
        return null;
    }

    // Deletes note file
    public static void deleteNote(Context context, String fileName) {
        File dir = context.getFilesDir();
        File file = new File(dir, fileName);

        if (file.exists()){
            file.delete();
        }
    }
}
