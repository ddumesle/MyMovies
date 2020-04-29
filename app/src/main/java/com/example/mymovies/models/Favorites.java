package com.example.mymovies.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Favorites {

    private CollectionReference col;

    private static final String KEY_YEAR = "Year";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_POSTER = "Poster";

    public Favorites() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        col = db.collection("/users/" + UID + "/favorites");
    }

    public void addToFavorites(Movie movie, final FavoriteCallback callback) {
        // Create a new user with a first and last name
        Map<String, Object> favorite = new HashMap<>();
        favorite.put(KEY_YEAR, movie.getYear());
        favorite.put(KEY_TITLE, movie.getTitle());
        favorite.put(KEY_POSTER, movie.getPoster());

        // Add a new collection with a generated ID
        col.document(movie.getImdbID()).set(favorite)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }

    public void getFavorites(final FavoriteCallback callback) {
        col.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            callback.onComplete(task.getResult());
                        } else {
                            callback.onFailure("Request failed.");
                        }
                    }
                });
    }

    public void deleteFavorite(String id, final FavoriteCallback callback) {
        col.document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e.getMessage());
            }
        });
    }

    public interface FavoriteCallback {
        void onSuccess();
        void onFailure(String reason);
        void onComplete(QuerySnapshot snapshot);
    }
}
