package com.example.android.popularmovies.utils;

import android.net.Uri;

import com.example.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public static final Uri API_URI = Uri.parse("https://api.themoviedb.org/3/");

    public static final String API_IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/";

    public enum IMAGE_SIZES {
        W185, W500
    }

    public static String getImageSize(IMAGE_SIZES size) {
        switch (size) {
            case W185: return "w185";
            case W500: return "w500";
            default: return "w185";
        }
    }

    public static String sendApiRequest(String queryMethod) {
        Uri popularUri = API_URI.buildUpon()
                .appendPath("movie")
                .appendPath(queryMethod)
                .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_TOKEN)
                .build();

        try {
            URL url = new URL(popularUri.toString());
            return getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getMovieAsJson(int movieId) {
        return sendApiRequest(movieId + "");
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
}
