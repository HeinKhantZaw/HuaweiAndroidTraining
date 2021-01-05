package com.example.recyclerviewexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createItemList();
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void createItemList() {
        ArrayList<ItemList> itemList = new ArrayList<>();
        itemList.add(new ItemList("help icon", android.R.drawable.ic_menu_help));
        itemList.add(new ItemList("email icon", android.R.drawable.ic_dialog_email));
        itemList.add(new ItemList("map icon", android.R.drawable.ic_dialog_map));
        itemList.add(new ItemList("alarm icon", android.R.drawable.ic_lock_idle_alarm));
        itemList.add(new ItemList("search icon", android.R.drawable.ic_search_category_default));
        itemList.add(new ItemList("lock icon", android.R.drawable.ic_lock_idle_lock));
        itemList.add(new ItemList("zoom icon", android.R.drawable.ic_menu_zoom));
        itemList.add(new ItemList("direction icon", android.R.drawable.ic_menu_directions));
    }
}