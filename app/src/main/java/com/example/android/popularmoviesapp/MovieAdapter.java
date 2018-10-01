package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.popularmoviesapp.database.MovieEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private final MovieAdapterOnClickHandler mClickHandler;
    private List<MovieEntry> mMovies;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public static String getPosterUrl(String imageString) {
        String BASE_URL = "http://image.tmdb.org/t/p/";
        String imageSize = "w185";
        return BASE_URL + imageSize + imageString;

    }

    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);


    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        MovieEntry movieEntry = mMovies.get(position);
        String thisMoviePoster = movieEntry.getPosterUrl();
        String posterUrl = getPosterUrl(thisMoviePoster);
        Context context = holder.mLayout.getContext();
        Picasso.with(context)
                .load(posterUrl)
                .into(holder.mImageView);


    }

    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.size();
    }

    public void setMovies(List<MovieEntry> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public int getMovieId(int position) {
        MovieEntry movie = mMovies.get(position);
        int movieId = movie.getId();
        return movieId;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int position);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final LinearLayout mLayout;
        ImageView mImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.iv);
            mLayout = (LinearLayout) view.findViewById(R.id.ll_main);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}
