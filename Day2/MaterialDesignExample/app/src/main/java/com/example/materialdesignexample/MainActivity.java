package com.example.materialdesignexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnButton, btnDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListenerAndId();
    }

    private void setListenerAndId() {
        btnButton = findViewById(R.id.btnButton);
        btnButton.setOnClickListener(this);
        btnDialog = findViewById(R.id.btnDialogs);
        btnDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnButton:
                Intent intent = new Intent(MainActivity.this, MaterialButtons.class);
                startActivity(intent);
                break;
            case R.id.btnDialogs:
                Intent intent1 = new Intent(MainActivity.this, MaterialDialog.class);
                startActivity(intent1);
                break;
        }
    }
}