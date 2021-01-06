package com.example.boundserviceexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Calendar;
import java.util.Date;

public class MyService extends Service {
    //create IBinder interface and initialize this using local binder class
    private final IBinder iBinder = new LocalBinder();

    //create bound service by extending binder class
    public class LocalBinder extends Binder{
        MyService getService(){
            return MyService.this; //return the instance of MyService.this
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  iBinder; //returns the instance of interface
    }
    public Date getDate(){
        return Calendar.getInstance().getTime();
    }
}
