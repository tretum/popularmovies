package com.example.android.popularmovies.model;

public class QueryListEntry {

    private int listPosition;
    private int movieId;
    private String posterPath;

    public QueryListEntry(int listPosition, int movieId, String posterPath) {
        this.listPosition = listPosition;
        this.movieId = movieId;
        this.posterPath = posterPath;
    }

    public int getListPosition() {
        return listPosition;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }
}
