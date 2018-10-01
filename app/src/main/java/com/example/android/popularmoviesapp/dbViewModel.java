package com.example.android.popularmoviesapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.example.android.popularmoviesapp.database.AppDatabase;
import com.example.android.popularmoviesapp.database.MovieEntry;

import java.util.List;

class dbViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = dbViewModel.class.getSimpleName();
    private AppDatabase database;
    private LiveData<List<MovieEntry>> movies;
    private static String mPreference = "popular";


    public dbViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        mPreference = sharedPreferences.getString(getApplication().getString(R.string.pref_view_key),getApplication().getString(R.string.pref_view_popular_value));
        movies = whichMovies(mPreference);
    }

    private LiveData<List<MovieEntry>> whichMovies(String preference) {
        Log.d(TAG, "whichMovies: Querying the Database");
        switch (preference) {
            case "popular":
                movies = database.taskDao().loadAllMoviesByPopularity();
                break;
            case "top_rated":
                movies = database.taskDao().loadAllMoviesByVoteAverage();
                break;
            case "favorite":
                movies = database.taskDao().loadAllMoviesByFavorite(true);
                break;
            default:
                movies = database.taskDao().loadAllMoviesByPopularity();

        }
        return movies;
    }

    public LiveData<List<MovieEntry>> getMovies(String preference) {
        if(preference != mPreference) {
            mPreference = preference;
            return whichMovies(preference);
        }else{
            return movies;
        }
    }
}

