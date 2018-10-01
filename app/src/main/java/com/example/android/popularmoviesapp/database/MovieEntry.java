package com.example.android.popularmoviesapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;

@Entity(tableName = "movie")
public class MovieEntry {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private String title;
    @ColumnInfo(name = "poster_url")
    private String posterUrl;
    private String overview;
    @ColumnInfo(name = "user_rating")
    private String userRating;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    private float popularity;
    @ColumnInfo(name = "vote_average")
    private float voteAverage;
    private boolean favorite;
    private String trailersJson;
    private String reviewsJson;

    /**
     * No args constructor for use in serialization
     */

    public MovieEntry(int id, String title, String posterUrl, String overview, String userRating, String releaseDate, float popularity, float voteAverage,boolean favorite, String trailersJson, String reviewsJson) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.favorite = favorite;
        this.trailersJson = trailersJson;
        this.reviewsJson = reviewsJson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean getFavorite(){return favorite;}

    public void setFavorite(boolean favorite) {this.favorite = favorite;}

    public String getTrailersJson(){return trailersJson;}

    public void setTrailersJson(String trailersJson){this.trailersJson = trailersJson;}

    public String getReviewsJson() {
        return reviewsJson;
    }

    public void setReviewsJson(String reviewsJson) {
        this.reviewsJson = reviewsJson;
    }
}

