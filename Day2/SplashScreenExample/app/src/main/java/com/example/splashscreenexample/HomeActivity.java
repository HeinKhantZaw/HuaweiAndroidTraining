package com.example.splashscreenexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    ListView itemList;
    String[] programmingLanguages = {"C", "C++", "C#", "Java", "Python", "Go", "Kotlin", "Ruby", "Scala",
            "PHP", "Javascript", "Ada", "C", "C++", "C#", "Java", "Python", "Go", "Kotlin", "Ruby",
            "Scala", "PHP", "Javascript", "Ada"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        itemList = findViewById(R.id.myListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, programmingLanguages);
        itemList.setAdapter(arrayAdapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String language = itemList.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), language, Toast.LENGTH_SHORT).show();
            }
        });
    }
}