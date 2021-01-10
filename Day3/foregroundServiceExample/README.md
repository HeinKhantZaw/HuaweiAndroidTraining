Foreground services
===
Foreground services perform operations that are noticeable to the user. It should have the same priority as an active activity and therefore should not be killed
by the Android system, even if the system is low on memory. Foreground services perform operations that are noticeable
to the user.
Each foreground service must show a status bar notification that has a priority of PRIORITY_LOW or higher. That way,
users are actively aware that your app is performing a task in the foreground and is consuming system resources. The
notification cannot be dismissed unless the service is either stopped or removed from the foreground.

Creating a foreground service
---
#### Request permission
Apps that target Android 9 (API level 28) or higher and use foreground services must request the FOREGROUND_SERVICE permission, as shown in the following code snippet. 
This is a normal permission, so the system automatically grants it to the requesting app.
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android" ...>

    <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>

    <application ...>
        ...
    </application>
</manifest>
```
#### Start a foreground service
To request that your service run in the foreground, call `startForeground()`. 
This method takes two parameters: a positive integer that uniquely identifies the notification in the status bar and the Notification object itself.
The notification must have a priority of PRIORITY_LOW or higher.

```java
Intent notificationIntent = new Intent(this, ExampleActivity.class);
PendingIntent pendingIntent =
        PendingIntent.getActivity(this, 0, notificationIntent, 0);

Notification notification =
          new Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
    .setContentTitle(getText(R.string.notification_title))
    .setContentText(getText(R.string.notification_message))
    .setSmallIcon(R.drawable.icon)
    .setContentIntent(pendingIntent)
    .setTicker(getText(R.string.ticker_text))
    .build();

// Notification ID cannot be 0.
startForeground(ONGOING_NOTIFICATION_ID, notification); // Starting foreground service
```
#### Remove a foreground service
To remove the service from the foreground, call `stopForeground()`. 
This method takes a boolean, which indicates whether to remove the status bar notification as well. Note that the service continues to run.
If you stop the service while it's running in the foreground, its notification is removed.
```java
// Stop foreground service and remove the notification.
stopForeground(true);
// Stop the foreground service.
stopSelf();
```
Code Explanation for this project
---

Additional Notes
---
You should only use a foreground service when your app needs to perform a task that is noticeable by the user even when they're not directly interacting with the app.
If the action is of low enough importance that you want to use a minimum-priority notification, create a background task instead.
