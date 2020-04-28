package com.example.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SearchActivity extends AppCompatActivity {

    private String searchType;
    private String searchYear;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText searchInput = findViewById(R.id.search);

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(searchInput.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * A function that gets the text from the view via
     * onClick and sets the searchType variable to be
     * used as 'type' parameter in the API.
     * @param v activity_search view
     */
    public void setSearchType(View v) {
        Button button = (Button) v;
        searchType = button.getText().toString().toLowerCase();
    }

    /**
     * A function that gets the text from the view via
     * onClick and sets the searchYear variable to be
     * used as 'y' parameter in the API.
     * @param v activity_search view
     */
    public void setSearchYear(View v) {
        Button button = (Button) v;
        searchYear = button.getText().toString();
    }

    /**
     * A function that gets the necessary parameters to
     * submit a valid request to the API and starts the
     * search result activity.
     * @param keyword search string
     */
    private void search(String keyword) {
        if (keyword.isEmpty()) {
            Toast.makeText(this, "Please enter a keyword",
                    Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            Bundle extras = new Bundle();
            extras.putString("KEYWORD", keyword);
            extras.putString("TYPE", searchType);
            extras.putString("YEAR", searchYear);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }
}
