package com.askyr.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    ArrayList<Note> notes;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.img_add);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.note_input, null, false);    //

                EditText edtTitle = viewInput.findViewById(R.id.edt_title);                         // EditText View component referencing by id
                EditText edtDescription = viewInput.findViewById(R.id.edt_description);             // EditText View component referencing by id

                // New Dialog box to add new note
                new AlertDialog.Builder(MainActivity.this)
                        .setView(viewInput)
                        .setTitle("Add Note")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // This Method will be called on clicking the "Add" button

                                String title = edtTitle.getText().toString();
                                String description = edtDescription.getText().toString();

                                Note note = new Note(title, description);   // creating a new Note instance with the value entered through dialog box

                                boolean isInserted = new NoteHandler(MainActivity.this).createNote(note);

                                if(isInserted) {
                                    Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                                    loadNotes();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Unable to save note", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }
                        }).show();
            }
        });

        recyclerView = findViewById(R.id.recycler);                                                 // RecyclerView component referencing by id

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        loadNotes();
    }

    public ArrayList<Note> readNotes() {
        // Helper Method to retrieve notes from database and reference them in this class instance

        ArrayList<Note> notesReceived = new NoteHandler(this).readNotes();
        return notesReceived;
    }

    public void loadNotes() {
        // Helper Method to load notes(collection of Note instances) in RecyclerView (also called inflating them on RecyclerView)

        notes = readNotes();

        noteAdapter = new NoteAdapter(notes, this);

        recyclerView.setAdapter(noteAdapter);
    }
}