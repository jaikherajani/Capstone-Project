package com.example.jaikh.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jaikh on 09-12-2016.
 */

public class MovieDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        Intent intent = getIntent();

        long movie_id = intent.getLongExtra("MOVIE_ID",0);
        Movie movie = (Movie)intent.getSerializableExtra("DATA_MOVIE");
        System.out.println("Received "+movie_id);

        Bundle mBundle = new Bundle();
        mBundle.putLong("MOVIE_ID",movie_id);
        mBundle.putSerializable("MOVIE",movie);
        MovieDetailFragment detailFragment = new MovieDetailFragment();
        detailFragment.setArguments(mBundle);
        setContentView(R.layout.activity_movie_detail);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerDetails, detailFragment).commit();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
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
