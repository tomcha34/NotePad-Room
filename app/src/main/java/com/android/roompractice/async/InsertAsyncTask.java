package com.android.roompractice.async;

import android.os.AsyncTask;
import android.util.Log;

import com.android.roompractice.models.Note;
import com.android.roompractice.persistence.NoteDao;

// Used for inserting a single job/task on a background thread.
// Has been deprecated (Context and Memory leaks are an issue).
public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {
    private static final String TAG = "InsertAsyncTask";

private NoteDao noteDao;

    public InsertAsyncTask(NoteDao dao) {
        noteDao = dao;
    }


    @Override
    protected Void doInBackground(Note... notes) {
        //This will show the "doInBackground method, is not working on the main thread.
        Log.d(TAG, "doInBackground: thread:  " + Thread.currentThread().getName());

        //Add notes to the DataBase.
        noteDao.insertNotes(notes);
        return null;
    }
}
