package com.example.mymovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mymovies.R;
import com.example.mymovies.models.Favorites;
import com.example.mymovies.models.HTTPClient;
import com.example.mymovies.models.Movie;
import com.example.mymovies.utils.Tools;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QuerySnapshot;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class that manages the the movie detail flow and
 * displays information about a single movie.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private final static String API_KEY = "9209cdbe";
    private final static String URL = "https://omdbapi.com/?";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initToolbar();
        submitRequest();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.search:
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;

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
     * A function that submits a new request for a single
     * movie from the API.
     */
    private void submitRequest() {
        String query = getQueryString();
        HTTPClient client = new HTTPClient(this, query);

        client.getResponse(new HTTPClient.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                updateUI(result);
            }
        });
    }

    /**
     * A function that builds the query string with parameters
     * to be used as the endpoint for the API request.
     * @return API endpoint
     */
    private String getQueryString() {
        String id = getIntent().getStringExtra("ID");
        return URL + "apikey=" + API_KEY + "&i=" + id;
    }

    /**
     * A function that updates the view with information that
     * is returned from the API.
     * @param movie JSONObject that is returned from the API
     */
    private void updateUI(final JSONObject movie) {
        TextView genre = findViewById(R.id.genre);
        TextView plot = findViewById(R.id.plot);
        TextView rated = findViewById(R.id.rated);
        TextView rating = findViewById(R.id.rating);
        ImageView poster = findViewById(R.id.poster);
        ImageButton favoriteButton = findViewById(R.id.favorite);

        try {
            toolbar.setTitle(Tools.truncate(movie.getString("Title"), 23));
            plot.setText(movie.getString("Plot"));
            rated.setText("Rated " + movie.getString("Rated"));
            rating.setText(movie.getString("imdbRating"));
            genre.setText(Tools.truncate(movie.getString("Genre"), 35));

            if (movie.getString("Poster") != "") {
                UrlImageViewHelper.setUrlDrawable(poster, movie.getString("Poster"));
            }

        } catch (JSONException e) {}

        // If user clicks hear icon, save the movie to the user's favorites.
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovieToFavorites(movie);
            }
        });
    }

    /**
     * A function that saves a movie to the user's favorites
     * on Firebase.
     * @param obj movie object to be saved
     */
    private void addMovieToFavorites(JSONObject obj) {
        try {
            final Movie movie = new Movie(obj.getString("imdbID"));
            movie.setYear(obj.getString("Year"));
            movie.setTitle(obj.getString("Title"));
            movie.setPoster(obj.getString("Poster"));

            Favorites favorites = new Favorites();
            favorites.addToFavorites(movie, new Favorites.FavoriteCallback() {
                @Override
                public void onSuccess() {
                    Snackbar.make(findViewById(R.id.parent),
                            "Added \"" + movie.getTitle() + "\" to favorites.",
                            Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String reason) {
                    Snackbar.make(findViewById(R.id.parent), reason,
                            Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete(QuerySnapshot snapshot) {}
            });
        } catch (JSONException e) {}
    }
}
