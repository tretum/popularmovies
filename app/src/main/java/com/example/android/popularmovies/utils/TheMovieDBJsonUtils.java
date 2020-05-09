package com.example.android.popularmovies.utils;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.QueryListEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TheMovieDBJsonUtils {

    private static final String POSTER_PATH = "poster_path";
    private static final String RESULTS = "results";


    public static List<QueryListEntry> getResultEntries(String responseAsJsonString) {
        ArrayList<QueryListEntry> results = new ArrayList<>();
        JSONArray resultsList;
        try {
            JSONObject jsonObject = new JSONObject(responseAsJsonString);
            resultsList = jsonObject.getJSONArray(RESULTS);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        for (int i = 0; i < resultsList.length(); i++) {
            try {
                JSONObject result = resultsList.getJSONObject(i);
                results.add(getEntryFromJsonObject(i, result));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private static QueryListEntry getEntryFromJsonObject(int position, JSONObject resultEntryObject)
            throws JSONException {

        int id = resultEntryObject.getInt("id");
        String posterPath = resultEntryObject.getString(POSTER_PATH);

        return new QueryListEntry(position, id, posterPath);
    }

    public static String getPosterPathFromJson(int index, String responseAsJsonString) {
        try {
            JSONObject jsonObject = new JSONObject(responseAsJsonString);
            JSONArray resultsList = jsonObject.getJSONArray(RESULTS);
            if(index < resultsList.length()){
                JSONObject result = resultsList.getJSONObject(index);
                String posterPath = result.getString(POSTER_PATH);
                return posterPath;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Movie parseMovie(int movieId, String movieResponseAsJson) {
        try {
            JSONObject jsonObject = new JSONObject(movieResponseAsJson);

            String title = jsonObject.getString("title");
            int runtime = jsonObject.getInt("runtime");
            double vote_average = jsonObject.getDouble("vote_average");
            String description = jsonObject.getString("overview");
            String posterPath = jsonObject.getString("poster_path");
            String releaseDate = jsonObject.getString("release_date");

            return new Movie(movieId, title, posterPath, runtime, vote_average, description, releaseDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
