package com.examples.tunes;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class TunesApp extends Application{
    public static final String CHANNEL_ID="MusicChannel";

    @Override
    public void onCreate(){
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Playback",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager!=null){
                manager.createNotificationChannel(channel);
            }
        }
    }
}
