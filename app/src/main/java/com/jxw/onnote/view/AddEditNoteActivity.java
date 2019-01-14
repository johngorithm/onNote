package com.jxw.onnote.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.jxw.onnote.R;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String ID_EXTRA = "com.jxw.onnote.ID_EXTRA";
    public static final String TITLE_EXTRA = "com.jxw.onnote.TITLE_EXTRA";
    public static final String DESCRIPTION_EXTRA = "com.jxw.onnote.DESCRIPTION_EXTRA";
    public static final String PRIORITY_EXTRA = "com.jxw.onnote.PRIORITY_EXTRA";

    private EditText titleField;
    private EditText descriptionField;
    private NumberPicker priorityField;
    private String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleField = findViewById(R.id.title_input_field);
        descriptionField = findViewById(R.id.description_input_field);
        priorityField = findViewById(R.id.priority_value);

        priorityField.setMaxValue(10);
        priorityField.setMinValue(1);

        // Close Icon
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        // Set ActionBar Values
        Intent incomingIntent = getIntent();
        if (incomingIntent.hasExtra(ID_EXTRA)) {
            setTitle("EDIT NOTE");
            titleField.setText(incomingIntent.getStringExtra(TITLE_EXTRA));
            descriptionField.setText(incomingIntent.getStringExtra(DESCRIPTION_EXTRA));
            priorityField.setValue(incomingIntent.getIntExtra(PRIORITY_EXTRA, 1));
        } else {
            setTitle("ADD NOTE");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_icon:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void saveNote() {
        String title = titleField.getText().toString();
        String description = descriptionField.getText().toString();
        int priority = priorityField.getValue();
        boolean isAllValid = true;

        if (title.trim().isEmpty()) {
            errorMessage = "Title is Required";
            isAllValid = false;
        } else if (description.trim().isEmpty()) {
            errorMessage = "Description is required";
            isAllValid = false;
        } else if (title.trim().isEmpty() && description.trim().isEmpty()){
            errorMessage = "Title and Description are both required";
            isAllValid = false;
        }

        if (isAllValid) {
            // save note
            Intent replyIntent = new Intent();
            replyIntent.putExtra(TITLE_EXTRA, title);
            replyIntent.putExtra(DESCRIPTION_EXTRA, description);
            replyIntent.putExtra(PRIORITY_EXTRA, priority);

            int id = getIntent().getIntExtra(ID_EXTRA, -1);
            if (id != -1) {
                replyIntent.putExtra(ID_EXTRA, id);
            }

            setResult(RESULT_OK, replyIntent);
            finish();
        } else {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            return;
        }


    }

//    private void validateInput() {
//
//    }
}
