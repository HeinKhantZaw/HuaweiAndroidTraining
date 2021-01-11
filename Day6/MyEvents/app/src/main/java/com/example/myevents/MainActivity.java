package com.example.myevents;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAddEvent, btnShowEvent;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        grantPermission();
    }

    private void initView() {
        btnAddEvent = findViewById(R.id.btnAddEvent);
        btnShowEvent = findViewById(R.id.btnShowEvent);
        btnAddEvent.setOnClickListener(this);
        btnShowEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddEvent:
                addEvent();
                break;
            case R.id.btnShowEvent:
                showEvent();
                break;
        }
    }

    private void showEvent() {
        cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getColumnIndex(CalendarContract.Events._ID);
                int title = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                int desc = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);
                int location = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);

                String idValue = cursor.getColumnName(id);
                String titleValue = cursor.getString(title);
                String descValue = cursor.getString(desc);
                String locationValue = cursor.getString(location);
                Log.i("event",idValue + " " + titleValue + " " + descValue + " " + locationValue); // can view the output in logcat
                showToast(idValue + " " + titleValue + " " + descValue + " " + locationValue); // If there are too many events in your device calender, Toast can't show all.
            }
        } else {
            Log.d("event","No Event");
            showToast("No Event");
        }
    }

    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void addEvent() {
        ContentResolver contentResolver = this.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CalendarContract.Events.TITLE, getString(R.string.eventTitle));
        contentValues.put(CalendarContract.Events.DESCRIPTION, getString(R.string.eventDesc));
        contentValues.put(CalendarContract.Events.EVENT_LOCATION, getString(R.string.eventLocation));
        contentValues.put(CalendarContract.Events.DTSTART, Calendar.getInstance().getTimeInMillis());
        contentValues.put(CalendarContract.Events.DTEND, Calendar.getInstance().getTimeInMillis() + 3600000);
        contentValues.put(CalendarContract.Events.CALENDAR_ID, 1);
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues);
        Log.d("event","Event Added");
        showToast("Event Added");
    }

    private void grantPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            String[] strings = {
                    Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
            };
            ActivityCompat.requestPermissions(this, strings, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            showToast("You've allowed Permission");
        }
    }
}