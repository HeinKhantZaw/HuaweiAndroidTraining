package com.example.sqlitedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddCountryActivity extends Activity {

    private Button addBtn;
    private EditText subjectEditText;
    private EditText descEditText;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Record");
        setContentView(R.layout.activity_add_record);

        init();

        dbManager = new DBManager(this);
        dbManager.open();
    }

    private void init() {
        subjectEditText = findViewById(R.id.countryName_editText);
        descEditText = findViewById(R.id.currency_editText);
        addBtn = findViewById(R.id.add_record);
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = subjectEditText.getText().toString();
                final String desc = descEditText.getText().toString();

                dbManager.insert(name, desc);

                Intent main = new Intent(AddCountryActivity.this, CountryListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
            }
        });
    }
}