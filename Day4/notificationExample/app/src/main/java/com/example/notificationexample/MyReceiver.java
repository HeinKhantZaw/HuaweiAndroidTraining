package com.example.notificationexample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class MyReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("myReceiver", "received an intent");
        if (intent.getAction().equals(MainActivity.ACTION)) {
            int notifID = intent.getExtras().getInt("NotifID");
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent notifIntent = new Intent(context, MainActivity.class);
            notifIntent.putExtra("myType", "5 seconds later");
            PendingIntent pendingIntent = PendingIntent.getActivity(context, notifID, notifIntent, 0);

            //create notification
            Notification notification = new Notification.Builder(context, MainActivity.id)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("Finished")
                    .setContentText("This is your local notification")
                    .setChannelId(MainActivity.id)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            //show notification
            manager.notify(notifID, notification);

        }
    }
}
