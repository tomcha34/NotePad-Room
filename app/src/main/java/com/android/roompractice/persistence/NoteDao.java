package com.android.roompractice.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.roompractice.models.Note;

import java.util.List;

//Declaring that this will be used to accesss the database
@Dao
public interface NoteDao {

    //for a new note. "..." means an array of.
    @Insert
    void insertNotes(Note... notes);

    //select all(*) from the "notes" database
    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    //custom query to select all notes with desired "title".
    @Query("SELECT * FROM notes WHERE title LIKE :title")
    List<Note> getNoteWithCustomQuery(String title);


    //for deleting notes. will return the number of rows deleted.
    @Delete
    int delete(Note... notes);

    //for updating notes. Will return the number of notes deleted
    @Update
    int update(Note... notes);
}
