package com.example.contentproviderdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    TextView tvQueryResult;
    String[] contactProjections = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvQueryResult = findViewById(R.id.tvQueryResult);
        checkPermission();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            String[] strings = {Manifest.permission.READ_CONTACTS};
            ActivityCompat.requestPermissions(this, strings, 1);
        } else {
            Toast.makeText(this, "You've allowed read contact Permission", Toast.LENGTH_SHORT).show();
            loadContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allowed = true;
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            for (int res : grantResults) {
                allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
            }
        } else {
            allowed = false;
        }

        if (allowed) {
            loadContacts();
        } else {
            Toast.makeText(this, "You need to enable permission first", Toast.LENGTH_SHORT).show();
        }

    }
    private void loadContacts() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                contactProjections, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder queryResults = new StringBuilder();
            while (cursor.moveToNext()) {
                String id = cursor.getString(0); // ContactsContract.Contacts._ID
                String name = cursor.getString(1); // ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
                String hasPhoneNumber = cursor.getString(2); //ContactsContract.Contacts.HAS_PHONE_NUMBER
                String phoneNumber = null;
                if (Integer.parseInt(hasPhoneNumber) > 0)
                {
                    //This search the phone number according to Contact ID
                    //The query used to search phone number is ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id (id is the string variable declared above)
                    Cursor phones = getContentResolver()
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        int phoneNumberID = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER); //get the id of phone number
                        phoneNumber = phones.getString(phoneNumberID); //get the phone number
                    }
                    phones.close();
                }
                queryResults.append(name + " , " + phoneNumber + " , " + hasPhoneNumber + "\n");
            }
            tvQueryResult.setText(queryResults.toString());
        } else {
            tvQueryResult.setText(R.string.noContactStr);
        }
    }

}