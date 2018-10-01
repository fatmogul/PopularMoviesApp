package com.example.android.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.database.AppDatabase;
import com.example.android.popularmoviesapp.database.MovieEntry;
import com.example.android.popularmoviesapp.utilities.MovieDbJsonUtils;
import com.example.android.popularmoviesapp.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {
    public static final String MOVIE_ID = "movie_id";
    private static final int DEFAULT_POSITION = -1;
    private AppDatabase mDb;

    TextView mMovieTitleView;
    TextView mMovieOverviewView;
    TextView mMovieUserRatingView;
    TextView mMovieReleaseDateView;
    ImageView mMoviePosterView;
    Button mButton;
    int movieId;
    boolean mFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mButton = (Button) findViewById(R.id.favorite_button);
        Intent intent = getIntent();
        if(intent == null){
            closeOnError();
        }

        movieId = intent.getIntExtra(MOVIE_ID,DEFAULT_POSITION);
        mDb = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final MovieEntry movie = mDb.taskDao().loadMovieById(movieId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mMovieTitleView = (TextView) findViewById(R.id.tv_movie_title);
                        mMovieOverviewView = (TextView) findViewById(R.id.tv_movie_overview);
                        mMovieReleaseDateView = (TextView) findViewById(R.id.tv_movie_release_date);
                        mMovieUserRatingView = (TextView) findViewById(R.id.tv_movie_user_rating);
                        mMoviePosterView = (ImageView) findViewById(R.id.iv_movie_poster);

                        mMovieTitleView.setText(movie.getTitle());
                        mMovieOverviewView.setText(movie.getOverview());
                        mMovieReleaseDateView.setText(movie.getReleaseDate());
                        mMovieUserRatingView.setText(Float.toString(movie.getPopularity()));

                        mFavorite = movie.getFavorite();
                        if(mFavorite){
                            mButton.setText(getString(R.string.remove_from_favorites));
                        }else{
                            mButton.setText(getString(R.string.add_to_favorites));
                        }
                        String posterId = movie.getPosterUrl();
                        String posterUrl = MovieAdapter.getPosterUrl(posterId);
                        Picasso.with(getApplicationContext())
                                .load(posterUrl)

                                .into(mMoviePosterView);
                        LinearLayout reviewView = findViewById(R.id.reviews_layout);
                        String reviewJson = movie.getReviewsJson();
                        ContentValues reviews = null;
                        try {
                            reviews = MovieDbJsonUtils.parseReviews(reviewJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Set<String> reviewKeys = reviews.keySet();
                        for (final String author : reviewKeys){
                            LinearLayout reviewLayout = new LinearLayout(DetailActivity.this);
                            reviewLayout.setOrientation(LinearLayout.HORIZONTAL);
                            TextView authorView = new TextView(DetailActivity.this);
                            authorView.setText(author + " - ");
                            authorView.setTypeface(Typeface.DEFAULT_BOLD);
                            reviewLayout.addView(authorView);
                            String content = reviews.getAsString(author);
                            TextView contentView = new TextView(DetailActivity.this);
                            contentView.setText(content);
                            reviewLayout.addView(contentView);
                            reviewView.addView(reviewLayout);
                        }



                        LinearLayout trailerView = findViewById(R.id.trailers_layout);
                        String trailersJson = movie.getTrailersJson();
                        ContentValues trailers = null;
                        try {
                            trailers = MovieDbJsonUtils.parseTrailers(trailersJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Set<String> trailerKeys = trailers.keySet();
                        for (final String trailerId : trailerKeys){
                            Button newButton = new Button(getApplicationContext());
                            String trailerName = trailers.getAsString(trailerId);
                            newButton.setText(trailerName);
                            newButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    goToYoutube(trailerId);
                                }
                            });
                            trailerView.addView(newButton);
                    }
                };

            });

        };




    });}
    public void goToYoutube(String trailerId){
        String youtubeUrl = String.valueOf(NetworkUtils.buildYoutubeUrl(trailerId));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl));
        startActivity(intent);
    }
public void changeFavorite(View v){
               if(!(mFavorite)) {
                    mFavorite = true;
                    mButton.setText(R.string.remove_from_favorites);
                }
                else{
                    mFavorite = false;
                    mButton.setText(R.string.add_to_favorites);
                }
                mDb = AppDatabase.getInstance(getApplicationContext());
               AppExecutors.getInstance().diskIO().execute(new Runnable() {
                   @Override
                   public void run() {
                    MovieEntry movie = mDb.taskDao().loadMovieById(movieId);
                    movie.setFavorite(mFavorite);
                    mDb.taskDao().updateMovie(movie);
            }
        });

}
    private void closeOnError() {
        finish();
        Toast.makeText(this, "@string/error_message", Toast.LENGTH_SHORT).show();
    }
}
