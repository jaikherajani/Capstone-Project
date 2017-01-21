package com.example.jaikh.movies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by jaikh on 09-12-2016.
 */

public class MovieDetail extends AppCompatActivity {
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        Intent intent = getIntent();

        long movie_id = intent.getLongExtra("MOVIE_ID", 0);
        Movie movie = (Movie) intent.getSerializableExtra("DATA_MOVIE");
        System.out.println("Received " + movie_id);

        Bundle mBundle = new Bundle();
        mBundle.putLong("MOVIE_ID", movie_id);
        mBundle.putSerializable("MOVIE", movie);
        MovieDetailFragment detailFragment = new MovieDetailFragment();
        detailFragment.setArguments(mBundle);
        setContentView(R.layout.activity_movie_detail);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerDetails, detailFragment).commit();

        if (!isNetworkAvailable()) {
            snackbar = Snackbar.make(findViewById(R.id.containerDetails), R.string.sb_no_internet, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
