package com.example.jaikh.movies;

import android.database.Cursor;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private final static String API_KEY = "9f51fc0657294458eba8b6a2080ac00f";
    private List<Movie> movies = new ArrayList<>();
    private List<Movie> favMovies = new ArrayList<>();
    private MoviesAdapter moviesAdapter;
    private RecyclerView recyclerView;
    private ImageView poster;
    private DBHelper databaseHelper;
    private Movie fMovie;
    public Long[] b = new Long[20];
    public int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DBHelper(this);

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
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("error : ", t.toString());
            }
        });
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
    }

    private void displayFavorites() {
        Cursor cursor = databaseHelper.getData();
        a=0;
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        if (cursor.moveToFirst()) {
            do {
                b[a] = cursor.getLong(0);
                Call<Movie> call = apiService.getMovieDetails(b[a], API_KEY);
                System.out.println("Fav MovieId : "+b[a]);
                System.out.println("count1 "+a);
                call.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        int statusCode = response.code();
                        //movie1 = response.body().getResult();
                        fMovie = response.body();
                        System.out.println("count2 "+a);
                        favMovies.add(a,fMovie);
                        System.out.println("fMovie : "+ fMovie.getTitle());
                        System.out.println("favorite movie " + call.request().url());
                        recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),favMovies));
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        // Log error here since request failed
                        //Log.e("error : ", t.toString());
                        Log.e("error : ", t.getMessage());
                    }
                });
                System.out.println("count3 "+a);
                a++;
            } while (cursor.moveToNext());
        }
        Snackbar.make(findViewById(R.id.fragment), "Showing Favorites", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

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
                displayPopular();
                break;
            case R.id.action_most_rated :
                displayRated();
                break;
            case R.id.action_favorite :
                displayFavorites();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
