package com.example.mymovies.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymovies.models.HTTPClient;
import com.example.mymovies.R;
import com.example.mymovies.models.VolleyCallback;

import org.json.JSONObject;


public class SearchResultsActivity extends AppCompatActivity {

    private final static String API_KEY = "9209cdbe";
    private final static String URL = "https://omdbapi.com/?";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        submitRequest();
    }

    private void submitRequest() {
        String query = getQueryString();
        HTTPClient client = new HTTPClient(this, query);

        client.getResponse(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                updateUI(result);
            }
        });
    }

    private String getQueryString() {
        String keyword = getIntent().getStringExtra("KEYWORD");
        String type = getIntent().getStringExtra("TYPE");
        String year = getIntent().getStringExtra("YEAR");
        String result = URL + "s=" + keyword;

        return result;
    }

    private void updateUI(JSONObject result) {
        TextView t = findViewById(R.id.test);
        t.setText(result.toString());
    }
}
