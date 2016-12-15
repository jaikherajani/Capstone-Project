package com.example.jaikh.movies;

/**
 * Created by jaikh on 09-12-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

import com.bumptech.glide.Glide;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    //private int rowLayout;
    private Context context;
    private Movie movie;
    //static OnItemClickListener mItemClickListener;


    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        TextView rating;
        ImageView poster;
        View mView;

        public MovieViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mView = v;
            poster = (ImageView) v.findViewById(R.id.grid_image);
            //context = v.getContext();
            //moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            //movieTitle = (TextView) v.findViewById(R.id.title);
            //data = (TextView) v.findViewById(R.id.subtitle);
            //movieDescription = (TextView) v.findViewById(R.id.description);
            //rating = (TextView) v.findViewById(R.id.rating);
            /*v.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    if (clickListener != null)
                        clickListener.onClick(view, getAdapterPosition());// call the onClick in the OnItemClickListener
                }
            }); // bind the listener*/
        }

        @Override
        public void onClick(View view) {
            System.out.println("Clicked");
        }
    }

    public MoviesAdapter(Context context,List<Movie> movies) {
        this.movies = movies;
        //this.rowLayout = rowLayout;
        this.context = context;
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
        Glide.with(context).load("http://image.tmdb.org/t/p/w185/"+movies.get(position).getPosterPath()).into(holder.poster);
        //System.out.println(movies.get(position).getPosterPath());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long movie_id = movies.get(position).getId();
                System.out.println("movie "+movie_id);
                Context context = view.getContext();
                Intent intent = new Intent(context, MovieDetail.class);
                intent.putExtra("MOVIE_ID", movie_id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}