package com.example.jaikh.movies.network;

/**
 * Created by jaikh on 09-12-2016.
 */

import com.example.jaikh.movies.model.Movie;
import com.example.jaikh.movies.model.MoviesResponse;
import com.example.jaikh.movies.model.SingleMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<SingleMovie> getMovieDetails(@Path("id") Long id, @Query("api_key") String apiKey, @Query("append_to_response") String params);

}
