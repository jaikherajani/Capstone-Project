package com.example.jaikh.movies;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaikh on 09-12-2016.
 */

public class MovieDetailFragment extends Fragment {

    private Movie movie;
    private SingleMovie sMovie;
    public TextView plotView, voteAvg, releaseDate, Title, reviews, voteCount,status;
    public ImageView imageView, imageView2, trailerview;
    public Toolbar myToolbar;
    public CollapsingToolbarLayout appbar;
    private final static String API_KEY = "9f51fc0657294458eba8b6a2080ac00f";
    public String key;
    FloatingActionButton viewtrailer;
    public long movie_id;
    private DBHelper databaseHelper;
    private String resultJSON = null;
    public String[] review = new String[10];
    public String[] author = new String[10];
    private FirebaseAnalytics mFirebaseAnalytics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        rootView = inflater.inflate(R.layout.movie_detail, container, false);
        Title = (TextView) rootView.findViewById(R.id.name);
        databaseHelper = new DBHelper(getContext());
        plotView = (TextView) rootView.findViewById(R.id.synopsis);
        voteAvg = (TextView) rootView.findViewById(R.id.vote_average);
        releaseDate = (TextView) rootView.findViewById(R.id.release_date);
        Title = (TextView) rootView.findViewById(R.id.name);
        imageView = (ImageView) rootView.findViewById(R.id.big_poster);
        imageView2 = (ImageView) rootView.findViewById(R.id.small_poster);
        trailerview = (ImageView) rootView.findViewById(R.id.moviestills);
        reviews = (TextView) rootView.findViewById(R.id.reviews);
        status = (TextView) rootView.findViewById(R.id.status);
        voteCount = (TextView) rootView.findViewById(R.id.vote_count);
        viewtrailer = (FloatingActionButton) rootView.findViewById(R.id.trailer);
        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("MOVIE");
            movie_id = getArguments().getLong("MOVIE_ID");
        }
        final FloatingActionButton share = (FloatingActionButton) rootView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey checkout this movie, its awesome. Here's the link - https://www.themoviedb.org/movie/" + movie_id);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share Movie with..."));
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        /*if(fav.equals("YES"))
            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite));
        else
            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_border));*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put("movie_id",movie_id);
                values.put("poster_path",sMovie.getPosterPath());
                getActivity().getContentResolver().insert(MovieProvider.CONTENT_URI, values);
                    Snackbar.make(view, "Saved as Favorite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite));
                mFirebaseAnalytics.setUserProperty("FAV_MOVIE",sMovie.getTitle());
                AppWidgetManager awm = AppWidgetManager.getInstance(getContext());
                Intent intent = new Intent(getContext(), AppWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                /*int[] ids = {R.xml.app_widget_info};
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);*/
                getActivity().sendBroadcast(intent);
            }
        });

        myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);
        appbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);

        //System.out.println(movie.getId());
        getData(movie_id);
        return rootView;
    }

    void getData(long movie_id)
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SingleMovie> call = apiService.getMovieDetails(movie_id, API_KEY, "videos");
        call.enqueue(new Callback<SingleMovie>() {
            @Override
            public void onResponse(Call<SingleMovie> call, Response<SingleMovie> response) {
                int statusCode = response.code();
                //movie1 = response.body().getResult();
                sMovie = response.body();
                displayData(sMovie);

                System.out.println("sMovie : "+sMovie.getId());
                System.out.println("single movie " + call.request().url());

                //recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.rv_item, getApplicationContext()));
                //recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
            }

            @Override
            public void onFailure(Call<SingleMovie> call, Throwable t) {
                // Log error here since request failed
                //Log.e("error : ", t.toString());
                Log.e("error : ", t.getMessage());
            }
        });

    }

 void displayData(SingleMovie sMovie)
 {

     ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
     ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
     appbar.setTitle(sMovie.getTitle());

     System.out.println("sMovie : "+sMovie.getTitle());
     Toast.makeText(getActivity(), "Showing " + sMovie.getTitle(), Toast.LENGTH_SHORT).show();
     status.setText(sMovie.getStatus());
     Glide.with(getContext())
             .load("https://image.tmdb.org/t/p/w780/" + sMovie.getBackdropPath())
             .into(imageView);
     System.out.println("backdrop path : " + sMovie.getBackdropPath());
     Glide.with(getContext())
             .load("https://image.tmdb.org/t/p/w185/" + sMovie.getPosterPath())
             .into(imageView2);
     System.out.println("poster path : " + sMovie.getPosterPath());
     Title.setText(sMovie.getTitle());
     releaseDate.setText(sMovie.getReleaseDate());
     voteAvg.setText(sMovie.getVoteAverage().toString());
     plotView.setText(sMovie.getOverview());
     voteCount.setText(sMovie.getVoteCount() + " votes");
     //reviews.setText(sMovie.getReviews().getResults().toString());
     key = sMovie.getVideos().getResults().get(0).getKey();
     Glide.with(getContext())
             .load("https://img.youtube.com/vi/" + key + "/hqdefault.jpg")
             .fitCenter()
             .into(trailerview);
     System.out.println("trailer : " + "http://img.youtube.com/vi/" + key + "/hqdefault.jpg");
     viewtrailer.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/watch?v=" + key));
             startActivity(intent1);
         }
     });

     FetchReview task1 = new FetchReview();
     reviews.setText("");
     try {
         resultJSON = task1.execute(movie_id).get();
         if (resultJSON != null) {
             JSONObject movie = new JSONObject(resultJSON);
             JSONArray movieDetails = movie.getJSONArray("results");
             for (int i = 0; i <=5; i++) {
                 JSONObject mov_reviews = movieDetails.getJSONObject(i);
                 author[i] = mov_reviews.getString("author");
                 review[i] = mov_reviews.getString("content");
                 reviews.append("\nReview By - "+author[i]+"\n"+review[i]+"\n");
                 reviews.append("------------------------------------------------------------------------------------\n");
             }
         }
     }
     catch (InterruptedException e) {
         e.printStackTrace();
     } catch (ExecutionException e) {
         e.printStackTrace();
     } catch (JSONException e) {
         e.printStackTrace();
     }

 }
}