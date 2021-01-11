package com.example.contentproviderdemowithloaderfeature;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    Button btnLoad;
    TextView tvContacts;
    String[] contactProjections = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
    };
    boolean firstTimeLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnLoad = findViewById(R.id.btnLoad);
        tvContacts = findViewById(R.id.tvQueryResult);
        btnLoad.setOnClickListener(this);
        checkPermission();
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            String[] strings = {Manifest.permission.READ_CONTACTS};
            ActivityCompat.requestPermissions(this, strings, 1);
        } else {
            Toast.makeText(this, "You've allowed read contact Permission", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == 1) {
            return new CursorLoader(MainActivity.this, ContactsContract.Contacts.CONTENT_URI, contactProjections, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {
            StringBuilder stringBuilder = new StringBuilder("");
            while (data.moveToNext()) {
                stringBuilder.append(data.getString(0) + " , " + data.getString(1) + " , " + data.getString(2)+"\n");
            }
            tvContacts.setText(stringBuilder.toString());
        } else {
            tvContacts.setText("No contacts in device");
        }
        data.close();
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoad:
                if (!firstTimeLoaded) {
                    getLoaderManager().initLoader(1, null, this);
                    firstTimeLoaded = true;
                } else {
                    getLoaderManager().restartLoader(1, null, this);
                }
                break;
            default:
                break;
        }
    }
}