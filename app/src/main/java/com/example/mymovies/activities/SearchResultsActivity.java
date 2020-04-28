package com.example.mymovies.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.models.HTTPClient;
import com.example.mymovies.R;
import com.example.mymovies.models.MovieAdapter;
import com.example.mymovies.models.VolleyCallback;

import org.json.JSONObject;

import java.util.List;


public class SearchResultsActivity extends AppCompatActivity {

    private final static String API_KEY = "9209cdbe";
    private final static String URL = "https://omdbapi.com/?";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        submitRequest();
    }

    private void submitRequest() {
        String query = getQueryString();
        HTTPClient client = new HTTPClient(this, query);

        client.getResponse(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                updateUI(result);
            }
        });
    }

    private String getQueryString() {
        String keyword = getIntent().getStringExtra("KEYWORD");
        String type = getIntent().getStringExtra("TYPE");
        String year = getIntent().getStringExtra("YEAR");
        String result = URL + "apikey=" + API_KEY + "&s=" + keyword;

        if (type != null && !type.isEmpty()) {
            result += "&type=" + type;
        }

        if (year != null && !year.isEmpty()) {
            result += "&y=" + year;
        }

        return result;
    }

    private void updateUI(JSONObject result) {

    }

    private void initComponent(List movies) {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        MovieAdapter adapter = new MovieAdapter(this, movies);
        recyclerView.setAdapter(adapter);
    }
}
