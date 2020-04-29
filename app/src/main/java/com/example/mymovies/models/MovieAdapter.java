package com.example.mymovies.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.utils.Tools;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<Movie> movies;
    private List<Movie> moviesFull;
    private OnItemClickListener onItemClickListener;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
        moviesFull = new ArrayList<>(movies);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView year;
        ImageView poster;
        ImageButton details;
        ImageButton favorite;

        ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            year = v.findViewById(R.id.year);
            poster = v.findViewById(R.id.poster);
            details = v.findViewById(R.id.details);
            favorite = v.findViewById(R.id.favorite);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movies_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Movie movie = movies.get(position);

            view.title.setText(Tools.truncate(movie.getTitle(), 20));
            view.year.setText("Released: " + movie.getYear());

            if (!isEmpty(movie.getPoster())) {
                UrlImageViewHelper.setUrlDrawable(view.poster, movie.getPoster());
            }

            view.favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, movie, "favorite");
                    }
                }
            });

            view.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, movie, "details");
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Movie obj, String action);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Movie> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(moviesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Movie movie : moviesFull) {
                    if (movie.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(movie);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movies.clear();
            movies.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
