package com.example.groupy.SigningIn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.groupy.Home.Home;
import com.example.groupy.R;
import com.example.groupy.calling.Apps;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.CallClient;

//Decider if new user


public class launcher extends AppCompatActivity {
    public static String USER_ID;
    public static SinchClient sinchClient;
    public static CallClient callClient;

    int storagecode =1;


    private void requestPERMISSIONShere(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION},storagecode);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        FirebaseUser firebaseUser;
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(ContextCompat.checkSelfPermission(launcher.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(launcher.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(launcher.this,"You have Granted Storage Permission", Toast.LENGTH_SHORT).show();

        } else{

            requestPERMISSIONShere();


        }


        if (currentFirebaseUser== null) {

            Intent intent = new Intent(launcher.this, MainActivity.class);
            startActivity(intent);
        } else {


            Apps.uid=currentFirebaseUser.getUid();


            Intent intent = new Intent(launcher.this, Home.class);
            startActivity(intent);
        }

    }
}
