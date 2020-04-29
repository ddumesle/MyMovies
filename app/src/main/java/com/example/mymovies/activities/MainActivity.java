package com.example.mymovies.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mymovies.R;
import com.example.mymovies.models.Favorites;
import com.example.mymovies.models.Movie;
import com.example.mymovies.models.MovieAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity {

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
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else {
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
            updateUI();
        } else {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        }
    }

    private void updateUI() {
        final LinearLayout progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        Favorites favoritesDB = new Favorites();
        favoritesDB.getFavorites(new Favorites.FavoriteCallback() {
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
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess() {}
        });

    }

    private void initComponent(List movies) {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        MovieAdapter adapter = new MovieAdapter(movies, "grid");
        recyclerView.setAdapter(adapter);
    }
}
