package com.example.jaikh.movies.ui;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jaikh.movies.network.ApiClient;
import com.example.jaikh.movies.network.ApiInterface;
import com.example.jaikh.movies.ui.widget.AppWidget;
import com.example.jaikh.movies.BuildConfig;
import com.example.jaikh.movies.network.FetchReview;
import com.example.jaikh.movies.model.Movie;
import com.example.jaikh.movies.database.MovieProvider;
import com.example.jaikh.movies.R;
import com.example.jaikh.movies.model.SingleMovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaikh on 09-12-2016.
 */

public class DetailFragment extends Fragment {

    private final static String API_KEY = BuildConfig.API_KEY;
    public TextView plotView, voteAvg, releaseDate, Title, reviews, voteCount, status;
    public ImageView imageView, imageView2, trailerview;
    public Toolbar myToolbar;
    public CollapsingToolbarLayout appbar;
    public String key;
    public long movie_id;
    public String[] review = new String[10];
    public String[] author = new String[10];
    FloatingActionButton viewtrailer;
    private Movie movie;
    private SingleMovie sMovie;
    private String resultJSON = null;
    private Cursor cursor;
    private boolean flag = false;
    private Context mContext;
    private Snackbar snackbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.movie_detail, container, false);
        mContext = rootView.getContext();
        Title = (TextView) rootView.findViewById(R.id.name);
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
        isFavorite();

        final FloatingActionButton share = (FloatingActionButton) rootView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Concatenated String " + mContext.getResources().getString(R.string.i_share) + movie_id);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mContext.getResources().getString(R.string.i_share) + movie_id);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share Movie with..."));
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        if (flag) {
            fab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    flag = false;
                    fab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_border));
                    mContext.getContentResolver().delete(MovieProvider.CONTENT_URI, Long.toString(movie_id), null);
                    Snackbar.make(view, R.string.sb_removed_favorite, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    AppWidgetManager awm = AppWidgetManager.getInstance(mContext);
                    Intent intent = new Intent(mContext, AppWidget.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    getActivity().sendBroadcast(intent);
                } else {
                    if (isNetworkAvailable()) {
                        if (sMovie.getPosterPath() != null) {
                            flag = true;
                            fab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_favorite));
                            ContentValues values = new ContentValues();
                            values.put("movie_id", movie_id);
                            values.put("poster_path", sMovie.getPosterPath());
                            getActivity().getContentResolver().insert(MovieProvider.CONTENT_URI, values);
                            Snackbar.make(view, R.string.sb_saved_favorite, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            AppWidgetManager awm = AppWidgetManager.getInstance(mContext);
                            Intent intent = new Intent(mContext, AppWidget.class);
                            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                            getActivity().sendBroadcast(intent);
                        } else {
                            Snackbar.make(view, R.string.sb_problem_saving_favorite, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } else {
                        snackbar = Snackbar.make(view, R.string.sb_no_internet, Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackbar.dismiss();
                            }
                        }).show();
                    }
                }
            }
        });

        myToolbar = (Toolbar) rootView.findViewById(R.id.my_toolbar);

        if (!MainActivity.tablet) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        appbar = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);

        //System.out.println(movie.getId());
        getData(movie_id);
        return rootView;
    }

    void getData(long movie_id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SingleMovie> call = apiService.getMovieDetails(movie_id, API_KEY, "videos");
        call.enqueue(new Callback<SingleMovie>() {
            @Override
            public void onResponse(Call<SingleMovie> call, Response<SingleMovie> response) {
                int statusCode = response.code();
                sMovie = response.body();
                displayData(sMovie);
                System.out.println("sMovie : " + sMovie.getId());
                System.out.println("single movie " + call.request().url());
            }

            @Override
            public void onFailure(Call<SingleMovie> call, Throwable t) {
                // Log error here since request failed
                //Log.e("error : ", t.toString());
                Log.e("error : ", t.getMessage());
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    void displayData(SingleMovie sMovie) {
        appbar.setTitle(sMovie.getTitle());

        System.out.println("sMovie : " + sMovie.getTitle());
        //Toast.makeText(getActivity(), "Showing " + sMovie.getTitle(), Toast.LENGTH_SHORT).show();
        status.setText(sMovie.getStatus());
        Glide.with(mContext)
                .load("https://image.tmdb.org/t/p/w780/" + sMovie.getBackdropPath())
                .into(imageView);
        System.out.println("backdrop path : " + sMovie.getBackdropPath());
        Glide.with(mContext)
                .load("https://image.tmdb.org/t/p/w185/" + sMovie.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView2);
        System.out.println("https://image.tmdb.org/t/p/w185/" + sMovie.getPosterPath());
        Title.setText(sMovie.getTitle());
        releaseDate.setText(sMovie.getReleaseDate());
        voteAvg.setText(sMovie.getVoteAverage().toString());
        plotView.setText(sMovie.getOverview());
        voteCount.setText(sMovie.getVoteCount() + " votes");
        //reviews.setText(sMovie.getReviews().getResults().toString());
        if (sMovie.getVideo()) {
            key = sMovie.getVideos().getResults().get(0).getKey();
            Glide.with(mContext)
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
        } else {
            key = null;
            viewtrailer.setVisibility(View.GONE);
            trailerview.setVisibility(View.GONE);
        }


        FetchReview task1 = new FetchReview();
        reviews.setText("");
        try {
            resultJSON = task1.execute(movie_id).get();
            if (resultJSON != null) {
                JSONObject movie = new JSONObject(resultJSON);
                JSONArray movieDetails = movie.getJSONArray("results");
                for (int i = 0; i <= 5; i++) {
                    JSONObject mov_reviews = movieDetails.getJSONObject(i);
                    author[i] = mov_reviews.getString("author");
                    review[i] = mov_reviews.getString("content");
                    reviews.append("\nReview By - " + author[i] + "\n" + review[i] + "\n");
                    reviews.append("------------------------------------------------------------------------------------\n");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void isFavorite() {
        flag = false;
        cursor = mContext.getContentResolver().query(MovieProvider.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getLong(0) == movie_id) {
                    flag = true;
                }
            } while (cursor.moveToNext());
        }
    }
}