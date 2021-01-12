package com.example.newsapi_demo.rest;

import com.example.newsapi_demo.model.DataModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API_Interface {
    @GET("top-headlines")
    Call<DataModel> getLatestNews(@Query("country") String country, @Query("category") String category, @Query("apiKey") String apiKey);
}
