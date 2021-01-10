package com.example.startserviceexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startServiceOnClick(View view) {
        startService(new Intent(MainActivity.this, MyService.class));
    }

    public void stopServiceOnCLick(View view) {
        stopService(new Intent(MainActivity.this, MyService.class));
    }
}