package com.example.jaikh.movies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String API_KEY = "9f51fc0657294458eba8b6a2080ac00f";
    private List<Movie> movies = new ArrayList<>();
    private RecyclerView recyclerView;
    private DBHelper databaseHelper;
    private String status = "popular";
    private Movie fMovie;
    private Snackbar snackbar;
    public Long[] b = new Long[20];
    public String[] url = new String[20];
    public int a;
    public Cursor favCursor;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHelper = new DBHelper(this);
        getSupportLoaderManager().initLoader(0,null,this);
        //"9f51fc0657294458eba8b6a2080ac00f"
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.hasFixedSize();
        displayPopular();
    }

    private void displayPopular() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("error : ", t.toString());
            }
        });
        snackbar = Snackbar.make(findViewById(R.id.fragment), R.string.sb_now_showing_popular, Snackbar.LENGTH_LONG);
        snackbar.setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }

    private void displayRated() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("error : ", t.toString());
            }
        });
        snackbar = Snackbar.make(findViewById(R.id.fragment), R.string.sb_now_showing_rated, Snackbar.LENGTH_LONG);
        snackbar.setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }

    private void displayFavorites() {
        movies.clear();
        a=0;
        if (favCursor.moveToFirst()) {
            do {
                url[a] = favCursor.getString(1);
                b[a] = favCursor.getLong(0);
                fMovie = new Movie(url[a],b[a]);
                movies.add(a,fMovie);
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
            } while (favCursor.moveToNext());
            snackbar = Snackbar.make(findViewById(R.id.fragment), R.string.sb_now_showing_favorites, Snackbar.LENGTH_LONG);
            snackbar.setAction("dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }
        else
        {
            snackbar = Snackbar.make(findViewById(R.id.fragment), R.string.sb_no_favorites, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Okay!", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    }).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.action_popular :
                status = "popular";
                displayPopular();
                break;
            case R.id.action_most_rated :
                status = "most_rated";
                displayRated();
                break;
            case R.id.action_favorite :
                status = "favorites";
                displayFavorites();
                break;
            case R.id.action_feedback :
                startActivity(new Intent(this,Feedback.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri = MovieProvider.CONTENT_URI;
        return new CursorLoader(this,contentUri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
