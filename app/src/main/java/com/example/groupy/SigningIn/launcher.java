package com.example.groupy.SigningIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.groupy.Home.Home;
import com.example.groupy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Decider if new user


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
            Intent intent = new Intent(launcher.this, Home.class);
            startActivity(intent);
        }
    }
}
