package com.example.mymovies;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;


public class SearchResultsActivity extends AppCompatActivity {

    private final static String API_KEY = "9209cdbe";

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
        String result = getIntent().getStringExtra("KEYWORD");
        String type = getIntent().getStringExtra("TYPE");
        String year = getIntent().getStringExtra("YEAR");

        return result;
    }

    private void updateUI(JSONObject result) {
        TextView t = findViewById(R.id.test);
        t.setText(result.toString());
    }
}
