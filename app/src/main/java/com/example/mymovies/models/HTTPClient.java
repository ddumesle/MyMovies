package com.example.mymovies.models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * A class that handles the request made to the
 * REST API via HTTP.
 */
public class HTTPClient {

    private String url;
    private RequestQueue queue;

    /**
     * Parameterized constructor.
     * @param context context of the app
     * @param url url string to make request
     */
    public HTTPClient(Context context, String url) {
        this.url = url;
        queue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * A function that makes a request to an API and returns
     * the response to a callback.
     * @param callback callback to return json to calling function
     */
    public void getResponse(final VolleyCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MYMOVIES", error.getMessage());
                    }
                });

        queue.add(request);
    }

    /**
     * A callback function that accesses the JSONObject
     * from the response of an API call.
     */
    public interface VolleyCallback {
        void onSuccess(JSONObject result);
    }
}
