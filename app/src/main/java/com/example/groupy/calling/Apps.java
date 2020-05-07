package com.example.groupy.calling;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.CallClient;


public class Apps extends Application {
    public static String USER_ID;
    //current user's sinch
    public static SinchClient sinchClient;
    //incoming/outgoing user's sinch
    public static CallClient callClient;
    public static String position="0";
    public static String uid="";
    public static String colour="#000000";



    @Override
    public void onCreate() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate();

        if (firebaseUser != null) {

            //setting up sinch
            uid=firebaseUser.getUid();
            USER_ID = firebaseUser.getUid();
            sinchClient = Sinch.getSinchClientBuilder().context(this)
                    .applicationKey("212c7bef-7efc-4d85-8a5f-fa7bbe64534a")
                    .applicationSecret("N8ezWXnLTEqapxuHDOYaLg==")
                    .environmentHost("clientapi.sinch.com")
                    .userId(USER_ID)
                    .build();
            sinchClient.setSupportActiveConnectionInBackground(true);
            sinchClient.startListeningOnActiveConnection();
            sinchClient.setSupportCalling(true);
        }
    }
}

