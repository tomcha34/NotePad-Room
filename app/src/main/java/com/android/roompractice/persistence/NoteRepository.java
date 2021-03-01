package com.android.roompractice.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.android.roompractice.async.DeleteAsyncTask;
import com.android.roompractice.async.InsertAsyncTask;
import com.android.roompractice.async.UpdateAsyncTask;
import com.android.roompractice.models.Note;

import java.util.List;

//Repo class is most effective when handling different Data sources.
public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note) {
        // execute method will pass to DoInBackground method.
        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);

    }

    public void updateNote(Note note) {
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    /* Will return a LiveData query of all the notes in the database.
    LiveData calls, by default, are Asynchronous i.e.
     operate on a background thread. if it was conducted on
     a main thread the program would crash. */
    public LiveData<List<Note>> retrieveNoteTask() {

        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(Note note) {

        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);

    }
}
