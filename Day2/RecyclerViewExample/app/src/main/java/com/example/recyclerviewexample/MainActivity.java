package com.example.recyclerviewexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ItemList[] itemList = initArray();

        recyclerView = findViewById(R.id.recyclerView);
        CustomAdapter adapter = new CustomAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private ItemList[] initArray() {
        return new ItemList[]{
                    new ItemList("help icon", android.R.drawable.ic_menu_help),
                    new ItemList("email icon", android.R.drawable.ic_dialog_email),
                    new ItemList("map icon", android.R.drawable.ic_dialog_map),
                    new ItemList("alarm icon", android.R.drawable.ic_lock_idle_alarm),
                    new ItemList("lock icon", android.R.drawable.ic_lock_idle_lock),
                    new ItemList("search icon", android.R.drawable.ic_search_category_default),
                    new ItemList("zoom icon", android.R.drawable.ic_menu_zoom),
                    new ItemList("direction icon", android.R.drawable.ic_menu_directions),
                    new ItemList("lock icon", android.R.drawable.ic_lock_idle_lock),
                    new ItemList("search icon", android.R.drawable.ic_search_category_default),
                    new ItemList("zoom icon", android.R.drawable.ic_menu_zoom),
                    new ItemList("map icon", android.R.drawable.ic_dialog_map),
                    new ItemList("alarm icon", android.R.drawable.ic_lock_idle_alarm),
                    new ItemList("direction icon", android.R.drawable.ic_menu_directions),
                    new ItemList("help icon", android.R.drawable.ic_menu_help),
                    new ItemList("email icon", android.R.drawable.ic_dialog_email),
                    new ItemList("map icon", android.R.drawable.ic_dialog_map),
                    new ItemList("alarm icon", android.R.drawable.ic_lock_idle_alarm),
                    new ItemList("direction icon", android.R.drawable.ic_menu_directions)
            };
    }
}