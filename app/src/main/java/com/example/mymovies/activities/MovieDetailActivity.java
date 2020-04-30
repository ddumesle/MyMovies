package com.example.mymovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class MovieDetailActivity extends AppCompatActivity {

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Movie Detail");
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

    private String getQueryString() {
        String id = getIntent().getStringExtra("ID");
        return URL + "apikey=" + API_KEY + "&i=" + id;
    }

    private void updateUI(final JSONObject movie) {
        TextView genre = findViewById(R.id.genre);
        TextView plot = findViewById(R.id.plot);
        TextView title = findViewById(R.id.title);
        TextView rating = findViewById(R.id.rating);
        ImageView poster = findViewById(R.id.poster);
        ImageButton favoriteButton = findViewById(R.id.favorite);

        try {
            if (movie.getString("Poster") != "") {
                UrlImageViewHelper.setUrlDrawable(poster, movie.getString("Poster"));
            }

            plot.setText(movie.getString("Plot"));
            genre.setText(movie.getString("Genre"));
            rating.setText(movie.getString("imdbRating"));
            title.setText(Tools.truncate(movie.getString("Title"), 20));
        } catch (JSONException e) {}

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMovieToFavorites(movie);
            }
        });
    }

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
