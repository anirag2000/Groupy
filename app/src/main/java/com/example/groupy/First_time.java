package com.example.groupy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class First_time extends AppCompatActivity  {
    String uid;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentFirebaseUser == null) {
            Intent intent = new Intent(First_time.this, MainActivity.class);
            startActivity(intent);
        } else {
            uid = currentFirebaseUser.getUid();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(First_time.this,"stopped",Toast.LENGTH_LONG).show();
        FirebaseAuth.getInstance().signOut();

    }


    public void register(View view) {
        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.date);
        EditText date = findViewById(R.id.date);
        EditText group_id = findViewById(R.id.group_code);
        String name_string = name.getText().toString();
        String date_string = date.getText().toString();
        String email_string = email.getText().toString();
        String group_id_string = group_id.getText().toString();

        if (name_string != null && date_string != null && email_string != null) {
            mAuth = FirebaseAuth.getInstance();
            try {
                FirebaseUser user = mAuth.getCurrentUser();
                String userId = user.getUid();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users").child(userId);
                User_details user_details = new User_details(name_string, date_string, email_string, group_id_string);
                Intent intent=new Intent(First_time.this,Home.class);
                startActivity(intent);

                myRef.setValue(user_details);
            } catch (Exception e) {
                Toast.makeText(First_time.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }


    }
}
