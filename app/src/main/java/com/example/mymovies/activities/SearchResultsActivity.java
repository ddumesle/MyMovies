package com.example.mymovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.models.HTTPClient;
import com.example.mymovies.R;
import com.example.mymovies.models.Movie;
import com.example.mymovies.models.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

        client.getResponse(new HTTPClient.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                formatResponse(result);
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

    private void formatResponse(JSONObject result) {
        List<Movie> movies = new ArrayList<>();

        try {
            JSONArray arr = result.getJSONArray("Search");

            for (int i = 0 ; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);

                Movie movie = new Movie(obj.getString("imdbID"));
                movie.setYear(obj.getString("Year"));
                movie.setTitle(obj.getString("Title"));
                movie.setPoster(obj.getString("Poster"));
                movies.add(movie);
            }
        } catch (JSONException e) {
            Log.d("MYMOVIES", e.getMessage());
        }

        initComponent(movies);
    }

    private void initComponent(List movies) {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        MovieAdapter adapter = new MovieAdapter(movies);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Movie movie, String action) {
                if (action.equals("favorite")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                if (action.equals("details")) {
                    // Intent to movie detail
                }
            }
        });
    }
}
