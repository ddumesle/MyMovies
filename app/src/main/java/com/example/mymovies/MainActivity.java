package com.example.mymovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private final static int SPLASH = 0;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCredentials();
    }

    /**
     * A function that checks whether a user is logged-in
     * with valid credentials. I
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
        // Recycler View
    }
}
