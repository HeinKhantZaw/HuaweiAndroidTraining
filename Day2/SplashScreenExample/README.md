Splashscreen
====

Splash Screen is most commonly the first startup screen which appears when App is opened. 
In other words, it is a simple constant screen for a fixed amount of time which is used to display some animations with company logo, name, advertising content etc.
Normally it shows when app is first time launched on android device.

Making a splashscreen
----
<strong>Step 1 :</strong> Open res > layout > activity_main.xml. Then add the following code.
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#76AFC2"
    tools:context=".MainActivity">

    <ImageView
        android:id="@+id/logo"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="76dp"
        android:background="#FFFFFF"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.497"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@mipmap/logo" />

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:fontFamily="cursive"
        android:text="you can add your text here"
        android:textColor="#FFF"
        android:textSize="40sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/logo" />

</androidx.constraintlayout.widget.ConstraintLayout>
```
<strong>Step 2 :</strong> Then we have to create an anim directory in res folder to add animation resource files.
In anim directory, create two anim files - one for image animation and another for text animation.

### For image animation
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
<translate
    android:duration="2000"
    android:fromXDelta="0%"
    android:fromYDelta="-50%" />
<alpha
    android:duration="1500"
    android:fromAlpha="0.1"
    android:toAlpha="1" />
</set>
```

### For text animation
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="2000"
        android:fromXDelta="0%"
        android:fromYDelta="100%" />
    <alpha
        android:duration="1500"
        android:fromAlpha="0.1"
        android:toAlpha="1" />
</set>
```
<strong>Step 3 :</strong> Now we have to create a new activity to show after Splash screen. 

<strong>Step 4 :</strong> After that, we finally add a handler in MainActivity. A handler is used to hold the screen for specific time and 
once the handler is out, HomeActivity will be launched. In the following code,
`postDelayed()` method will delay the time for 4 seconds (4000 milliseconds). 

```java
 protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = findViewById(R.id.logo);
        yourText = findViewById(R.id.textView);
        top = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        logo.setAnimation(top);
        yourText.setAnimation(bottom);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
```
