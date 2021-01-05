package com.example.layoutexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConstraintLayoutActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLLH, btnLLV, btnRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListenerAndFindViewID();

    }

    private void setListenerAndFindViewID() {
        btnLLH = findViewById(R.id.btnLLH);
        btnLLV = findViewById(R.id.btnLLV);
        btnRL = findViewById(R.id.btnRL);
        btnLLH.setOnClickListener(this);
        btnLLV.setOnClickListener(this);
        btnRL.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLLH:
                goToActivity(LinearLayoutVerticalActivity.class);
                break;
            case R.id.btnLLV:
                goToActivity(LinearLayoutHorizontalActivity.class);
                break;
            case R.id.btnRL:
                goToActivity(RelativeLayoutActivity.class);
                break;
        }
    }

    private void goToActivity(Class className) {
        Intent intent = new Intent(ConstraintLayoutActivity.this, className);
        startActivity(intent);
    }
}