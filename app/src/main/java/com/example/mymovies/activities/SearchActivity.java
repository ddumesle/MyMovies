package com.example.mymovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymovies.R;

/**
 * A class the manages the search flow and enables user
 * to search for a movie by keyword, movie type, and
 * movie release year.
 */
public class SearchActivity extends AppCompatActivity {

    private RadioGroup type;
    private RadioGroup year;

    private String searchType;
    private String searchYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        type = findViewById(R.id.type);
        year = findViewById(R.id.year);
        final EditText searchInput = findViewById(R.id.search);

        // Call the search function when user clicks enter on keyboard
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(searchInput.getText().toString());
                    return true;
                }
                return false;
            }
        });

        // Clear all selections of the movie type group
        findViewById(R.id.clear_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type.clearCheck();
                searchType = null;
            }
        });

        // Clear all selections of the movie year group
        findViewById(R.id.clear_year).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year.clearCheck();
                searchYear = null;
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
        RadioButton button = findViewById(type.getCheckedRadioButtonId());
        searchType = button.getText().toString().toLowerCase();
    }

    /**
     * A function that gets the text from the view via
     * onClick and sets the searchYear variable to be
     * used as 'y' parameter in the API.
     * @param v activity_search view
     */
    public void setSearchYear(View v) {
        RadioButton button = findViewById(year.getCheckedRadioButtonId());
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
