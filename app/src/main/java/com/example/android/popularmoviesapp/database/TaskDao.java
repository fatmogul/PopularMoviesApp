package com.example.android.popularmoviesapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    LiveData<List<MovieEntry>> loadAllMoviesByPopularity();

    @Insert
    void insertMovie(MovieEntry movieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieEntry movieEntry);

    @Delete
    void deleteMovie(MovieEntry movieEntry);

    @Query("SELECT * FROM movie ORDER BY vote_average DESC")
    LiveData<List<MovieEntry>> loadAllMoviesByVoteAverage();

    @Query("SELECT * FROM movie WHERE id = :id")
    MovieEntry loadMovieById(int id);

    @Query("SELECT * FROM movie WHERE favorite = :bool")
    LiveData<List<MovieEntry>> loadAllMoviesByFavorite(boolean bool);
}
