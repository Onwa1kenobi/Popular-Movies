package com.julius.popularmovies.movies;

import com.google.gson.annotations.SerializedName;
import com.julius.popularmovies.models.Movie;

import java.util.List;

/**
 * Created by ameh on 14/05/2017.
 */

class MoviesDBResponse {
    @SerializedName("results")
    private List<Movie> items;

    public MoviesDBResponse() {

    }

    List<Movie> getMoviesList() {
        return items;
    }
}
