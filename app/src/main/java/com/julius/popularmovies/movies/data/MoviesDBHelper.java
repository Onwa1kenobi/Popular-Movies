package com.julius.popularmovies.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ameh on 25/05/2017.
 */

public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " +
                MovieListContract.MovieListEntry.TABLE_NAME + " (" +
                MovieListContract.MovieListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieListContract.MovieListEntry.COLUMN_MOVIE_ID + " LONG NOT NULL," +
                MovieListContract.MovieListEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieListContract.MovieListEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieListContract.MovieListEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MovieListContract.MovieListEntry.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL," +
                MovieListContract.MovieListEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieListContract.MovieListEntry.TABLE_NAME);
        onCreate(db);
    }
}
