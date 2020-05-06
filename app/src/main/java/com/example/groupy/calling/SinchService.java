package com.example.groupy.calling;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;

import org.greenrobot.eventbus.EventBus;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SinchService extends Service implements CallClientListener {
    private CallClient callClient;

    public SinchService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DatabaseReference online_status_all_users;
        FirebaseUser firebaseUser;
        //whats the receiver's details for the page load
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        //FOR ONLINE AND OFFLINE PART
        //say your realtime database has the child `online_statuses`
        online_status_all_users = FirebaseDatabase.getInstance().getReference().child("online_statuses");

        //on each user's device when connected they should indicate e.g. `linker` should tell everyone he's snooping around
        online_status_all_users.child(firebaseUser.getUid()).setValue("online");
        //also when he's not doing any snooping or if snooping goes bad he should also tell
        online_status_all_users.child(firebaseUser.getUid()).onDisconnect().setValue("offline");

        if(Apps.sinchClient==null)
        {
            Apps.sinchClient = Sinch.getSinchClientBuilder().context(this)
                    .applicationKey("212c7bef-7efc-4d85-8a5f-fa7bbe64534a")
                    .applicationSecret("N8ezWXnLTEqapxuHDOYaLg==")
                    .environmentHost("clientapi.sinch.com")
                    .userId(Apps.uid)
                    .build();
            Apps.sinchClient.setSupportActiveConnectionInBackground(true);
            Apps.sinchClient.startListeningOnActiveConnection();
            Apps.sinchClient.setSupportCalling(true);
        }




        Apps.sinchClient.addSinchClientListener(new SinchClientListener() {
            public void onClientStarted(SinchClient client) {
                callClient=client.getCallClient();
                callClient.addCallClientListener(SinchService.this);
                Apps.callClient=callClient;
                EventBus.getDefault().post(new SinchStatus.SinchConnected(client, callClient));
            }
            public void onClientStopped(SinchClient client) {
                EventBus.getDefault().post(new SinchStatus.SinchDisconnected(client));
            }
            public void onClientFailed(SinchClient client, SinchError error) {
                EventBus.getDefault().post(new SinchStatus.SinchFailed(client, error));
            }
            public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) {
            }
            public void onLogMessage(int level, String area, String message) {
                EventBus.getDefault().post(new SinchStatus.SinchLogger(area, message, level));
                Log.d("callz", "-- "+message+"//"+area);
            }
        });
        if(!Apps.sinchClient.isStarted())
            Apps.sinchClient.start();
        return START_STICKY;
    }

    @Override
    public void onIncomingCall(CallClient callClient, Call call) {
        EventBus.getDefault().post(new SinchStatus.SinchIncommingCall(callClient, call));
        Log.d("callz", "ADA TELEPON MASUK: "+call.getRemoteUserId());
        Intent callscreen = new Intent(this, IncommingCallActivity.class);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(call.getRemoteUserId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue(String.class);
                Toast.makeText(getApplicationContext(),call.getCallId()+name,Toast.LENGTH_LONG).show();
                String photourl=dataSnapshot.child("photourl").getValue(String.class);
                callscreen.putExtra("callid", call.getCallId());
                callscreen.putExtra("name",name);
                callscreen.putExtra("photo",photourl);


                callscreen.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(callscreen);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        DatabaseReference online_status_all_users;
        FirebaseUser firebaseUser;
        //whats the receiver's details for the page load
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        //FOR ONLINE AND OFFLINE PART
        //say your realtime database has the child `online_statuses`
        online_status_all_users = FirebaseDatabase.getInstance().getReference().child("online_statuses");

        //on each user's device when connected they should indicate e.g. `linker` should tell everyone he's snooping around
        online_status_all_users.child(firebaseUser.getUid()).setValue("offline");
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }
}
