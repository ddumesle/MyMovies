package com.example.mymovies.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.example.mymovies.R;
import com.example.mymovies.models.Favorites;
import com.example.mymovies.models.Movie;
import com.example.mymovies.models.MovieGridAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A class the manages the overall flow of the
 * entire application.
 */
public class MainActivity extends AppCompatActivity {

    private Favorites favorites;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    private static final String KEY_YEAR = "Year";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_POSTER = "Poster";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        checkCredentials();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
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

            case R.id.logout:
                mAuth.signOut();
                intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                return true;

            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * A function that checks whether a user is currently
     * authenticated with FirebaseAuth.
     */
    private void checkCredentials() {
        if (currentUser != null) {
            favorites = new Favorites();
            updateUI();
        } else {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        }
    }

    /**
     * A function that updates the view with information
     * that is saved from the user's favorites in Firebase.
     */
    private void updateUI() {
        favorites.getFavorites(new Favorites.FavoriteCallback() {
            @Override
            public void onFailure(String reason) {
                Snackbar.make(findViewById(R.id.parent), reason,
                        Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(QuerySnapshot snapshot) {
                List<Movie> movies = new ArrayList<>();

                for (QueryDocumentSnapshot documentSnapshot : snapshot) {
                    Movie movie = new Movie(documentSnapshot.getId());
                    movie.setYear(documentSnapshot.getString(KEY_YEAR));
                    movie.setTitle(documentSnapshot.getString(KEY_TITLE));
                    movie.setPoster(documentSnapshot.getString(KEY_POSTER));
                    movies.add(movie);
                }

                initComponent(movies);
            }

            @Override
            public void onSuccess() {}
        });

    }

    /**
     * A function that initializes the movie adapter to bind
     * the view and the movie model.
     * @param movies
     */
    private void initComponent(List movies) {
        MovieGridAdapter adapter = new MovieGridAdapter(movies);
        GridView gridView = findViewById(R.id.gridview);
        gridView.setAdapter(adapter);

        // Set a listener for each movie that is returned from the DB
        adapter.setOnItemClickListener(new MovieGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final Movie movie, String action) {

                // If user clicks the trash icon, delete movie from DB
                if (action.equals("delete")) {
                    favorites.deleteFavorite(movie.getImdbID(), new Favorites.FavoriteCallback() {
                        @Override
                        public void onSuccess() {
                            Snackbar.make(findViewById(R.id.parent),
                                    "Deleted \"" + movie.getTitle() + "\" from favorites.",
                                    Snackbar.LENGTH_SHORT).show();
                            updateUI();
                        }

                        @Override
                        public void onFailure(String reason) {
                            Snackbar.make(findViewById(R.id.parent), reason,
                                    Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete(QuerySnapshot snapshot) {}
                    });
                }

                // If user clicks the movie poster, start a new activity to show details
                if (action.equals("detail")) {
                    Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                    intent.putExtra("ID", movie.getImdbID());
                    startActivity(intent);
                }
            }
        });
    }
}
