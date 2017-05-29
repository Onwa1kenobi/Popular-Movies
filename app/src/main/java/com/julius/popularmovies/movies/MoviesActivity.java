package com.julius.popularmovies.movies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.julius.popularmovies.R;
import com.julius.popularmovies.utils.Constants;

public class MoviesActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private MoviesFragment mMoviesFragment;

    private String mSortOrder = Constants.SORT_BY_POPULARITY;
    private int mMenuItemId = -1;
    private boolean hideMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieSectionsPagerAdapter mSectionsPagerAdapter = new MovieSectionsPagerAdapter(this, getSupportFragmentManager());

        mMoviesFragment = (MoviesFragment) mSectionsPagerAdapter.getItem(0);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0, true);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideMenu = position != 0;
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSmoothScrollingEnabled(true);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_filter_list));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mMenuItemId == -1) {
            return true;
        }

        switch (mMenuItemId) {
            case R.id.mode_sort_popularity:
                mToolbar.setSubtitle(getResources().getString(R.string.mode_sort_popularity));
                break;

            case R.id.mode_sort_top_rated:
                mToolbar.setSubtitle(getResources().getString(R.string.mode_sort_top_rated));
                break;
        }

        menu.findItem(mMenuItemId).setChecked(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return !hideMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mode_sort_popularity:
                item.setChecked(true);
                mToolbar.setSubtitle(getResources().getString(R.string.mode_sort_popularity));
                mSortOrder = Constants.SORT_BY_POPULARITY;
                break;

            case R.id.mode_sort_top_rated:
                item.setChecked(true);
                mToolbar.setSubtitle(getResources().getString(R.string.mode_sort_top_rated));
                mSortOrder = Constants.SORT_BY_TOP_RATED;
                break;
        }
        mMenuItemId = item.getItemId();

        mMoviesFragment.resetLoadingElements(mSortOrder);
        return super.onOptionsItemSelected(item);
    }
}
