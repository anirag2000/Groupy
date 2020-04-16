package com.example.groupy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        FirebaseUser firebaseUser;
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentFirebaseUser== null) {
            Intent intent = new Intent(launcher.this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(launcher.this, First_time.class);
            startActivity(intent);
        }








    }
}
