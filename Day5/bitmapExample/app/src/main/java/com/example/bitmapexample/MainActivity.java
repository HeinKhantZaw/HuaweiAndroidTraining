package com.example.bitmapexample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img;
    Button btnRotate, btnScale, btnSkew, btnTranslate;
    private float currentDegree = 0;
    private float currentX_Scale = 0;
    private float currentY_Scale = 0;
    int originalImageWidth, originalImageHeight;
    Bitmap.Config originalImageConfig;
    Bitmap originalBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        BitmapDrawable originalBitmapDrawable = (BitmapDrawable) img.getDrawable();
        originalBitmap = originalBitmapDrawable.getBitmap();
        originalImageWidth = originalBitmap.getWidth();
        originalImageHeight = originalBitmap.getHeight();
        originalImageConfig = originalBitmap.getConfig();
    }

    private void initView() {
        img = findViewById(R.id.imageView);
        btnRotate = findViewById(R.id.btnRotate);
        btnScale = findViewById(R.id.btnScale);
        btnSkew = findViewById(R.id.btnSkew);
        btnTranslate = findViewById(R.id.btnTranslate);
        btnRotate.setOnClickListener(this);
        btnScale.setOnClickListener(this);
        btnTranslate.setOnClickListener(this);
        btnSkew.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRotate:
                doRotate();
                break;
            case R.id.btnScale:
                doScale();
                break;
            case R.id.btnSkew:
                doSkew();
                break;
            case R.id.btnTranslate:
                doTranslate();
                break;
        }
    }

    private void doRotate() {
        currentDegree += 120;
        Bitmap rotateBitmap = Bitmap.createBitmap(originalImageWidth, originalImageHeight, originalImageConfig);
        Canvas rotateCanvas = new Canvas(rotateBitmap);
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.setRotate(currentDegree, originalImageWidth / 2.0f, originalImageHeight / 2.0f);
        Paint paint = new Paint();
        rotateCanvas.drawBitmap(originalBitmap, rotateMatrix, paint);
        img.setImageBitmap(rotateBitmap);
    }

    private void doScale() {
        currentX_Scale -= 0.1;
        currentY_Scale -= 0.1;
        if (currentX_Scale <= 0) {
            currentX_Scale = 1;
        }
        if (currentY_Scale <= 0) {
            currentY_Scale = 1;
        }
        Bitmap scaleBitmap = Bitmap.createBitmap((int) (originalImageWidth * currentX_Scale), (int) (originalImageHeight * currentY_Scale), originalImageConfig);
        Canvas scaleCanvas = new Canvas(scaleBitmap);
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(currentX_Scale, currentY_Scale, originalImageWidth / 2.0f, originalImageHeight / 2.0f);
        Paint paint = new Paint();
        scaleCanvas.drawBitmap(originalBitmap, scaleMatrix, paint);
        img.setImageBitmap(scaleBitmap);
    }

    private void doSkew() {
        double randNum = Math.random();
        double xSkew = randNum;
        double ySkew = randNum;
        int xAfterSkew = (int) (originalImageWidth * (1 + randNum));
        int yAfterSkew = (int) (originalImageHeight * (1 + randNum));
        Bitmap skewBitmap = Bitmap.createBitmap(xAfterSkew, yAfterSkew, originalImageConfig);
        Canvas skewCanvas = new Canvas(skewBitmap);
        Matrix skewMatrix = new Matrix();
        skewMatrix.setSkew((float) xSkew, (float) ySkew, originalImageWidth / 2.0f, originalImageHeight / 2.0f);
        Paint paint = new Paint();
        skewCanvas.drawBitmap(originalBitmap,skewMatrix,paint);
        img.setImageBitmap(skewBitmap);
    }

    private void doTranslate() {
        Random random= new Random();
        int xTranslate = random.nextInt(2000);
        int yTranslate = random.nextInt(2000);
        Bitmap translateBitmap = Bitmap.createBitmap(originalImageWidth+xTranslate,originalImageHeight+yTranslate,originalImageConfig);
        Canvas translateCanvas = new Canvas(translateBitmap);
        Matrix translateMatrix = new Matrix();
        translateMatrix.setTranslate(xTranslate,yTranslate);
        Paint paint = new Paint();
        translateCanvas.drawBitmap(originalBitmap,translateMatrix,paint);
        img.setImageBitmap(translateBitmap);
    }
}