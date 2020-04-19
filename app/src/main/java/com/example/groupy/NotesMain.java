package com.example.groupy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotesMain extends AppCompatActivity {
String group_code;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_main);
Intent intent=getIntent();
group_code=intent.getStringExtra("group_code");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          Intent intent=new Intent(NotesMain.this,AddNotes.class);
          intent.putExtra("group_code",group_code);
          startActivity(intent);
            }
        });

     ArrayList<String> title = new ArrayList<>();
     ArrayList<String> users= new ArrayList<>();
        ArrayList<String> mdescription = new ArrayList<>();
        DatabaseReference ref1= FirebaseDatabase.getInstance().getReference().child("group").child("group_code").child(group_code).child("notes");




        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    NotesModal notesModal=dsp.getValue(NotesModal.class);
                    title.add(notesModal.title);
                    mdescription.add(notesModal.description);

                    users.add(notesModal.uid);
                    NotesAdapter adapter = new NotesAdapter(NotesMain.this,title,mdescription,users);

                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


           recyclerView = findViewById(R.id.recyclerview);
        int numberOfColumns = 2;
        int spaceInPixels = 1;
        recyclerView.addItemDecoration(new RecyclerViewItemDecorator(spaceInPixels));
        recyclerView.setLayoutManager(new GridLayoutManager(NotesMain.this, numberOfColumns));


        NotesAdapter adapter = new NotesAdapter(NotesMain.this,title,mdescription,users);

        recyclerView.setAdapter(adapter);
    }
}
