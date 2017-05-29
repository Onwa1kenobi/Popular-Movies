package com.julius.popularmovies.movies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class MoviesContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMathcher = buildUriMatcher();
    private MoviesDBHelper mMoviesDBHelper;

    public MoviesContentProvider() {
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieListContract.AUTHORITY, MovieListContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieListContract.AUTHORITY, MovieListContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        final SQLiteDatabase sqLiteDatabase = mMoviesDBHelper.getWritableDatabase();

        int match = sUriMathcher.match(uri);

        int deletedRows;

        switch (match) {
            case MOVIE_WITH_ID:
                // Using selection and selectionArgs
                // URI: content://<authority>/movies/#
                String moviesDBID = uri.getPathSegments().get(1);

                // Selection is the moviesDB id column = ?, and the selectionArgs is the unique id
                String mSelection = MovieListContract.MovieListEntry.COLUMN_MOVIE_ID + "=?";
                String[] mSelectionArgs = new String[]{moviesDBID};

                deletedRows = sqLiteDatabase.delete(MovieListContract.MovieListEntry.TABLE_NAME,
                        mSelection,
                        mSelectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return deletedRows;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        final SQLiteDatabase sqLiteDatabase = mMoviesDBHelper.getWritableDatabase();

        int match = sUriMathcher.match(uri);

        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = sqLiteDatabase.insert(MovieListContract.MovieListEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    // Successfully added new row
                    returnUri = ContentUris.withAppendedId(MovieListContract.MovieListEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert new row " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        Context context = getContext();
        mMoviesDBHelper = new MoviesDBHelper(context);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        final SQLiteDatabase sqLiteDatabase = mMoviesDBHelper.getReadableDatabase();

        int match = sUriMathcher.match(uri);

        Cursor returnCursor;

        switch (match) {
            case MOVIES:
                returnCursor = sqLiteDatabase.query(MovieListContract.MovieListEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case MOVIE_WITH_ID:
                // Using selection and selectionArgs
                // URI: content://<authority>/movies/#
                String moviesDBID = uri.getPathSegments().get(1);

                // Selection is the moviesDB id column = ?, and the selectionArgs is the unique id
                String mSelection = MovieListContract.MovieListEntry.COLUMN_MOVIE_ID + "=?";
                String[] mSelectionArgs = new String[]{moviesDBID};

                returnCursor = sqLiteDatabase.query(MovieListContract.MovieListEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
