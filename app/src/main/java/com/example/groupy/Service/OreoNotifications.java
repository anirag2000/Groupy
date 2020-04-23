package com.example.groupy.Service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.groupy.R;

public class OreoNotifications extends ContextWrapper {

    private static final String CHANNEL_ID = "com.raghavendra.groupy";
    private static final String CHANNEL_NAME = "groupy";

    private NotificationManager notificationManager;

    public OreoNotifications(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return  notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void  getOreoNotification(String title, String body,
                                                     PendingIntent pendingIntent, Uri soundUri, String icon,int j){

        final Bitmap[] bitmap = {null};

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(icon)
                .into(new CustomTarget<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        bitmap[0] = resource;

displayImageNotification(bitmap[0],title,body,pendingIntent,soundUri,j);

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayImageNotification(Bitmap bitmap, String title, String body, PendingIntent pendingIntent, Uri soundUri, int j) {

        Notification.Builder builder=new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.chat)
                .setLargeIcon(bitmap)
                .setSound(soundUri)
                .setAutoCancel(true);
        int i=0;
        if(j>0)
        {
            i=j;
        }

        getManager().notify(i, builder.build());
    }


//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void  displayImageNotification(Bitmap bitmap, String title, String body, PendingIntent pendingintent, Uri soundUri) {
//
//        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
//                .setContentIntent(pendingintent)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setLargeIcon(bitmap)
//                .setSound(soundUri)
//                .setAutoCancel(true);
//
//        oreoNotification.getManager().notify(i, builder.build());
//
//
//
//    }

}