package com.example.mymovies.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymovies.R;
import com.example.mymovies.utils.Tools;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.List;

/**
 * A class that serves as a bridge between the view
 * and the information from am arbitrary source.
 */
public class MovieGridAdapter extends BaseAdapter {

    private List<Movie> movies;
    private OnItemClickListener onItemClickListener;

    /**
     * Parameterized constructor.
     * @param movies list of movie objects
     */
    public MovieGridAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        TextView title;
        TextView year;
        ImageView poster;
        ImageButton delete;

        // Inflate the view
        if (v == null) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_movies_grid, viewGroup, false);
        }

        title = v.findViewById(R.id.title);
        year = v.findViewById(R.id.year);
        poster = v.findViewById(R.id.poster);
        delete = v.findViewById(R.id.delete);

        // Get the current movie
        final Movie movie = getItem(i);

        // Update the view with the movie's title and release year
        title.setText(Tools.truncate(movie.getTitle(), 15));
        year.setText("Released: " + movie.getYear());

        // Setup a async task to retrieve the movie's poster if it exists
        if (movie.getPoster() != "") {
            UrlImageViewHelper.setUrlDrawable(poster, movie.getPoster());
        }

        // Listen for clicks on the movie poster
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, movie, "detail");
                }
            }
        });

        // Listen for clicks on the trash icon
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, movie, "delete");
                }
            }
        });

        return v;
    }

    /**
     * An interface to enable setting a listener from an
     * outside method.
     */
    public interface OnItemClickListener {
        void onItemClick(View view, Movie obj, String action);
    }

    /**
     * A function sets a listener received from the calling
     * function.
     * @param onItemClickListener menu item click listener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

