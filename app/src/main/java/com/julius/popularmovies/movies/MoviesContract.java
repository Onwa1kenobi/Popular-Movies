package com.julius.popularmovies.movies;

import android.content.ContentResolver;
import android.widget.ImageView;

import com.julius.popularmovies.models.Movie;
import com.julius.popularmovies.models.Review;
import com.julius.popularmovies.models.Trailer;
import com.julius.popularmovies.movies.data.MoviesDBHelper;

import java.util.List;

/**
 * Created by ameh on 13/05/2017.
 */
class MoviesContract {

    interface MoviesView {
        void displayMovies(List<Movie> movies);

        void displayErrorMessage(String message);

        void displayMovieDetails(Movie movie, ImageView image);
    }

    interface MovieDetailsView {
        void displayMovieDetails(List<Trailer> trailers, List<Review> reviews);

        void displayErrorMessage(String message);

        void displayToastMessage(String message);
    }

    interface MovieDetailsInterface {
        void getMovieDetails(long id);

        void saveMovieToFavourites(Movie movie);

        void deleteMovieFromFavourites(int movieId);

        boolean isMovieInFavourites(int movieId);
    }

    interface MovieInterface {
        void getMovieList(int page, String sortOrder);

        void getPopularMovies(int page);

        void getTopRatedMovies(int page);

        void getFavouriteMovies(MoviesDBHelper dbHelper, ContentResolver contentResolver);

        void getMovie(Movie movie, ImageView image);
    }
}
