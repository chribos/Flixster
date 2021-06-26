package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieTrailerActivity;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

import static android.content.ContentValues.TAG;

public class MovieDetailsActivity extends AppCompatActivity {
    Movie movie;
    Button play;
    String id;
    //add view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    TextView tvRating;
    ImageView trailerPoster;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_movie_details);
        //above replaced with new view binding library to reduce code
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //unwrap the movie passed in via intent with its simple name as the key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format ("Showing details for %s", movie.getTitle()));
        // resolve the view objects
        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        rbVoteAverage = binding.rbVoteAverage;
        trailerPoster = binding.trailerPoster;
        play = binding.play;
        id = String.valueOf(movie.getIdPath());
        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        Glide.with(this).load(movie.getBackdropPath()).placeholder(R.drawable.flicks_backdrop_placeholder).override(600,800).into(trailerPoster);
        final String[] key = {""};

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);
        tvRating = binding.tvRating;
        tvRating.setText(voteAverage / 2.0f +"/" +"5");
        //AsyncHttpClient client = new AsyncHttpClient();
        String TRAILER_URL = "https://api.themoviedb.org/3/movie/"+id+"/videos?api_key=3493a81ca7ef0536653b049b3aae20cd";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(TRAILER_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject= json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results" + results.toString());
                    //ids.addAll(Movie.fromJsonArray(results));
                    key[0] = results.getJSONObject(0).getString("key");
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get position
                //check if it is valid
                //get movie at pos
                //make intent for activity of displaying details
                Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                //serialize the movie with parceler to turn into key
                Log.d(TAG, key[0]);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(key[0]));
                //show the activity
                startActivity(intent);
            }
        });
    }

}