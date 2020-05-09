package com.example.android.popularmovies.model;

public class Movie {

    private final int id;
    private String title;
    private String posterPath;
    private int runtime;
    private double rating;
    private String description;

    public String getReleaseDate() {
        return releaseDate;
    }

    private String releaseDate;

    public Movie(int id, String title, String posterPath, int runtime, double rating, String description, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.runtime = runtime;
        this.rating = rating;
        this.description = description;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
