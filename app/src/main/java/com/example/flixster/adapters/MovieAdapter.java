package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;


import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @NotNull
    @Override
    //inflates xml layout and returns to the holder
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView= LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(movieView);
    }

    @Override
    //populated data into the view through holder
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder"+position);
        //get movie data position
        Movie movie = movies.get(position);
        //then bind data into view holder
        holder.bind(movie);
    }

    @Override
    //return item count [movie list]
    public int getItemCount() {
        return movies.size();
    }

    //define inner ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //define member variables for each component of xml layout
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            //in constructor define where text/image views are coming from
            tvTitle= itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster= itemView.findViewById(R.id.ivPoster);
            //add this to itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            int placeholder;
            //add radius and margin for rounded cornes
            int radius = 30;
            int margin = 10;
            //if phone is in landscape, then set imageUrl to be backdrop image
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl= movie.getBackdropPath();
                placeholder = R.drawable.flicks_backdrop_placeholder;
            }
            else {
                imageUrl = movie.getPosterPath();
                placeholder = R.drawable.flicks_movie_placeholder;
            }
            //else set imageUrl = posterImage
            Glide.with(context).load(imageUrl).centerCrop().transform(new RoundedCornersTransformation(radius, margin)).placeholder(placeholder).override(300,400).into(ivPoster);
        }

        @Override
        public void onClick(View v) {
            //get position
            int position = getAdapterPosition();
            //check if it is valid
            if(position != RecyclerView.NO_POSITION){
                //get movie at pos
                Movie movie = movies.get(position);
                //make intent for activity of displaying details
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                //serialize the movie with parceler to turn into key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                //show the activity
                context.startActivity(intent);


            }

        }
    }
}
