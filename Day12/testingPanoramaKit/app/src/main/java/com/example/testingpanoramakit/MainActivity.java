package com.example.testingpanoramakit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huawei.hms.panorama.Panorama;
import com.huawei.hms.panorama.PanoramaInterface;
import com.huawei.hms.support.api.client.ResultCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnDisplay, btnDisplayRing, btnPickImg, btnVideo, btnDisplayInApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        init();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] str = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, str, 1);
        }
    }

    private void init() {
        btnDisplay = findViewById(R.id.btnDisplay);
        btnDisplayRing = findViewById(R.id.btnDisplayRing);
        btnPickImg = findViewById(R.id.btnPickImg);
        btnDisplayInApp = findViewById(R.id.btnDisplayInApp);
        btnVideo = findViewById(R.id.btnVideo);

        btnDisplay.setOnClickListener(this);
        btnDisplayRing.setOnClickListener(this);
        btnPickImg.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
        btnDisplayInApp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDisplay:
            case R.id.btnDisplayRing:
                displayPanorama(view.getId());
                break;
            case R.id.btnPickImg:
                pickImgAndDisplay();
                break;
            case R.id.btnVideo:
            case R.id.btnDisplayInApp:
                displayInApp(view.getId());
                break;
        }
    }

    private void displayInApp(int id) {
        Intent intent = new Intent(this, InAppPanoramaImageActivity.class);
        intent.putExtra("ViewId", id);
        startActivity(intent);
    }

    private void pickImgAndDisplay() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1 || resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "onActivityResult requestCode or resultCode invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (data == null) {
            Toast.makeText(this, "onActivityResult data is null", Toast.LENGTH_SHORT).show();
            return;
        }
        Panorama.getInstance().loadImageInfoWithPermission(this, data.getData(), PanoramaInterface.IMAGE_TYPE_SPHERICAL).setResultCallback(new resultCallback());
    }

    private void displayPanorama(int id) {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pano);
        switch (id) {
            case R.id.btnDisplay:
                Panorama.getInstance().loadImageInfo(getApplicationContext(), uri, PanoramaInterface.IMAGE_TYPE_SPHERICAL).setResultCallback(new resultCallback());
                break;
            case R.id.btnDisplayRing:
                Panorama.getInstance().loadImageInfo(getApplicationContext(), uri, PanoramaInterface.IMAGE_TYPE_RING).setResultCallback(new resultCallback());
                break;
        }
    }

    private class resultCallback implements ResultCallback<PanoramaInterface.ImageInfoResult> {
        @Override
        public void onResult(PanoramaInterface.ImageInfoResult imageInfoResult) {
            if (imageInfoResult == null) {
                Toast.makeText(MainActivity.this, "imageInfoResult is null", Toast.LENGTH_SHORT).show();
            }
            if (imageInfoResult.getStatus().isSuccess()) {
                Intent intent = imageInfoResult.getImageDisplayIntent();
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Error! Intent null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Error Status:" + imageInfoResult.getStatus(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}