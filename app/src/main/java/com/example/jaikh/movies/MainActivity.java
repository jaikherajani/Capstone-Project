package com.example.jaikh.movies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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

    private String API_KEY = BuildConfig.API_KEY;
    private List<Movie> movies = new ArrayList<>();
    private RecyclerView recyclerView;
    private DBHelper databaseHelper;
    private String status;
    private Movie fMovie;
    private Snackbar snackbar;
    public Long[] b = new Long[20];
    public String[] url = new String[20];
    public int a;
    public static boolean tablet=false;
    public Cursor favCursor;
    private static final String BUNDLE_RECYCLER_LAYOUT = "MainActivity.recycler.layout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHelper = new DBHelper(this);
        getSupportLoaderManager().initLoader(0,null,this);
        if(findViewById(R.id.containerDetails)!=null)
        {
            tablet=true;
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.hasFixedSize();
        if (isNetworkAvailable())
        displayPopular(null);
        else
        {
            snackbar = Snackbar.make(findViewById(R.id.fragment), R.string.sb_no_internet, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save the user's current game state
        savedInstanceState.putString("STATUS",status);
        System.out.println("STATUS SENT IS "+status);
        savedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        if(findViewById(R.id.containerDetails)!=null)
            tablet=true;
        else
            tablet = false;
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            String new_status = savedInstanceState.getString("STATUS");
            System.out.println("STATUS RECEIVED IS "+new_status);
            switch (new_status)
            {
                case "popular":
                    displayPopular(savedRecyclerLayoutState);
                    break;
                case "most_rated":
                    displayRated(savedRecyclerLayoutState);
                    break;
                case "favorites":
                    displayFavorites(savedRecyclerLayoutState);
                    break;
            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void displayPopular(final Parcelable position) {
        status="popular";
        if (!isNetworkAvailable())
        {
            snackbar = Snackbar.make(findViewById(R.id.fragment), R.string.sb_no_internet, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }

        movies.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies,getSupportFragmentManager()));
                if(position !=null)
                    recyclerView.getLayoutManager().onRestoreInstanceState(position);
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

    private void displayRated(final Parcelable position) {
        if (!isNetworkAvailable())
        {
            snackbar = Snackbar.make(findViewById(R.id.fragment), R.string.sb_no_internet, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }
        status="most_rated";
        movies.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                movies = response.body().getResults();
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies, getSupportFragmentManager()));
                if(position !=null)
                    recyclerView.getLayoutManager().onRestoreInstanceState(position);
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

    private void displayFavorites(final Parcelable position) {
        if (!isNetworkAvailable())
        {
            snackbar = Snackbar.make(findViewById(R.id.fragment), R.string.sb_no_internet, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }
        status="favorites";
        movies.clear();
        a=0;
        if (favCursor.moveToFirst()) {
            do {
                url[a] = favCursor.getString(1);
                b[a] = favCursor.getLong(0);
                fMovie = new Movie(url[a],b[a]);
                movies.add(a,fMovie);
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies, getSupportFragmentManager()));
                if(position !=null)
                    recyclerView.getLayoutManager().onRestoreInstanceState(position);
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
            displayPopular(null);
            snackbar = Snackbar.make(findViewById(R.id.fragment), R.string.sb_no_favorites, Snackbar.LENGTH_LONG);
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
                displayPopular(null);
                break;
            case R.id.action_most_rated :
                status = "most_rated";
                displayRated(null);
                break;
            case R.id.action_favorite :
                status = "favorites";
                displayFavorites(null);
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
