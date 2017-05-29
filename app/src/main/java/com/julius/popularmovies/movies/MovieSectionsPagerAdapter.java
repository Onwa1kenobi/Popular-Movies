package com.julius.popularmovies.movies;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.julius.popularmovies.R;


/**
 * Created by ameh on 27/05/2017.
 */
public class MovieSectionsPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    private MoviesFragment moviesFragment;
    private FavouriteMoviesFragment favouriteMoviesFragment;

    public MovieSectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        moviesFragment = new MoviesFragment();

        favouriteMoviesFragment = new FavouriteMoviesFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return moviesFragment;
            case 1:
                return favouriteMoviesFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.movies_fragment_title);
            case 1:
                return mContext.getString(R.string.favourite_movies_fragment_title);
        }
        return null;
    }
}