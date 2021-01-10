Bound services
===
A service is bound when an application component binds to it by calling bindService(). Bound service offers
a client-server interface that allows components to interact with the service, send requests and, get results. A bound service typically lives only while it serves another application component 
and does not run in the background indefinitely.
<br/><br/>
Components which can bind to Service are Activity, Service and Content Provider and we cannot use a
broadcast receiver to connect to a service.

Types of BoundService
---
* Local binding - the service and the component which wants to bind to that particular service are the
part of the same app process.
* Remote binding - the service and the component that wants to bind to
that particular service are in two different processes.

![Two Types](https://user-images.githubusercontent.com/40730345/104118701-3f0ddd00-5359-11eb-8dbb-2f5189a09a8f.JPG)


Creating a BoundService
---
<h3>MyService</h3>

First, we have to creat a class `LocalBinder` inside MyService which extends Binder class. `LocalBinder` class has a method which return the object of MyService. Through this object, the activity(MainActivity in this case) has the gateway to access the public methods(in our example, it's `getDate()`) of the MyService class.
Then, we create IBinder interface and initialize this using local binder class.
In here, IBinder is the class that provides the programming interface that clients can use to interact with the service.
`onBind()` return IBinder object when the service is bound.
`getDate()` method is a public method of BoundService class and it can only be accessed by the object of this class. 

In the code below, I made a service which gets the system Time from the android device.
This is how MyService Class looks like.

#### MyService.java
```java
package com.example.boundserviceexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import java.util.Calendar;
import java.util.Date;

public class MyService extends Service {
   
    //create bound service by extending Binder class
    public class LocalBinder extends Binder {
        MyService getService() {
            return MyService.this; //return the instance of MyService.this
        }
        
    //create IBinder interface and initialize this using local binder class
    private final IBinder iBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder; //returns the instance of interface
    }

    public Date getDate() {
        return Calendar.getInstance().getTime();
    }
}
```

<h3>MainActivity</h3>

In MainActivity, we first create ServiceConnection object that defines callbacks for service binding, and then we have to override `onServiceConnected()` and `onServiceDisconnected()`. When the Android system creates the connection between the client and service, it calls `onServiceConnected()` on the ServiceConnection. The `onServiceConnected()` method includes an IBinder argument, which the client then uses to communicate with the bound service. `isBound` is a boolean variable that checks 
whether the service has bound or not. `onServiceDisconnected()` method is called when the service is disconnected from the client. So, we change the value of `isBound` to false.
  
```java
ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.LocalBinder binder = (MyService.LocalBinder) iBinder;
            service = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };
```
As we need to interact with the service only while MainActivity is visible, we bind the service during `onStart()` and unbind during `onStop()`.
So, in `onStart()`, a new intent is created and `bindService()` is called to start a new service.

```java
 @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, MyService.class);
        bindService(intent, connection,BIND_AUTO_CREATE);
    }
```
In onStop(), we check the value of `isBound`. If it's true (means the service is still binding), then we call `unbindService()` to unbind the service and set `isBound` to false.
If `isBound` is false, then we don't need to call  `unbindService()` as it's already unbound.
```java
 @Override
    protected void onStop() {
        super.onStop();
        if(isBound) {
            unbindService(connection);
            isBound = false;
        }
    }
```
So, only one thing left to do in MainActivity.java. We have to call the service on button click. So, we add the following code. Through service object, `getDated()` is called and it returns the current date and time of the device. Then, `setText()` is used to display the value.
```java
 btnDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBound){
                    Date date = service.getDate();
                    tvDisplay.setText(date.toString());
                }
            }
        });
```

This is the final code for MainActivity.java

#### MainActivity.java
```java
package com.example.boundserviceexample;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnDisplayDate;
    TextView tvDisplay;
    MyService service;
    boolean isBound = false;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.LocalBinder binder = (MyService.LocalBinder) iBinder;
            service = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDisplayDate = findViewById(R.id.btnDisplayDate);
        tvDisplay = findViewById(R.id.textView);
        btnDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBound){
                    Date date = service.getDate();
                    tvDisplay.setText(date.toString());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, MyService.class);
        bindService(intent, connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

}
```
Additional Notes
---
Here are some important notes about binding to a service:

- You should always trap DeadObjectException exceptions, which are thrown when the connection has broken. This is the only exception thrown by remote methods.
- Objects are reference counted across processes.
- You usually pair the binding and unbinding during matching bring-up and tear-down moments of the client's lifecycle, as described in the following examples:
  - If you need to interact with the service only while your activity is visible, you should bind during `onStart()` and unbind during `onStop()`.
  - If you want your activity to receive responses even while it is stopped in the background, then you can bind during `onCreate()` and unbind during `onDestroy()`. Beware that this implies that your activity needs to use the service the entire time it's running (even in the background), so if the service is in another process, then you increase the weight of the process and it becomes more likely that the system will kill it.
