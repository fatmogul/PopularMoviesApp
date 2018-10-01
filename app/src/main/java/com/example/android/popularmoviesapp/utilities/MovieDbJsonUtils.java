package com.example.android.popularmoviesapp.utilities;

import android.content.ContentValues;

import com.example.android.popularmoviesapp.database.MovieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


public class MovieDbJsonUtils {
    public static final String RESULTS = "results";

    public static final String TITLE = "title";

    public static final String POSTER_PATH = "poster_path";
    public static final String OVERVIEW = "overview";

    public static final String USER_RATING = "vote_average";
    public static final String RELEASE_DATE = "release_date";

    public static final String MESSAGE_CODE = "status_code";

    public static final String MOVIE_ID = "id";

    public static final String POPULARITY = "popularity";

    public static final String VOTE_AVERAGE = "vote_average";

    public static final String TRAILER_ID = "key";

    public static final String TRAILER_NAME = "name";

    public static final String REVIEW_AUTHOR = "author";

    public static final String REVIEW_CONTENT = "content";

    public static List<MovieEntry> getMovies(String moviesJsonStr)
            throws JSONException {

        List<MovieEntry> movies = new ArrayList<MovieEntry>();

        JSONObject movieJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (movieJson.has(MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray movieArray = movieJson.getJSONArray(RESULTS);


        for (int i = 0; i < movieArray.length(); i++) {
            /* These are the values that will be collected */
            JSONObject movie = movieArray.getJSONObject(i);

            int id = Integer.parseInt(movie.getString(MOVIE_ID));
            String posterUrl = movie.getString(POSTER_PATH);
            String title = movie.getString(TITLE);
            String overview = movie.getString(OVERVIEW);
            String userRating = movie.getString(USER_RATING);
            String releaseDate = movie.getString(RELEASE_DATE);
            float popularity = Float.parseFloat(movie.getString(POPULARITY));
            float voteAverage = Float.parseFloat(movie.getString(VOTE_AVERAGE));

            movies.add(new MovieEntry(id,title,posterUrl,overview,userRating,releaseDate,popularity,voteAverage,false,"",""));
        }

        return movies;
    }
    public static ContentValues parseReviews(String jsonResponse) throws JSONException {
        JSONObject reviewJson = new JSONObject(jsonResponse);
        ContentValues reviews = new ContentValues();


        /* Is there an error? */
        if (reviewJson.has(MESSAGE_CODE)) {
            int errorCode = reviewJson.getInt(MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray reviewArray = reviewJson.getJSONArray(RESULTS);
        for (int i = 0; i < reviewArray.length(); i++) {
            /* These are the values that will be collected */
            JSONObject review = reviewArray.getJSONObject(i);
            String authorName = review.getString(REVIEW_AUTHOR);
            String reviewContent = review.getString(REVIEW_CONTENT);
            reviews.put(authorName, reviewContent);
        }
        return reviews;

    }

    public static ContentValues parseTrailers(String jsonResponse) throws JSONException {
        JSONObject trailerJson = new JSONObject(jsonResponse);
        ContentValues trailers = new ContentValues();


        /* Is there an error? */
        if (trailerJson.has(MESSAGE_CODE)) {
            int errorCode = trailerJson.getInt(MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray trailerArray = trailerJson.getJSONArray(RESULTS);
        for (int i = 0; i < trailerArray.length(); i++) {
            /* These are the values that will be collected */
            JSONObject trailer = trailerArray.getJSONObject(i);
            String trailerName = trailer.getString(TRAILER_NAME);
            String trailerId = trailer.getString(TRAILER_ID);
            trailers.put(trailerId, trailerName);
        }
        return trailers;


}}

