package com.example.groupy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddNotes extends AppCompatActivity {
String uid;
String group_id;
String name;
AlertDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNotes.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog= builder.create();

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        uid=firebaseUser.getUid();
        ImageButton add=findViewById(R.id.add);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                group_id=snapshot.child("group_id").getValue(String.class);
                name=snapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                EditText title=findViewById(R.id.addtitle);
                EditText description=findViewById(R.id.adddescription);
                String title_text=title.getText().toString();
                String description_text=description.getText().toString();
                if(!title_text.isEmpty()&& !description_text.isEmpty()){
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                    NotesModal notesModal=new NotesModal(title_text,description_text,name);
                    databaseReference.child("group").child("group_code").child(group_id).child("notes").push().setValue(notesModal);
                    AsyncTask.execute(() -> {
                        Intent intent=new Intent(AddNotes.this,NotesMain.class);
                        startActivity(intent);



                    });
                    new Handler().postDelayed(() -> {
                        dialog.hide();;


                    },1500);



                }
                else {
                    Toast.makeText(AddNotes.this,"Empty Fields",Toast.LENGTH_LONG).show();
                }



            }
        });
    }
}
