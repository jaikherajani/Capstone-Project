package com.example.jaikh.movies;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jaikh on 04-01-2017.
 */

public class FetchReview extends AsyncTask<Long, Void, String> {
    public String apiKey= "9f51fc0657294458eba8b6a2080ac00f";
    private final String LOG_TAG = FetchReview.class.getSimpleName();
    URL url;
    public String movie_id;
    @Override
    protected String doInBackground(Long... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String MovieDBjson = null;
        Long movie_id = params[0];

        try {
            // Construct the URL for the theMoviedb query
            // Possible parameters are avaiable at TMDB's API page, at
            // http://api.themoviedb.org/3/discover/movie?api_key=[API KEY]
                url = new URL("http://api.themoviedb.org/3/movie/"+movie_id+"/reviews?&api_key=" + apiKey);
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            MovieDBjson = buffer.toString();
            Log.d("result", MovieDBjson);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return MovieDBjson;
    }
}
