package com.example.mymovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.models.Favorites;
import com.example.mymovies.models.HTTPClient;
import com.example.mymovies.R;
import com.example.mymovies.models.Movie;
import com.example.mymovies.models.MovieListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A class the manages the results from a user's query. It
 * displays a list of movies that matches a user's request to
 * the API.
 */
public class SearchResultsActivity extends AppCompatActivity {

    private MovieListAdapter adapter;

    private final static String API_KEY = "9209cdbe";
    private final static String URL = "https://omdbapi.com/?";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        initToolbar();
        submitRequest();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search Results");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_results, menu);

        // Filter movies from the toolbar
        MenuItem searchItem = menu.findItem(R.id.filter);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.home:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

    /**
     * A function that submits a new request for a list of
     * movies from the API.
     */
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

    /**
     * A function that builds the query string with parameters
     * to be used as the endpoint for the API request.
     * @return API endpoint
     */
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

    /**
     * A function that builds a list of movies from the
     * the response of the API to used in the adapter.
     * @param result API response
     */
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
        } catch (JSONException e) {}

        initComponent(movies);
    }

    /**
     * A function that updates the view with information
     * that is formatted from the API's response.
     * @param movies a list of movie objects
     */
    private void initComponent(List movies) {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        adapter = new MovieListAdapter(movies);
        recyclerView.setAdapter(adapter);

        // Set a listener for each movie that is returned from the API
        adapter.setOnItemClickListener(new MovieListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final Movie movie, String action) {

                // If user clicks the heart icon, save the movie to the user's favorites
                if (action.equals("favorite")) {
                    Favorites favoriteDB = new Favorites();
                    favoriteDB.addToFavorites(movie, new Favorites.FavoriteCallback() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(String reason) {
                            Snackbar.make(findViewById(R.id.parent), reason,
                                    Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete(QuerySnapshot snapshot){}
                    });
                }

                // If the user clicks the chevron icon, start a new activity to show details
                if (action.equals("details")) {
                    Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                    intent.putExtra("ID", movie.getImdbID());
                    startActivity(intent);
                }
            }
        });
    }
}
