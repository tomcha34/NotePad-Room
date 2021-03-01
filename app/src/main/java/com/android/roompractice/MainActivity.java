package com.android.roompractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.roompractice.adapters.NotesRecyclerAdapter;
import com.android.roompractice.models.Note;
import com.android.roompractice.persistence.NoteRepository;
import com.android.roompractice.util.VerticalItemSpacingDecorator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesRecyclerAdapter.OnNoteListener,
        View.OnClickListener {
    private static final String TAG = "MainActivity";

    //Ui Components
    private RecyclerView mRecyclerView;

    //Variables
    private ArrayList<Note> mNotes = new ArrayList<>();
    NotesRecyclerAdapter mNotesRecyclerAdapter;
    private NoteRepository mNoteRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);
        mNoteRepository = new NoteRepository(this);
        findViewById(R.id.fab).setOnClickListener(this);

        initRecyclerView();
        retrieveNotes();
        //insertFakeNotes();

        Log.d(TAG, "onCreate: Thread: " + Thread.currentThread().getName());


    }

    /* this method will listen to the database. The Observer will notify and update whenever
     the database is changed. */
    private void retrieveNotes(){
        mNoteRepository.retrieveNoteTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // if there is notes in the list I want to clear them because
                // I am querying the list again.
                if(notes.size() > 0){
                    mNotes.clear();
                } if(mNotes != null){
                    mNotes.addAll(notes);
                }
                // Tell your adapter the dataset has changed so it updates the View.
                mNotesRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void insertFakeNotes() {
        for (int i = 0; i < 1000; i++) {
            Note note = new Note();
            note.setTitle("title # " + i);
            note.setContent("content # " + i);
            note.setTimestamp("Feb 2021");
            mNotes.add(note);

        }
        mNotesRecyclerAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalItemSpacingDecorator itemSpacingDecorator = new VerticalItemSpacingDecorator(10);
        mRecyclerView.addItemDecoration(itemSpacingDecorator);
        //Activate our ItemTouchHelper so swiping right deletes a list item note.
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mNotesRecyclerAdapter = new NotesRecyclerAdapter(mNotes, this);
        mRecyclerView.setAdapter(mNotesRecyclerAdapter);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: clicked:" + position);
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);

    }

    private void deleteNote(Note note){
        mNotes.remove(note);
        mNotesRecyclerAdapter.notifyDataSetChanged();

        mNoteRepository.deleteNote(note);
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback
            = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        // This will allow you to move list items in MainActivity
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        //This will allow you to swipe list items in MainActivity
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));

        }
    };
}