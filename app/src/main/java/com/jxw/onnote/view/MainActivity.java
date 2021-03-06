package com.jxw.onnote.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jxw.onnote.R;
import com.jxw.onnote.adapter.NoteAdapter;
import com.jxw.onnote.data.Note;
import com.jxw.onnote.data.NoteViewModel;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteViewModel noteViewModel;
    public static final int ADD_NOTE_REQUEST_CODE = 1;
    public static final int EDIT_NOTE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.add_new_note_button);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNoteIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                // Deleting the swiped note
                // viewHolder.getAdapterPosition() is used get the "int" position of the swiped note in the recycler view
                noteViewModel.delete(noteAdapter.getNoteAtPostion(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(notesRecyclerView);

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent editNoteIntent = new Intent(MainActivity.this, AddEditNoteActivity.class);

                editNoteIntent.putExtra(AddEditNoteActivity.ID_EXTRA, note.getId());
                editNoteIntent.putExtra(AddEditNoteActivity.TITLE_EXTRA, note.getTitle());
                editNoteIntent.putExtra(AddEditNoteActivity.DESCRIPTION_EXTRA, note.getDescription());
                editNoteIntent.putExtra(AddEditNoteActivity.PRIORITY_EXTRA, note.getPriority());
                editNoteIntent.putExtra(AddEditNoteActivity.CREATED_AT_EXTRA, note.getCreatedAt());

                startActivityForResult(editNoteIntent, EDIT_NOTE_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.TITLE_EXTRA);
            String description = data.getStringExtra(AddEditNoteActivity.DESCRIPTION_EXTRA);
            int priority = data.getIntExtra(AddEditNoteActivity.PRIORITY_EXTRA, 1);
            
            Note newNote = new Note(title, description, priority, new Date().toString(), null);
            noteViewModel.insert(newNote);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.ID_EXTRA, -1);

            if (id == -1) {
                Toast.makeText(this, "Note Can't Be Updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.TITLE_EXTRA);
            String description = data.getStringExtra(AddEditNoteActivity.DESCRIPTION_EXTRA);
            int priority = data.getIntExtra(AddEditNoteActivity.PRIORITY_EXTRA, 1);
            String createdAt = data.getStringExtra(AddEditNoteActivity.CREATED_AT_EXTRA);

            Note noteToUpdate = new Note(title, description, priority, createdAt, new Date().toString());
            noteToUpdate.setId(id);
            
            noteViewModel.update(noteToUpdate);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note Not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
             default:
                 return super.onOptionsItemSelected(item);
        }

    }
}
