package com.julius.popularmovies.movies;

import com.julius.popularmovies.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ameh on 14/05/2017.
 */
interface MovieService {

    @GET(Constants.MOVIE_DB_API_URL + Constants.SORT_BY_POPULARITY)
    Call<MoviesDBResponse> getPopularMovies(
            @Query(value = "api_key", encoded = true) String apiKey,
            @Query("page") int pageNumber
    );

    @GET(Constants.MOVIE_DB_API_URL + Constants.SORT_BY_TOP_RATED)
    Call<MoviesDBResponse> getTopRatedMovies(
            @Query(value = "api_key", encoded = true) String apiKey,
            @Query("page") int pageNumber
    );

}
