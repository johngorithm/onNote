package com.jxw.onnote.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.jxw.onnote.R;
import com.jxw.onnote.adapter.NoteAdapter;
import com.jxw.onnote.data.Note;
import com.jxw.onnote.data.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    public static final int ADD_NOTE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.add_new_note_button);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNoteIntent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(addNoteIntent, ADD_NOTE_REQUEST_CODE);
            }
        });

        RecyclerView notesRecyclerView = findViewById(R.id.notes_recycler_view);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setHasFixedSize(true);

        // View adapter
        final NoteAdapter noteAdapter = new NoteAdapter();
        notesRecyclerView.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                // update recycler view
                noteAdapter.setNotes(notes);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteActivity.TITLE_EXTRA);
            String description = data.getStringExtra(AddNoteActivity.DESCRIPTION_EXTRA);
            int priority = data.getIntExtra(AddNoteActivity.PRIORITY_EXTRA, 1);
            
            Note newNote = new Note(title, description, priority);
            noteViewModel.insert(newNote);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note Not Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
