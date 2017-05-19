package com.julius.popularmovies.movies;

import android.widget.ImageView;

import com.julius.popularmovies.models.Movie;

import java.util.List;

/**
 * Created by ameh on 13/05/2017.
 */
class MoviesContract {

    interface View {
        void displayMovies(List<Movie> movies);

        void displayErrorMessage(String message);

        void displayMovieDetails(Movie movie, ImageView image);
    }

    interface MovieInterface {
        void getMovieList(int page, String sortOrder);

        void getPopularMovies(int page);

        void getTopRatedMovies(int page);

        void getMovie(Movie movie, ImageView image);
    }
}
