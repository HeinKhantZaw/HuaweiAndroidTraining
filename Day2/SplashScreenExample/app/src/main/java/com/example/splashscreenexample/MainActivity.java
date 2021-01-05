package com.example.splashscreenexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView logo;
    TextView yourText;
    Animation top, bottom;
    public static int SPLASH_SCREEN_TIME = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByID();
        loadAnim();
        setAnim();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_TIME);
    }

    private void loadAnim() {
        top = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
    }

    private void setAnim() {
        logo.setAnimation(top);
        yourText.setAnimation(bottom);
    }

    private void findViewByID() {
        logo = findViewById(R.id.logo);
        yourText = findViewById(R.id.textView);
    }
}