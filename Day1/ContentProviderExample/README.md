Android Permission
==================

An android app demo that showcases the android permission. There's only one activity. 
You can check <uses-permission> tag in AndroidManifest.xml. 
In this demo, [android.permission.READ_CALENDAR](https://developer.android.com/reference/android/Manifest.permission#READ_CALENDAR) and
[android.permission.WRITE_CALENDAR](https://developer.android.com/reference/android/Manifest.permission#WRITE_CALENDAR) are added in manifest file. 

Introduction
===
App permissions help support user privacy by protecting access to the following:
- Restricted data, such as system state and a user's contact information.
- Restricted actions, such as connecting to a paired device and recording audio.

Permissions are granted by the user when the application is installed (on devices running Android 5.1 and lower) or
while the app is running (on devices running Android 6.0 and higher).

Types of permissions
===
### Install-time permissions

When you declare install-time permissions in your app, the system automatically grants your app the permissions when the user installs your app.

### Runtime permissions

Runtime permissions, also known as dangerous permissions, give your app additional access to restricted data, and
they allow your app to perform restricted actions that more substantially affect the system and other apps.
Therefore, you need to request runtime permissions in your app before you can access the restricted data or perform restricted actions. 
When your app requests a runtime permission, the system presents a runtime permission prompt.

### Special permissions

Special permissions correspond to particular app operations. Only the platform and OEMs can define special permissions. 
Additionally, the platform and OEMs usually define special permissions when they want to protect access to particularly powerful actions, such as drawing over other apps.

About this repo
===
When you run this app, the system will show a a runtime permission prompt for user to grant the permission to access and add calendar events.
If you've allowed the permission, you will see the toast ("You've allowed Permission") when you open the app for the next time.
The method ,that check if the permission is granted or not, is written in MainActiviy.java.
```java
 if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            String[] strings = {
                    Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
            };
            ActivityCompat.requestPermissions(this, strings, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You've allowed Permission", Toast.LENGTH_LONG).show();
        }
```
You can find more about permission in this [sample project](https://github.com/android/permissions-samples.git). 
