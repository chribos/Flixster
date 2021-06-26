package com.example.flixster.models;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import org.parceler.Parcel;
@Parcel //indicates class can be parsed
public class Movie {
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    Double voteAverage;
    Integer id;

    public Movie() {}

    public Movie(JSONObject movie) throws JSONException {
        posterPath = movie.getString("poster_path");
        backdropPath = movie.getString("backdrop_path");
        title = movie.getString("title");
        overview = movie.getString("overview");
        voteAverage = movie.getDouble("vote_average");
        id = movie.getInt("id");
    }
    //turn array into list of movies
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i =0; i<movieJsonArray.length(); i++) {
            //add movie at each position of array
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }
    //to get data out of objects by right click -> generate -> getters -> select all 3

    //we have to add base url and image size and then append poster path to that
    public String getPosterPath() {
        //base url and size hardcoded and then path appended
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }
    public String getBackdropPath() {
        //base url and size hardcoded and then path appended
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public Integer getIdPath() {
        return (id);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }
}
