package com.julius.popularmovies.movies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.julius.popularmovies.models.Movie;
import com.julius.popularmovies.movies.data.MovieListContract;
import com.julius.popularmovies.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ameh on 22/05/2017.
 */

public class MovieDetailsPresenter implements MoviesContract.MovieDetailsInterface {

    private final MoviesContract.MovieDetailsView movieDetailsView;
    private final MovieService movieService;
    private ContentResolver contentResolver;

    MovieDetailsPresenter(MoviesContract.MovieDetailsView movieDetailsView, MovieService movieService, ContentResolver contentResolver) {
        this.movieDetailsView = movieDetailsView;
        this.movieService = movieService;
        this.contentResolver = contentResolver;
    }

    @Override
    public void getMovieDetails(long id) {
        movieService.getMovieDetails((int) id, Constants.MOVIE_DB_API_KEY, Constants.MOVIE_DETAILS_APPEND)
                .enqueue(new Callback<MoviesDBResponse>() {
                    @Override
                    public void onResponse(Call<MoviesDBResponse> call, Response<MoviesDBResponse> response) {
                        if (response.isSuccessful()) {
                            movieDetailsView.displayMovieDetails(response.body().getTrailers().getMovieTrailers(), response.body().getReviews().getMovieReviews());
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesDBResponse> call, Throwable throwable) {
                        movieDetailsView.displayErrorMessage(throwable.getMessage());
                    }
                });
    }

    @Override
    public void saveMovieToFavourites(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieListContract.MovieListEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieListContract.MovieListEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieListContract.MovieListEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieListContract.MovieListEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieListContract.MovieListEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieListContract.MovieListEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

        Uri uri = contentResolver.insert(MovieListContract.MovieListEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            movieDetailsView.displayToastMessage("Added to favourites");
        }
    }

    @Override
    public void deleteMovieFromFavourites(int movieId) {
        Uri uri = MovieListContract.MovieListEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(movieId)).build();

        int i = contentResolver.delete(uri, null, null);

        if (i > 0) {
            movieDetailsView.displayToastMessage("Removed from favourites");
        } else {
            movieDetailsView.displayToastMessage("An error occurred while deleting");
        }
    }

    @Override
    public boolean isMovieInFavourites(int movieId) {
        Cursor cursor = null;
        try {
            Uri uri = MovieListContract.MovieListEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(Integer.toString(movieId)).build();
            cursor = contentResolver.query(uri,
                    null, null, null, null);
            return cursor != null && cursor.getCount() >= 1;
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            return false;
        } finally {
            cursor.close();
        }
    }
}
