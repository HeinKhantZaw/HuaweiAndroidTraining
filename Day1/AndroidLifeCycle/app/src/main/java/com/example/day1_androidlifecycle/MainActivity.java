package com.example.day1_androidlifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edtValue;
    Button btn;
    public static String TAG = "output(Activity_A):";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtValue = findViewById(R.id.edtValue);
        btn = findViewById(R.id.btn1);

        // You can see this in android studio log cat
        Log.i(TAG, "onCreate() method is called.");

        // You can see this in application
        Toast toast  = Toast.makeText(MainActivity.this, "Activity_A :onCreate() method is called.", Toast.LENGTH_SHORT);
        toast.show();

        //setOnClickListener for button => (Which means what will happen when this button is clicked)
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,ActivityB.class); // Intent is used to start another activity
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart() method is called.");
        Toast toast  = Toast.makeText(MainActivity.this, "Activity_A :onStart() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume() method is called.");
        Toast toast  = Toast.makeText(MainActivity.this, "Activity_A :onResume() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause() method is called.");
        Toast toast  = Toast.makeText(MainActivity.this, "Activity_A :onPause() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop() method is called.");
        Toast toast  = Toast.makeText(MainActivity.this, "Activity_A :onStop() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() method is called.");
        Toast toast  = Toast.makeText(MainActivity.this, "Activity_A :onDestroy() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart() method is called.");
        Toast toast  = Toast.makeText(MainActivity.this, "Activity_A :onRestart() method is called.", Toast.LENGTH_SHORT);
        toast.show();
    }

}