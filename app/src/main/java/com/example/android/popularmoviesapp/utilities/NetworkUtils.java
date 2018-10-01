package com.example.android.popularmoviesapp.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

        private static final String TAG = NetworkUtils.class.getSimpleName();


        private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
        private static final String YOUTUBE_BASE_URL = "http://www.youtube.com/";

        private static final String MOVIE_API_CODE = "d3247c6727f78a5c3ac097a3c74ca8c0";

        final static String YOUTUBE_PARAM = "v";
        final static String API_PARAM = "api_key";
        final static String TRAILER_PATH = "videos";
        final static String REVIEW_PATH = "reviews";
        final static String YOUTUBE_PATH = "watch";

        public static URL buildMovieUrl(String preference) {
            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(preference)
                    .appendQueryParameter(API_PARAM, MOVIE_API_CODE)
                    .build();
            URL url = null;
            try{
                url = new URL(builtUri.toString());
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            return url;
        }
        public static URL buildTrailerUrl(int movieId){
            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(String.valueOf(movieId))
                    .appendPath(TRAILER_PATH)
                    .appendQueryParameter(API_PARAM, MOVIE_API_CODE)
                    .build();
            URL url = null;
            try{
                url = new URL(builtUri.toString());
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            return url;
        }

        public static URL buildReviewUrl(int movieId){
            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendPath(String.valueOf(movieId))
                    .appendPath(REVIEW_PATH)
                    .appendQueryParameter(API_PARAM, MOVIE_API_CODE)
                    .build();
            URL url = null;
            try{
                url = new URL(builtUri.toString());
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            return url;
        }
        /**
         * This method returns the entire result from the HTTP response.
         *
         * @param url The URL to fetch the HTTP response from.
         * @return The contents of the HTTP response.
         * @throws IOException Related to network and stream reading
         */
        public static String getResponseFromHttpUrl(URL url) throws IOException {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        }
        public static URL buildYoutubeUrl(String trailerId){
            Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                    .appendPath(YOUTUBE_PATH)
                    .appendQueryParameter(YOUTUBE_PARAM, trailerId)
                    .build();
            URL url = null;
            try{
                url = new URL(builtUri.toString());
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            return url;

        }
    }

