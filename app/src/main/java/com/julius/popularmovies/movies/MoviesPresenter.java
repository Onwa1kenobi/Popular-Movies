package com.julius.popularmovies.movies;

import android.widget.ImageView;

import com.julius.popularmovies.models.Movie;
import com.julius.popularmovies.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ameh on 13/05/2017.
 */
class MoviesPresenter implements MoviesContract.MovieInterface {

    private final MoviesContract.View moviesView;
    private final MovieService movieService;

    MoviesPresenter(MoviesContract.View moviesView, MovieService movieService) {
        this.moviesView = moviesView;
        this.movieService = movieService;
    }

    @Override
    public void getMovieList(int page, String sortOrder) {
        switch (sortOrder) {
            case Constants.SORT_BY_POPULARITY:
                getPopularMovies(page);
                break;

            case Constants.SORT_BY_TOP_RATED:
                getTopRatedMovies(page);
                break;
        }
    }

    @Override
    public void getPopularMovies(int page) {
        movieService.getPopularMovies(Constants.MOVIE_DB_API_KEY, page).enqueue(new Callback<MoviesDBResponse>() {
            @Override
            public void onResponse(Call<MoviesDBResponse> call, Response<MoviesDBResponse> response) {
                if (response.isSuccessful()) {
                    moviesView.displayMovies(response.body().getMoviesList());
                }
            }

            @Override
            public void onFailure(Call<MoviesDBResponse> call, Throwable throwable) {
                moviesView.displayErrorMessage(throwable.getMessage());
            }
        });
    }

    @Override
    public void getTopRatedMovies(int page) {
        movieService.getTopRatedMovies(Constants.MOVIE_DB_API_KEY, page).enqueue(new Callback<MoviesDBResponse>() {
            @Override
            public void onResponse(Call<MoviesDBResponse> call, Response<MoviesDBResponse> response) {
                if (response.isSuccessful()) {
                    moviesView.displayMovies(response.body().getMoviesList());
                }
            }

            @Override
            public void onFailure(Call<MoviesDBResponse> call, Throwable throwable) {
                moviesView.displayErrorMessage(throwable.getMessage());
            }
        });
    }

    @Override
    public void getMovie(Movie movie, ImageView image) {
        moviesView.displayMovieDetails(movie, image);
    }
}
