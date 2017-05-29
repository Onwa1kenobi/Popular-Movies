package com.julius.popularmovies.movies;

import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ImageView;

import com.julius.popularmovies.models.Movie;
import com.julius.popularmovies.movies.data.MovieListContract;
import com.julius.popularmovies.movies.data.MoviesDBHelper;
import com.julius.popularmovies.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ameh on 13/05/2017.
 */
class MoviesPresenter implements MoviesContract.MovieInterface {

    private final MoviesContract.MoviesView moviesView;
    private final MovieService movieService;

    MoviesPresenter(MoviesContract.MoviesView moviesView, MovieService movieService) {
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
    public void getFavouriteMovies(MoviesDBHelper dbHelper, ContentResolver contentResolver) {
        Cursor cursor = null;
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            cursor = contentResolver.query(MovieListContract.MovieListEntry.CONTENT_URI,
                    null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Movie movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_MOVIE_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_RELEASE_DATE)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_VOTE_AVERAGE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_POSTER_PATH)));
                movies.add(movie);
                cursor.moveToNext();
            }
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        moviesView.displayMovies(movies);
    }

    @Override
    public void getMovie(Movie movie, ImageView image) {
        moviesView.displayMovieDetails(movie, image);
    }

}
