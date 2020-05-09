package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.example.android.popularmovies.utils.TheMovieDBJsonUtils;
import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.utils.NetworkUtils.API_IMAGE_BASE_PATH;

public class Detail extends AppCompatActivity {

    private TextView mYearTextView;
    private TextView mDescriptionTextView;
    private TextView mRatingTextView;
    private TextView mRuntimeTextView;
    private TextView mTitleTextView;

    private ImageView mPosterImageView;

    private ProgressBar mProgressBar;
    private TextView mErrorMessage;

    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mScrollView = findViewById(R.id.sv_detail_content);

        mProgressBar = findViewById(R.id.pb_detail_loading_indicator);
        mErrorMessage = findViewById(R.id.tv_detail_error_message);

        mPosterImageView = findViewById(R.id.iv_detail_poster);
        mTitleTextView = findViewById(R.id.tv_detail_movie_title);

        mYearTextView = findViewById(R.id.tv_detail_year);
        mDescriptionTextView = findViewById(R.id.tv_detail_description);
        mRatingTextView = findViewById(R.id.tv_detail_rating);
        mRuntimeTextView = findViewById(R.id.tv_detail_runtime);

        Intent startingIntent = getIntent();
        if(startingIntent != null) {
            if(startingIntent.hasExtra(Intent.EXTRA_TEXT)) {

                int movieId = startingIntent.getIntExtra(Intent.EXTRA_TEXT, 1);
                new DetailFetcher().execute(movieId);

            }
        }
    }

    private void enableLoadingView(){
        mProgressBar.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
    }

    private void enableDetailView(Movie movie) {
        mProgressBar.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);

        String imageSize = NetworkUtils.getImageSize(NetworkUtils.IMAGE_SIZES.W500);
        String path = API_IMAGE_BASE_PATH  + imageSize + movie.getPosterPath();
        Picasso.get()
                .load(path)
                .into(mPosterImageView);

        mRatingTextView.setText(movie.getRating() + "/10");
        mRuntimeTextView.setText(movie.getRuntime() + " mins");
        mDescriptionTextView.setText(movie.getDescription());
        mYearTextView.setText(movie.getReleaseDate());
        mTitleTextView.setText(movie.getTitle());
    }

    private void loadErrorView() {
        mErrorMessage.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    private class DetailFetcher extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... integers) {
            if(integers.length < 1) {
                return null;
            }
            int movieId = integers[0];

            String movieAsJson = NetworkUtils.getMovieAsJson(movieId);
            if(movieAsJson != null) {
                return TheMovieDBJsonUtils.parseMovie(movieId, movieAsJson);
            } else {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            enableLoadingView();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Movie movie) {
            if(movie == null) {
                loadErrorView();
            } else {
                enableDetailView(movie);
            }
            super.onPostExecute(movie);
        }
    }


}
