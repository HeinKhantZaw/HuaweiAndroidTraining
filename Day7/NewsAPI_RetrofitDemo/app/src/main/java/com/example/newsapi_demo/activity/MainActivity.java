package com.example.newsapi_demo.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapi_demo.R;
import com.example.newsapi_demo.adapter.CustomAdapter;
import com.example.newsapi_demo.model.Article;
import com.example.newsapi_demo.model.DataModel;
import com.example.newsapi_demo.rest.API_Client;
import com.example.newsapi_demo.rest.API_Interface;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "ADD YOUR OWN API KEY!!"; // you can get your api key here https://newsapi.org/

    private static final String BUSINESS_CATEGORY = "business";
    private static final String TECHNOLOGY_CATEGORY = "technology";
    private static final String HEALTH_CATEGORY = "health";
    private static final String GENERAL_CATEGORY = "general";
    private static final String SCIENCE_CATEGORY = "science";
    private static final String ENTERTAINMENT_CATEGORY = "entertainment";

    RecyclerView recyclerView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Business:
                apiCall(BUSINESS_CATEGORY);
                break;
            case R.id.Technology:
                apiCall(TECHNOLOGY_CATEGORY);
                break;
            case R.id.Entertainment:
                apiCall(ENTERTAINMENT_CATEGORY);
                break;
            case R.id.Science:
                apiCall(SCIENCE_CATEGORY);
                break;
            case R.id.General:
                apiCall(GENERAL_CATEGORY);
                break;
            case R.id.Health:
                apiCall(HEALTH_CATEGORY);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (API_KEY.equals("ADD YOUR OWN API KEY!!")) {
            Toast.makeText(this, "ADD YOUR OWN API KEY IN MAIN ACTIVITY FIRST", Toast.LENGTH_LONG).show();
        } else {
            recyclerView = findViewById(R.id.recyclerView);
            final LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            apiCall(GENERAL_CATEGORY);
        }

    }

    private void apiCall(String category) {
        API_Interface apiService = API_Client.getClient().create(API_Interface.class);
        Call<DataModel> call = apiService.getLatestNews("US", category, API_KEY);
        call.enqueue(new Callback<DataModel>() {
            @Override
            public void onResponse(@NotNull Call<DataModel> call, Response<DataModel> response) {
                if (response.body().getStatus().equals("ok")) {
                    List<Article> articleList = response.body().getArticles();
                    if (articleList.size() > 0) {
                        final CustomAdapter adapter = new CustomAdapter(articleList);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<DataModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}