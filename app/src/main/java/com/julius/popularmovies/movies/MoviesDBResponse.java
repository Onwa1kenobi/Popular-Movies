package com.julius.popularmovies.movies;

import com.google.gson.annotations.SerializedName;
import com.julius.popularmovies.models.Movie;
import com.julius.popularmovies.models.Review;
import com.julius.popularmovies.models.Trailer;

import java.util.List;

/**
 * Created by ameh on 14/05/2017.
 */

class MoviesDBResponse {
    @SerializedName("results")
    private List<Movie> items;
    @SerializedName("reviews")
    private GetReviews reviews;
    @SerializedName("videos")
    private GetTrailers trailers;

    public MoviesDBResponse() {

    }

    List<Movie> getMoviesList() {
        return items;
    }

    GetTrailers getTrailers() {
        return trailers;
    }

    GetReviews getReviews() {
        return reviews;
    }

    class GetTrailers {
        @SerializedName("results")
        private List<Trailer> trailers;

        List<Trailer> getMovieTrailers() {
            return trailers;
        }
    }

    class GetReviews {
        @SerializedName("results")
        private List<Review> reviews;

        List<Review> getMovieReviews() {
            return reviews;
        }
    }

}
