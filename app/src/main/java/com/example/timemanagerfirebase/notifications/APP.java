package com.example.timemanagerfirebase.notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class APP extends Application {
    public static final String CHANNEL_ID="Add";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        createNotificationChannels2();

    }
    private void createNotificationChannels()
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel1=new NotificationChannel(
                    CHANNEL_ID,
                    "Add",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel one");
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }
    }
    private void createNotificationChannels2()
    {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channeln=new NotificationChannel(
                    CHANNEL_ID,
                    "Add",
                    NotificationManager.IMPORTANCE_LOW
            );

            channeln.setDescription("This  is another channel ");
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channeln);


        }
    }}
