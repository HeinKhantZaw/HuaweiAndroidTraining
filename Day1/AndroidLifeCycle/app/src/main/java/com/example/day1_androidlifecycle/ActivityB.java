package com.example.day1_androidlifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ActivityB extends AppCompatActivity {
    public static String TAG = "output(Activity_B):";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart() method is called.");
        Toast toast  = Toast.makeText(ActivityB.this, "Activity_B :onStart() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() method is called.");
        Toast toast  = Toast.makeText(ActivityB.this, "Activity_B :onResume() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause() method is called.");
        Toast toast  = Toast.makeText(ActivityB.this, "Activity_B :onPause() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop() method is called.");
        Toast toast  = Toast.makeText(ActivityB.this, "Activity_B :onStop() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() method is called.");
        Toast toast  = Toast.makeText(ActivityB.this, "Activity_B :onDestroy() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart() method is called.");
        Toast toast  = Toast.makeText(ActivityB.this, "Activity_B :onRestart() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG,"onBackPressed() method is called");
        Toast toast  = Toast.makeText(ActivityB.this, "Activity_B :onBackPressed() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }
}