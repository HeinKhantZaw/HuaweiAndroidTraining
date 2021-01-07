package com.example.notificationexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String ACTION = "android_notification";
    public static String id = "myChannel";
    NotificationManager notificationManager;
    NotificationFragment mFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String info = "Notification";
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            info = extra.getString("myType");
            if (info == null) {
                info = "still nothing";
            }
        }
        Log.v("MainActivity", "info:" + info);
        mFragment = NotificationFragment.newInstance(info);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.notifFragment, mFragment).commit();
        }
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id,
                    "myChannel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription("Add your description here");
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setShowBadge(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});
            notificationManager.createNotificationChannel(mChannel);
        }
    }

    public void setAlarm(View view){
        int NotifID = 1;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,0);

        //PendingIntent to launch receiver when the alarm triggers
        Intent notificationIntent = new Intent(MainActivity.ACTION);
        notificationIntent.setPackage("com.example.notificationexample");
        notificationIntent.putExtra("NotifID",NotifID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,NotifID,notificationIntent,0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        finish();
    }
}