package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.newsapp.Models.ApiClient;
import com.example.newsapp.Models.Articles;
import com.example.newsapp.Models.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    final String API_KEY = "f8ab1525f3a04ea28c612f92d281a8f4";
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String country = getCountry();
        retrieveJSON(country, API_KEY);

    }
    public void retrieveJSON(String country , String apiKey ){
    Call<Headlines> call = ApiClient.getInstance().getApi().getHeadlines(country,apiKey);
    call.enqueue(new Callback<Headlines>() {
        @Override
        public void onResponse(Call<Headlines> call, Response<Headlines> response) {
            if(response.isSuccessful() && response.body().getArticles() != null){
                articles.clear();
                articles = response.body().getArticles();
                adapter = new Adapter(MainActivity.this , articles);
                recyclerView.setAdapter(adapter);
            }
        }

        @Override
        public void onFailure(Call<Headlines> call, Throwable t) {
            Toast.makeText(MainActivity.this, t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    });
    }
    public String getCountry(){
        Locale locale = Locale.getDefault();
        String Country = locale.getCountry();
        return Country.toLowerCase();

    }
}
