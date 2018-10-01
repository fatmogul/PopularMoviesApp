package com.example.android.popularmoviesapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.popularmoviesapp.database.AppDatabase;
import com.example.android.popularmoviesapp.database.MovieEntry;
import com.example.android.popularmoviesapp.utilities.MovieDbJsonUtils;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,SharedPreferences.OnSharedPreferenceChangeListener {
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private AppDatabase mDb;
    String mPreference;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.settings){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*below code found at https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out from user @gar*/
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreference = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_view_key),getString(R.string.pref_view_popular_value));

        mDb = AppDatabase.getInstance(getApplicationContext());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<MovieEntry> movies = null;
                URL movieRequest = NetworkUtils.buildMovieUrl(mPreference);
                if (isOnline() && mPreference != "favorite") {
                    try {
                        movies = MovieDbJsonUtils.getMovies(NetworkUtils.getResponseFromHttpUrl(movieRequest));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (movies != null) {
                    for (MovieEntry movie : movies) {
                        String trailerJson = "";
                        String reviewJson = "";
                        int movieId = movie.getId();
                        URL trailerRequest = NetworkUtils.buildTrailerUrl(movieId);
                        URL reviewRequest = NetworkUtils.buildReviewUrl(movieId);
                        try {
                            trailerJson = NetworkUtils.getResponseFromHttpUrl(trailerRequest);
                            } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            reviewJson = NetworkUtils.getResponseFromHttpUrl(reviewRequest);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        movie.setTrailersJson(trailerJson);
                        movie.setReviewsJson(reviewJson);
                        try {

                            mDb.taskDao().insertMovie(movie);
                        } catch (Exception e) {
                            MovieEntry newMovie = mDb.taskDao().loadMovieById(movieId);
                            MovieEntry finalMovie = new MovieEntry(movie.getId(),movie.getTitle(),movie.getPosterUrl(),movie.getOverview(),movie.getUserRating(),movie.getReleaseDate(),movie.getPopularity(),movie.getVoteAverage(),newMovie.getFavorite(),trailerJson,reviewJson);
                            mDb.taskDao().updateMovie(finalMovie);
                        }
                    }
                }


                return null;
            }

        }.execute();
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        int columns = 4;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, columns));

        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        setupViewModel(mPreference);


    }

    private void setupViewModel(String preference) {

        dbViewModel viewModel = ViewModelProviders.of(this).get(dbViewModel.class);
        viewModel.getMovies(mPreference).observe(this, new Observer<List<MovieEntry>>() {
            @Override
            public void onChanged(@Nullable List<MovieEntry> movieEntries) {
                mAdapter.setMovies(movieEntries);
            }
        });

    }


    @Override
    public void onClick(int position) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        int movieId = mAdapter.getMovieId(position);
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra(DetailActivity.MOVIE_ID, movieId);
        startActivity(intent);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_view_key))){
            mPreference = sharedPreferences.getString(getString(R.string.pref_view_key),getString(R.string.pref_view_popular_value));
            setupViewModel(mPreference);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        SharedPreferences sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);

        mPreference = sharedPreferences.getString(getString(R.string.pref_view_key),getString(R.string.pref_view_popular_value));
        setupViewModel(mPreference);
    }
}
