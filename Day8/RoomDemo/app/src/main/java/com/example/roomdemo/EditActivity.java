package com.example.roomdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    EditText edtName, edtPhoneNumber;
    Button btnSave;
    int mPersonId;
    Intent intent;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        if (intent != null && intent.hasExtra("id")) {
            btnSave.setText("Update");
            edtName.setText(intent.getStringExtra("name"));
            edtPhoneNumber.setText(intent.getStringExtra("number"));
        }
    }

    private void init() {
        edtName = findViewById(R.id.edtName);
        edtPhoneNumber = findViewById(R.id.edtNumber);
        btnSave = findViewById(R.id.btnSave);
        intent = getIntent();
        mDb = AppDatabase.getInstance(getApplicationContext());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveBtnClicked();
            }
        });
    }

    private void onSaveBtnClicked() {
        final PersonData personData = new PersonData(edtName.getText().toString(), edtPhoneNumber.getText().toString());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (intent != null && !intent.hasExtra("id")) {
                    mDb.personDao().insertPerson(personData);
                } else {
                    personData.setId(intent.getIntExtra("id",0));
                    mDb.personDao().updatePerson(personData);
                }
                finish();
            }
        });
    }
}