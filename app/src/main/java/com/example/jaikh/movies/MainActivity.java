package com.example.jaikh.movies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private final static String API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private List<Movie> movies = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private RecyclerView recyclerView;
    private ImageView poster;
    //private Movie movieObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TmdbMovies movies = new TmdbApi("9f51fc0657294458eba8b6a2080ac00f").getMovies();
        //moviesAdapter = new MoviesAdapter(movies);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.hasFixedSize();
        //moviesAdapter = new MoviesAdapter(getApplicationContext());
        //recyclerView.setAdapter(moviesAdapter);
        //moviesAdapter.displayPopular();
        //recyclerView.isClickable();
        //recyclerView.setAdapter(moviesAdapter);
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
                //recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.rv_item, getApplicationContext()));
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("error : ", t.toString());
            }
        });
    }
    /*private void displayRated() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                movies = response.body().getResults();
                //recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.rv_item, getApplicationContext()));
                //recyclerView.setAdapter(new MoviesAdapter(movies,getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("error : ", t.toString());
            }
        });
    }*/

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
                //displayPopular();
                break;
            case R.id.action_most_rated :
                //displayRated();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
