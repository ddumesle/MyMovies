package com.example.mymovies;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SearchResultsActivity extends AppCompatActivity {

    private final static String API_KEY = "9209cdbe";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        submitRequest();
    }

    private void submitRequest() {
        String query = getQueryString();
    }

    private String getQueryString() {
        String result = getIntent().getStringExtra("KEYWORD");
        String type = getIntent().getStringExtra("TYPE");
        String year = getIntent().getStringExtra("YEAR");

        return result;
    }
}
