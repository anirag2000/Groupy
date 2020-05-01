package com.example.groupy.calling;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.example.groupy.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallState;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class IncommingCallActivity extends AppCompatActivity implements View.OnClickListener,SensorEventListener {
    private TextView mCallingStatus;
    private TextView mCallingName;
    private LinearLayout mCallingNotify;
    private Button mCallingAnswer;
    private Button mCallingReject;
    private LinearLayout mCallingActionButton;
    private Call call;
    private Ringtone r;
    private boolean isIncomming;
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private View mCallingBlacksreen;
CircularImageView profilepic;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //


        setContentView(R.layout.incommingcall_layout);
        //initialize all the views
        initView();
        //set up microphone sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //phones proximity sensor
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        Intent intent=getIntent();
        //incoming call information stored in call
        call = Apps.callClient.getCall(getIntent().getStringExtra("callid"));
        isIncomming=getIntent().getBooleanExtra("incomming", true);
        if(isIncomming) {
            //if u are getting a call load up the layout
            setBlinking(mCallingNotify, true);
            mCallingStatus.setText("Call From ");
            String name=intent.getStringExtra("name");
            mCallingName.setText(name+"");
            String imageurl=intent.getStringExtra("photo");
            Glide.with(IncommingCallActivity.this).load(imageurl).into(profilepic);
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            UiUtils.setFullscreen(this, true);
            r.play();
        }else{
            //if u are the one calling
            UiUtils.setFullscreen(this, false);
            mCallingStatus.setText("Calling");
            mCallingAnswer.setVisibility(View.GONE);
            String name=intent.getStringExtra("name");
            mCallingName.setText(name+"");
            mCallingReject.setText("END");
            String imageurl=intent.getStringExtra("photo");
            Glide.with(IncommingCallActivity.this).load(imageurl).into(profilepic);
        }
    }

    private void initView() {
        mCallingStatus = findViewById(R.id.calling_status);
        mCallingName = findViewById(R.id.calling_name);
        mCallingNotify = findViewById(R.id.calling_notify);
        mCallingAnswer = findViewById(R.id.calling_answer);
        mCallingAnswer.setOnClickListener(this);
        mCallingReject = findViewById(R.id.calling_reject);
        mCallingReject.setOnClickListener(this);
        mCallingActionButton = findViewById(R.id.calling_action_button);
        mCallingBlacksreen=findViewById(R.id.calling_blackscreen);
        profilepic=findViewById(R.id.pic);
    }

    @Override
    public void onClick(View v) {
        //which button was clicked
        switch (v.getId()) {
            default:
                break;
            case R.id.calling_answer:
                call.answer();
                mCallingAnswer.setVisibility(View.GONE);
                mCallingReject.setText("END");
                mCallingStatus.setText("ACTIVE CALL");
                setBlinking(mCallingNotify, false);
                if(r!=null)r.stop();
                UiUtils.setFullscreen(this, false);
                break;
            case R.id.calling_reject:
                call.hangup();
                if(r!=null)r.stop();
                finish();
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSinchLogging(SinchStatus.SinchLogger sinchLogger){
        if(sinchLogger.message.contains("terminationCause=DENIED")&&!isIncomming){
            if(r!=null)r.stop();
            call.hangup();
            Toast.makeText(this, "USER REJECTED", Toast.LENGTH_SHORT).show();
            finish();
        }else if(sinchLogger.message.contains("onSessionEstablished")){
            mCallingStatus.setText("ACTIVE CALL");
        }else if(sinchLogger.message.contains("onSessionTerminated")){
            call.hangup();
            if(r!=null)r.stop();
            if(sinchLogger.message.contains("terminationCause=NO_ANSWER")){
                Toast.makeText(this, "NO ANSWER", Toast.LENGTH_SHORT).show();
            }else if(sinchLogger.message.contains("terminationCause=TIMEOUT")){
                Toast.makeText(this, "TIMEOUT", Toast.LENGTH_SHORT).show();
            }else if(sinchLogger.message.contains("terminationCause=CANCELED")){
                Notification n = new NotificationCompat.Builder(this, "calling").setContentTitle("Missed Call").setAutoCancel(true).setContentText("You have missed call from " + call.getRemoteUserId()).setSmallIcon(android.R.drawable.sym_call_missed).build();
                NotificationManagerCompat.from(this).notify(1133,n);
            }
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    private void setBlinking(View object, boolean status) {
        if(!status){
            object.animate().cancel();
            return;
        }
        ObjectAnimator anim= ObjectAnimator.ofFloat(object, View.ALPHA, 0.1f,1.0f);
        anim.setDuration(1000);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        if(event.values[0]==0){
            if(!isIncomming || call.getState() == CallState.ESTABLISHED) {
                params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                params.screenBrightness = 0;
                getWindow().setAttributes(params);
                UiUtils.enableDisableViewGroup((ViewGroup) findViewById(R.id.calling_root).getParent(), false);
                UiUtils.setFullscreen(this, true);
                mCallingBlacksreen.setVisibility(View.VISIBLE);
            }
        }else {
            params.screenBrightness = -1;
            getWindow().setAttributes(params);
            UiUtils.enableDisableViewGroup((ViewGroup)findViewById(R.id.calling_root).getParent(),true);
            UiUtils.setFullscreen(this, false);
            mCallingBlacksreen.setVisibility(View.GONE);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity,SensorManager.SENSOR_DELAY_NORMAL);
    }
}
