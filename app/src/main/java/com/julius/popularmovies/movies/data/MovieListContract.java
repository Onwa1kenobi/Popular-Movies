package com.julius.popularmovies.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ameh on 25/05/2017.
 */

public class MovieListContract {

    static final String AUTHORITY = "com.julius.popularmovies";
    static final String PATH_MOVIES = "movies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class MovieListEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        static final String TABLE_NAME = "movies";

    }
}
