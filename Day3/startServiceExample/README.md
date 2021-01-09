Android Service
====
A Service is an application component that can perform long-running operations in the background. It does not provide a user interface. 
Once started, a service might continue running for some time, even after the user switches to another application. 
Additionally, a component can bind to a service to interact with it and even perform interprocess communication (IPC). 
For example, a service can handle network transactions, play music, perform file I/O, or interact with a content provider, all from the background.
Each service class must have a corresponding `<service>` declaration in its package's AndroidManifest.xml. 
Services can be started with `Context.startService()` and `Context.bindService()`


Types of Service
---
There are three different types of services:

<h3> 1. Foreground Service </h3>
<p>
A foreground service performs some operation that is noticeable to the user. For example, an audio app would use a foreground service to play an audio track.
Foreground services must display a Notification. Foreground services continue running even when the user isn't interacting with the app.
When you use a foreground service, you must display a notification so that users are actively aware that the service is running. 
This notification cannot be dismissed unless the service is either stopped or removed from the foreground. As for another example, a fitness app can uses a foreground service to record a user's running activity, after receiving permission from the user.
The notification might show the distance that the user has traveled after running.
</p>

<h3> 2. Background Service </h3>
<p>
A background service performs an operation that isn't directly noticed by the user.
For example, if an app used a service to compact its storage, that would usually be a background service.
</p>

<h3> 3. Bound Service </h3>
<p>
A service is bound when an application component binds to it by calling bindService(). 
A bound service offers a client-server interface that allows components to interact with the service, send requests, receive results, and 
even do so across processes with interprocess communication (IPC). A bound service runs only as long as another application component is bound to it. 
Multiple components can bind to the service at once, but when all of them unbind, the service is destroyed. For example, an audio player might find it useful to allow its service to run indefinitely and also provide binding. This way, an activity can start the service to play some music and the music continues to play even if the user leaves the application. Then, when the user returns to the application, the activity can bind to the service to regain control of playback.</p> 

Go to [additional notes](https://github.com/HeinKhantZaw/HuaweiAndroidTraining/tree/main/Day3/startServiceExample#additional-notes) for further explanation.

Service Lifecycle
---
The follwing figure shows service lifecycle. 
The diagram on the left shows the lifecycle when the service is created with `startService()` 
and the diagram on the right shows the lifecycle when the service is created with `bindService()`.

![Service Lifecycle](https://www.tutlane.com/images/android/android_services_started_bound_services_life_cycle.png)

<h3> startService() </h3>

A service is started when an application component calls `startService()` method. Once started, a service can
run in the background indefinitely, even if the component which is responsible for the start is destroyed.
Two option are available to stop the execution of service:
• By calling `stopService()` method,
• The service can stop itself by calling `stopSelf()` method.

<h3> bindService() </h3>
A service is bound when an application component binds to it by calling `bindService()`. Bound service offers
a client-server interface that allows components to interact with the service, send requests and, get results.
It processes across inter-process communication (IPC). A bound service runs only as long as another
application component is bound to it. Multiple components can bind to the service at once, but when all of
them unbind, the service is destroyed. The client can unbind the service by calling
the unbindService() method.
Components which can bind to Service are Activity, Service and Content Provider and we cannot use a
broadcast receiver to connect to a service.

Skeleton code for service
---
```java
public class ExampleService extends Service {
    int startMode;       // indicates how to behave if the service is killed
    IBinder binder;      // interface for clients that bind
    boolean allowRebind; // indicates whether onRebind should be used

    @Override
    public void onCreate() {
        // The service is being created
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        return startMode;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        return binder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return allowRebind;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }
    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    }
}
```
onCreate( )
---
The system calls this method when the service is created first using `onStartCommand()` or `onBind()`. It is typically used to
initialize the service. E.g. allocate binder objects to handle local communication.

onStartCommand()
---
The Android service calls this method when a component (eg: Activity) requests to start a service using `startService()`.
When this method executes, the service is started and can run in the background indefinitely. 
If you implement this, it is your responsibility to stop the service when its work is completed by calling `stopSelf()` or `stopService()`.

<h3> Return values of onStartCommand() </h3>

- START_STICKY (will restart if the android system terminates for any reason)
  - The typical scenario is a music service which is constantly running a music audio. When it gets killed it will restart when the resources are available, but with the null intent, so that it may not have to remember which song was playing earlier,
you don't want your music service to automatically start blasting music in your ears in your unexpected time because it
got killed some quiet time back.
- START_NOT_STICKY (will run till it has pending works. If android stops services forcefully, it will not restart services till user start services)
  - Alarm services are a kind of service which is periodically will do some kind of backend job, like polling the
data from the server every ten minutes or 20 minutes. So during that time if it gets killed there is no need to
restart it because it is always going to be once again triggered after 10 or 20 minutes. So tasks which need
to be run in the background periodically are the best examples of START_NOT_STICKY services.
- START_REDELIVER_INTENT (will restart the service after the crash and also redeliver the intents that were present at the time of crash.)
  - A typical example is file download. If the file download service gets killed somewhere in between
because of the resource crunch and when later resources become available the file download
should continue from there it had been stopped.

onBind()
---
This method is required to implement in android service even if you are not using and it is invoked whenever an application component calls the
`bindService()` method in order to bind itself with a service.
You can `throw new UnsupportedOperationException("Not yet implemented");` in `onBind()` method if you are not using it.
To implement `onBind()`, you must provide an interface that clients use in order to communicate with the service. It returns a
reference to a binder object(Ibinder) to the client for later use. If you don’t want to allow binding, then return null.

onUnbind()
---
The Android system invokes this method when all the clients get disconnected from a particular service interface.

onRebind()
---
Once all clients are disconnected from the particular interface of service and there is a need to connect the service with
new clients, the system calls this method.

onDestroy()
---
Called by the system to notify a Service that it is no longer used and is being removed. The service should clean up any resources it holds (threads, registered receivers, etc) at this point.
Upon return, there will be no more calls in to this Service object and it is effectively dead. Do not call this method directly.

Additional notes
---
Although there are started and bound services separately, service can work both ways — it can be started (to run indefinitely) and also allow binding. It's simply a matter of whether you implement a couple of callback methods: onStartCommand() to allow components to start it and onBind() to allow binding. 
