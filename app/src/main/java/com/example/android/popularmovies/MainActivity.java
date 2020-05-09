package com.example.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.model.QueryListEntry;
import com.example.android.popularmovies.utils.NetworkUtils;
import com.example.android.popularmovies.utils.TheMovieDBJsonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PosterAdapter.PosterAdapterOnClickHandler {
    public static final String QUERY_METHOD_POPULAR = "popular";
    public static final String QUERY_METHOD_HIGHEST_RATED = "top_rated";

    private RecyclerView mPosterGridRecyclerView;
    private TextView mErrorMessageTextView;
    private ProgressBar mProgressBar;
    private PosterAdapter posterAdapter;

    private String currentQueryMethod = QUERY_METHOD_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mErrorMessageTextView = findViewById(R.id.tv_error_message_display);
        mPosterGridRecyclerView = findViewById(R.id.rv_poster_grid);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mPosterGridRecyclerView.setLayoutManager(gridLayoutManager);
        posterAdapter = new PosterAdapter(this);
        mPosterGridRecyclerView.setAdapter(posterAdapter);
        loadMovieList(currentQueryMethod);
    }

    @Override
    public void handleGridItemClicked(int movieId) {
        Context context = MainActivity.this;
        Class<Detail> destination = Detail.class;
        Intent intent = new Intent(context, destination);
        intent.putExtra(Intent.EXTRA_TEXT, movieId);
        startActivity(intent);
    }

    private void enableLoadingView() {
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.GONE);
        mPosterGridRecyclerView.setVisibility(View.GONE);
    }

    private void enableListView() {
        mProgressBar.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.GONE);
        mPosterGridRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if(itemId == R.id.menu_highest_rated) {
            currentQueryMethod = QUERY_METHOD_HIGHEST_RATED;
        } else if(itemId == R.id.menu_most_popular) {
            currentQueryMethod = QUERY_METHOD_POPULAR;
        }
        loadMovieList(this.currentQueryMethod);

        return super.onOptionsItemSelected(item);
    }

    private void loadMovieList(String queryMethod) {
        if(queryMethod.equals(QUERY_METHOD_HIGHEST_RATED)) {
            setTitle(getString(R.string.highest_rated_title));
        } else if(queryMethod.equals(QUERY_METHOD_POPULAR)){
            setTitle(getString(R.string.most_popular_title));
        }
        new FetchMovieList().execute(queryMethod);
    }

    private void loadErrorView() {
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mPosterGridRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    private class FetchMovieList extends AsyncTask<String, Void, List<QueryListEntry>> {

        @Override
        protected List<QueryListEntry> doInBackground(String... strings) {
            String queryMethod = strings[0];

            String popularMoviesJson = NetworkUtils.sendApiRequest(queryMethod);
            if(popularMoviesJson == null || popularMoviesJson.equals("")) {
                return new ArrayList<>();
            }

            return TheMovieDBJsonUtils.getResultEntries(popularMoviesJson);
        }

        @Override
        protected void onPreExecute() {
            enableLoadingView();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<QueryListEntry> movies) {
            if(movies.isEmpty()){
                loadErrorView();
            } else {
                enableListView();
                posterAdapter.changeData(movies);
            }
        }
    }
}
