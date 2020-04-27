package com.example.groupy.SigningIn;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupy.Home.Home;
import com.example.groupy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.CallClient;

//Decider if new user


public class launcher extends AppCompatActivity {
    public static String USER_ID;
    public static SinchClient sinchClient;
    public static CallClient callClient;

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
