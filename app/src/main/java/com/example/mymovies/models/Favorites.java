package com.example.mymovies.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Favorites {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    public Favorites() {

    }

    public boolean addToFavorites(Movie movie) {
        return false;
    }

    public void getFavorites() {

    }
}
