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

public class MovieGridAdapter extends BaseAdapter {

    private List<Movie> movies;
    private OnItemClickListener onItemClickListener;

    public MovieGridAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        TextView title;
        TextView year;
        ImageView poster;
        ImageButton delete;

        if (v == null) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_movies_grid, viewGroup, false);
        }

        title = v.findViewById(R.id.title);
        year = v.findViewById(R.id.year);
        poster = v.findViewById(R.id.poster);
        delete = v.findViewById(R.id.delete);

        final Movie movie = getItem(i);

        title.setText(Tools.truncate(movie.getTitle(), 15));
        year.setText("Released: " + movie.getYear());

        if (movie.getPoster() != "") {
            UrlImageViewHelper.setUrlDrawable(poster, movie.getPoster());
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, movie);
                }
            }
        });

        return v;
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

    public interface OnItemClickListener {
        void onItemClick(View view, Movie obj);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

