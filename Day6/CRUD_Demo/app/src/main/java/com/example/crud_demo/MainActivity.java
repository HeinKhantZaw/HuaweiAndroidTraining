package com.example.crud_demo;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLoad, btnAdd, btnRemove, btnUpdate;
    TextView tvContacts;
    EditText edtContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkPermission();
    }

    private void initView() {
        btnLoad = findViewById(R.id.btnLoad);
        btnAdd = findViewById(R.id.btnAdd);
        btnRemove = findViewById(R.id.btnRemove);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvContacts = findViewById(R.id.tvContact);
        edtContacts = findViewById(R.id.edtContact);

        btnLoad.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            String[] strings = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoad:
                loadContacts();
                break;
            case R.id.btnAdd:
                addContact();
                break;
            case R.id.btnRemove:
                removeContact();
                break;
            case R.id.btnUpdate:
                updateContact();
                break;
        }
    }

    private void updateContact() {
        String[] updateValue = edtContacts.getText().toString().split(" ");

        String targetString = null, newString = null;
        if (updateValue.length == 2) {
            targetString = updateValue[0];
            newString = updateValue[1];

            String where = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " = ? ";
            String[] param = {targetString};

            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, newString);
            contentResolver.update(ContactsContract.RawContacts.CONTENT_URI, contentValues, where, param);
            loadContacts();
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeContact() {
        String whereClause = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " = '" + edtContacts.getText().toString() + "'";
        getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, whereClause, null);
        loadContacts();
        Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
    }

    private void addContact() {
        ArrayList<ContentProviderOperation> cpo = new ArrayList<>();

        cpo.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "account@gmaill.com")
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "accountName").build());

        cpo.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, edtContacts.getText().toString()).build());
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, cpo);
            loadContacts();
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadContacts() {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder queryResults = new StringBuilder();
            while (cursor.moveToNext()) {

                int idCol = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int nameCol = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
                int phoneNumCol = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

                String id = cursor.getString(idCol); // ContactsContract.Contacts._ID
                String name = cursor.getString(nameCol); // ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
                String hasPhoneNumber = cursor.getString(phoneNumCol); //ContactsContract.Contacts.HAS_PHONE_NUMBER
                String phoneNumber = null;
                if (Integer.parseInt(hasPhoneNumber) > 0) {
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
            tvContacts.setText(queryResults.toString());
        } else {
            tvContacts.setText("No contact");
        }
    }
}