package com.example.testingpanoramakit;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.panorama.Panorama;
import com.huawei.hms.panorama.PanoramaInterface;
import com.huawei.hms.panorama.ResultCode;

import java.io.IOException;

public class InAppPanoramaImageActivity extends Activity implements View.OnTouchListener {
    ConstraintLayout layout;
    View panoramaView;
    TextView tvChangeMode;
    MediaPlayer mediaPlayer;
    PanoramaInterface.PanoramaLocalInterface panoramaLocalInterface;
    int controlModeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreenMode();
        setContentView(R.layout.activity_in_app_panorama_image);

        layout = findViewById(R.id.layout);
        tvChangeMode = findViewById(R.id.tvMode);

        panoramaLocalInterface = Panorama.getInstance().getLocalInstance(this);

        if (panoramaLocalInterface == null) {
            Toast.makeText(this, "panoramaLocalInstance is null", Toast.LENGTH_SHORT).show();
            return;
        }

        int ret = panoramaLocalInterface.init();
        if (ret != ResultCode.SUCCEED) {
            Toast.makeText(this, "panoramaLocalInstance init failed" + ret, Toast.LENGTH_SHORT).show();
            return;
        }
        initControls();
        Intent intent = getIntent();
        showDisplay(intent.getIntExtra("ViewId", 0));
    }

    private void showDisplay(int viewId) {
        switch (viewId) {
            case R.id.btnDisplayInApp:
                displayPano();
                break;
            case R.id.btnVideo:
                displayVideo();
                break;
        }
        if (tvChangeMode != null) {
            tvChangeMode.bringToFront();
        }
    }

    private void displayPano() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pano);
        int result = panoramaLocalInterface.setImage(uri, PanoramaInterface.IMAGE_TYPE_RING);
        if (result != ResultCode.SUCCEED) {
            Toast.makeText(this, "Fail to load! Error:" + result, Toast.LENGTH_SHORT).show();
        }
        addViewToLayout();
    }

    private void displayVideo() {
        Surface surface = panoramaLocalInterface.getSurface(PanoramaInterface.IMAGE_TYPE_SPHERICAL);
        if (surface == null) {
            Toast.makeText(this, "Surface is null", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pano_vid);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.setSurface(surface);
        mediaPlayer.setVolume(0,0);
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        float ratio = mediaPlayer.getVideoWidth() / (float) mediaPlayer.getVideoHeight();
        Toast.makeText(this, "ratio = " + ratio, Toast.LENGTH_SHORT).show();
        panoramaLocalInterface.setValue(PanoramaInterface.KEY_VIDEO_RATIO, String.valueOf(ratio));
        addViewToLayout();
    }

    private void addViewToLayout() {
        panoramaView = panoramaLocalInterface.getView();
        if (panoramaView == null) {
            Toast.makeText(this, "View is null", Toast.LENGTH_SHORT).show();
            return;
        } else {
            panoramaView.setOnTouchListener(this);
            layout.addView(panoramaView);
        }
    }


    private void initControls() {
        tvChangeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controlModeCount % 3 == 0) {
                    panoramaLocalInterface.setControlMode(PanoramaInterface.CONTROL_TYPE_TOUCH);
                    tvChangeMode.setText("Touch");
                } else if (controlModeCount % 3 == 1) {
                    panoramaLocalInterface.setControlMode(PanoramaInterface.CONTROL_TYPE_POSE);
                    tvChangeMode.setText("Pose");
                } else {
                    panoramaLocalInterface.setControlMode(PanoramaInterface.CONTROL_TYPE_MIX);
                    tvChangeMode.setText("Mix");
                }
                controlModeCount++;
            }
        });
        tvChangeMode.performClick();
    }

    private void initView() {

    }

    private void fullScreenMode() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Window win = getWindow();
        win.getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (panoramaView != null && panoramaView.equals(view)) {
            if (panoramaLocalInterface != null) {
                panoramaLocalInterface.updateTouchEvent(motionEvent);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (panoramaLocalInterface != null) {
            panoramaLocalInterface.deInit();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}