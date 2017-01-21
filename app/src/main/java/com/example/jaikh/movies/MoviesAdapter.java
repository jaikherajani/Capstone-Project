package com.example.jaikh.movies;

/**
 * Created by jaikh on 09-12-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    long movie_id;
    private List<Movie> movies;
    private Context context;
    private Movie movie;
    private FragmentManager supportFragmentManager;


    public MoviesAdapter(Context context, List<Movie> movies, FragmentManager supportFragmentManager) {
        this.movies = movies;
        this.context = context;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        //holder.movieTitle.setText(movies.get(position).getTitle());
        //holder.data.setText(movies.get(position).getReleaseDate());
        //holder.movieDescription.setText(movies.get(position).getOverview());
        //holder.rating.setText(movies.get(position).getVoteAverage().toString());
        Glide.with(context).load("https://image.tmdb.org/t/p/w185/" + movies.get(position).getPosterPath()).into(holder.poster);
        //System.out.println(movies.get(position).getPosterPath());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movie_id = movies.get(position).getId();
                movie = movies.get(position);
                System.out.println("movie " + movie_id);
                Context context = view.getContext();
                if (MainActivity.tablet) {
                    Bundle mBundle = new Bundle();
                    mBundle.putLong("MOVIE_ID", movie_id);
                    mBundle.putSerializable("MOVIE", movie);
                    MovieDetailFragment detailFragment = new MovieDetailFragment();
                    detailFragment.setArguments(mBundle);
                    supportFragmentManager.beginTransaction().replace(R.id.containerDetails, detailFragment).commit();
                } else {
                    Intent intent = new Intent(context, MovieDetail.class);
                    intent.putExtra("MOVIE_ID", movie_id);
                    intent.putExtra("MOVIE", movie);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView poster;
        View mView;

        public MovieViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mView = v;
            poster = (ImageView) v.findViewById(R.id.grid_image);
        }

        @Override
        public void onClick(View view) {
            System.out.println("Clicked");
        }
    }
}