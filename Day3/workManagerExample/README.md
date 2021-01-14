Android WorkManager
===
WorkManager is an API that makes it easy to schedule deferrable, asynchronous tasks that are expected to run even if the app exits or the device restarts. 
So, it is basically a task scheduler. The WorkManager API is a suitable and recommended replacement for all previous Android background scheduling APIs, 
including FirebaseJobDispatcher, GcmNetworkManager, and Job Scheduler.

WorkManager Features
---
- It is fully backward compatible so in your code you do not need to write if-else for checking the android version.
- With WorkManager we can check the status of the work. 
- Tasks can be chained as well, for example when one task is finished it can start another. See the example below.
```java
WorkManager.getInstance(...)
    .beginWith(Arrays.asList(workA, workB))
    .then(workC)
    .enqueue();
```
- Provides guaranteed execution with the constraints, we have many constrained available that we will see ahead.
- Declaratively define the optimal conditions for your work to run using Work Constraints. 
(For example, run the work only when the device is Wi-Fi, when the device idle, or when it has sufficient storage space, etc.)
- WorkManager allows you to schedule work to run one- time or repeatedly using flexible scheduling windows. 
Work can be tagged and named as well, allowing you to schedule unique, replaceable work and monitor or cancel groups of work together.

When to use WorkManager
---
Android WorkManager can be a perfect background processing library to use when your task –
1. Does not need to run at a specific time
2. Can be deferred to be executed
3. Is guaranteed to run even after the app is killed or device is restarted
4. Has to meet constraints like battery supply or network availability before execution

Getting started with WorkManager
---

### Add dependencies
Before you start using WorkManager, you need import the library into your Android project first.
Add the following dependencies to your app's build.gradle file:

```gradle
dependencies {
  def work_version = "2.4.0"

    // For Java 
    implementation "androidx.work:work-runtime:$work_version"

    // For Kotlin + coroutines
    implementation "androidx.work:work-runtime-ktx:$work_version"

    // optional - RxJava2 support
    implementation "androidx.work:work-rxjava2:$work_version"

    // optional - GCMNetworkManager support
    implementation "androidx.work:work-gcm:$work_version"

    // optional - Test helpers
    androidTestImplementation "androidx.work:work-testing:$work_version"
  }
```
### Define the work
Work is defined using the Worker class. 
The doWork() method runs asynchronously on a background thread provided by WorkManager.
To create some work for WorkManager to run, extend the Worker class and override the doWork() method.
For example, to create a Worker that uploads images, you can do the following:
```java

public class UploadWorker extends Worker {
   public UploadWorker(
       @NonNull Context context,
       @NonNull WorkerParameters params) {
       super(context, params);
   }

   @Override
   public Result doWork() {

     // Do the work here--in this case, upload the images.
     uploadImages();

     // Indicate whether the work finished successfully with the Result
     return Result.success();
   }
}

```
The Result returned from doWork() informs the WorkManager service whether the work succeeded and, in the case of failure, whether or not the work should be retried.

* Result.success(): The work finished successfully.
* Result.failure(): The work failed.
* Result.retry(): The work failed and should be tried at another time according to its retry policy.

### Create a WorkRequest
Once your work is defined, it must be scheduled with the WorkManager service in order to run. WorkManager offers a lot of flexibility in how you schedule your work. You can schedule it to run periodically over an interval of time, or you can schedule it to run only one time.

However you choose to schedule the work, you will always use a WorkRequest. While a Worker defines the unit of work, a WorkRequest (and its subclasses) define how and when it should be run. In the simplest case, you can use a OneTimeWorkRequest, as shown in the following example.
```java

WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class).build();

```
### Submit the WorkRequest to the system
Finally, you need to submit your WorkRequest to WorkManager using the enqueue() method.
```java

WorkManager.getInstance(myContext)
           .enqueue(uploadWorkRequest);

```
More about WorkRequests
---
Work is defined in WorkManager via a WorkRequest. In order to schedule any work with WorkManager you must first create a WorkRequest object and then enqueue it.
```java

WorkRequest myWorkRequest = ... //Implementation for WorkRequest is explained below as there are two different types of WorkRequests 
WorkManager.getInstance(myContext).enqueue(myWorkRequest);

```

WorkRequest itself is an abstract base class. 
There are two derived implementations of this class that you can use to create the request, `OneTimeWorkRequest` and `PeriodicWorkRequest`.

## OneTimeWorkRequest
For simple work, which requires no additional configuration, use the static method `from()`:
```java
WorkRequest myWorkRequest = OneTimeWorkRequest.from(MyWork.class);
```
For more complex work, you can use a builder.
```java
WorkRequest uploadWorkRequest =
   new OneTimeWorkRequest.Builder(MyWork.class)
       // Additional configuration
       .build();
```
## PeriodicWorkRequest
Your app may at times require that certain work runs periodically.
For example, you may want to periodically backup your data, download fresh content in your app, or upload logs to a server.
Here is how you use the PeriodicWorkRequest to create a WorkRequest object which executes periodically:
```java

PeriodicWorkRequest saveRequest =
       new PeriodicWorkRequest.Builder(SaveImageToFileWorker.class, 1, TimeUnit.HOURS)
           // You can add constraints here (WorkConstraints is explained below)
           .build();

```
### Flexible run intervals
If the nature of your work makes it sensitive to run timing, you can configure your PeriodicWorkRequest to run within a flex period inside each interval period, 
as shown below.
![flexible run intervals](https://developer.android.com/images/topic/libraries/architecture/workmanager/how-to/definework-flex-period.png)
The following is an example of periodic work that can run during the last 15 minutes of every one hour period.
```java

WorkRequest saveRequest =
       new PeriodicWorkRequest.Builder(SaveImageToFileWorker.class,
               1, TimeUnit.HOURS,
               15, TimeUnit.MINUTES)
           .build();

```
## WorkConstraints 

Constraints ensure that work is deferred until optimal conditions are met. The following constraints are available to WorkManager.
- **NetworkType**:	Constrains the type of network required for your work to run. For example, Wi-Fi (UNMETERED).
- **BatteryNotLow**:	When set to true, your work will not run if the device is in low battery mode.
- **RequiresCharging**:	When set to true, your work will only run when the device is charging.
- **DeviceIdle**: When set to true, this requires the user’s device to be idle before the work will run. This can be useful for running batched operations that might otherwise have a negative performance impact on other apps running actively on the user’s device.
- **StorageNotLow**:	When set to true, your work will not run if the user’s storage space on the device is too low.

To create a set of constraints and associate it with some work, create a Constraints instance using the Contraints.Builder() and assign it to your WorkRequest.Builder().
For example, the following code builds a work request which only runs when the user’s device is both charging and on Wi-Fi:
```java

Constraints constraints = new Constraints.Builder()
       .setRequiredNetworkType(NetworkType.UNMETERED)
       .setRequiresCharging(true)
       .build();

WorkRequest myWorkRequest =
       new OneTimeWorkRequest.Builder(MyWork.class)
               .setConstraints(constraints) 
               .build(); // Will run when the device is both charging and on Wi-Fi

```
When multiple constraints are specified, your work will run only when all the constraints are met.

## Delayed Work

In the event that your work has no constraints or that all the constraints are met when your work is enqueued, the system may choose to run the work immediately. If you do not want the work to be run immediately, you can specify your work to start after a minimum initial delay.

Here is an example of how to set your work to run at least 10 minutes after it has been enqueued.
```java

WorkRequest myWorkRequest =
      new OneTimeWorkRequest.Builder(MyWork.class)
               .setInitialDelay(10, TimeUnit.MINUTES)
               .build(); // will run after 10 minutes after enqueued

```
While the example illustrates how to set an initial delay for a `OneTimeWorkRequest`, you can also set an initial delay for a `PeriodicWorkRequest`. In that case, only the first run of your periodic work would be delayed.

Code Explanation for this project
====
Now I am going to explain what's inside this demo app.<br/>
In this demo app, we will create a layout. This layout will contain TextView and Button. After that, we will set
onClickListener() and this event will enqueue the WorkRequest to WorkManager and shows the status on TextView.

### Adding dependency to build.gradle

First, go to app level build.gradle file and we add the following implementation line.

```gradle
implementation 'androidx.work:work-runtime:2.4.0'
```
### Concept of WorkerManager class

* Worker: The main class where we will put the work that needs to be done.
* WorkRequest: It defines an individual task, like it will define which worker class should execute the task.
* WorkManager: The class used to enqueue the work requests.

### Create Worker class
Create a class called NotificationWorker.java. As we've seen above, Work is defined using the Worker class. So, we need to extend Worker in NotificationWorker.java to make it Worker class.

```java
public class NotificationWorker extends Worker {
    private static final String WORK_RESULT = "work result";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
    
        showNotification("workManager","Message has been sent");
        Data outputData = new Data.Builder().putString(WORK_RESULT,"Jobs Finished").build();
        return Result.success(outputData);
    }

    private void showNotification(String task, String description) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "task_channel";
        String channelName = "task_name";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channelId)
                .setContentTitle(task)
                .setContentText(description)
                .setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(1,builder.build());
    }
}
```

We have to add a work in `doWork()` method. In this app, the work is to show notification, and we indicate whether the work finished successfully or not with `Result.success()` method.

<TO BE CONTINUED>

