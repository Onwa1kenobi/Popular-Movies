package com.julius.popularmovies.movies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.julius.popularmovies.R;
import com.julius.popularmovies.utils.Constants;

public class MoviesActivity extends AppCompatActivity {

    private static final String STATE_MOVIES = "saved_state_movies";
    private static final String STATE_ADAPTER_POSITION = "saved_state_adapter_position";
    private static final String STATE_MOVIES_PAGE = "saved_state_movies_page";
    private static final String STATE_SORT_ORDER = "saved_state_sort_order";
    private static final String STATE_MENU_SELECTED = "saved_state_menu_position";
    private static final String STATE_LOADING = "saved_state_loading";

    private MoviesPresenter mPresenter;
    private MoviesAdapter mAdapter;
    private SwipeRefreshLayout mSrl;
    private Toolbar mToolbar;
    private MovieSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ProgressBar mProgressBar;
    private TextView mErrorText;
    private MoviesFragment mMoviesFragment;

    private String mSortOrder = Constants.SORT_BY_POPULARITY;
    private int mMenuItemId = -1;
    private int mCurrentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new MovieSectionsPagerAdapter(this, getSupportFragmentManager());

        mMoviesFragment = (MoviesFragment) mSectionsPagerAdapter.getItem(0);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1, true);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSmoothScrollingEnabled(true);

//        mPresenter = new MoviesPresenter(this, MoviesInjector.provideMovieService(Constants.MOVIE_DB_API_URL));
//
//        int mAdapterPosition = savedInstanceState != null
//                ? savedInstanceState.getInt(STATE_ADAPTER_POSITION, -1)
//                : -1;
//
//        mCurrentPage = savedInstanceState != null
//                ? savedInstanceState.getInt(STATE_MOVIES_PAGE, 1)
//                : 1;
//
//        mMenuItemId = savedInstanceState != null
//                ? savedInstanceState.getInt(STATE_MENU_SELECTED, -1)
//                : -1;
//
//        mSortOrder = savedInstanceState != null
//                ? savedInstanceState.getString(STATE_SORT_ORDER, Constants.SORT_BY_POPULARITY)
//                : Constants.SORT_BY_POPULARITY;
//
//        List restoredMovies = savedInstanceState != null
//                ? savedInstanceState.getParcelableArrayList(STATE_MOVIES)
//                : new ArrayList<Movie>(0);
//
//        isLoading = savedInstanceState != null && savedInstanceState.getBoolean(STATE_LOADING);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_filter_list));
//        ViewCompat.setElevation(mToolbar, getResources().getDimension(R.dimen.toolbar_elevation));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

//        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
//
//        mAdapter = new MoviesAdapter(restoredMovies, mPresenter);
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.movies_columns), GridLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(gridLayoutManager);
//        recyclerView.setAdapter(mAdapter);
//        if (mAdapterPosition != -1) recyclerView.scrollToPosition(mAdapterPosition);
//
//        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
//            @Override
//            protected void loadMoreItems() {
//                mCurrentPage += 1;
//                isLoading = true;
//
//                mPresenter.getMovieList(mCurrentPage, mSortOrder);
//            }
//
//            @Override
//            public boolean isLastPage() {
//                return isLastPage;
//            }
//
//            @Override
//            public boolean isLoading() {
//                return isLoading;
//            }
//        });
//
//        mSrl = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPresenter.getMovieList(1, mSortOrder);
//            }
//        });
//
//        if (isLoading) {
//            if (mAdapter.getItemCount() <= 0) {
//                mProgressBar.setVisibility(View.VISIBLE);
//            } else {
//                mSrl.setRefreshing(true);
//            }
//            mPresenter.getMovieList(1, mSortOrder);
//        } else if (savedInstanceState != null && mAdapter.getItemCount() <= 0) {
//            mProgressBar.setVisibility(View.VISIBLE);
//            mPresenter.getMovieList(mCurrentPage, mSortOrder);
//        }
//
//        if (savedInstanceState == null) {
//            mProgressBar.setVisibility(View.VISIBLE);
//            mPresenter.getMovieList(mCurrentPage, mSortOrder);
//        }
//
//        mErrorText = (TextView) findViewById(R.id.error_text);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList(STATE_MOVIES, new ArrayList<>(mAdapter.getItems()));
//        outState.putInt(STATE_ADAPTER_POSITION, mAdapter.getCurrentViewPosition());
//        outState.putInt(STATE_MOVIES_PAGE, mCurrentPage);
//        outState.putInt(STATE_MENU_SELECTED, mMenuItemId);
//        outState.putString(STATE_SORT_ORDER, mSortOrder);
//        outState.putBoolean(STATE_LOADING, mProgressBar.getVisibility() == View.VISIBLE || mSrl.isRefreshing());
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
        return true;
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
//        mProgressBar.setVisibility(View.VISIBLE);
//        resetLoadingElements();
//        mPresenter.getMovieList(mCurrentPage, mSortOrder);
        return super.onOptionsItemSelected(item);
    }

//    private void resetLoadingElements() {
//        if (mSrl.isRefreshing()) {
//            mSrl.setRefreshing(false);
//        }
//        mAdapter.getItems().clear();
//        mAdapter.notifyDataSetChanged();
//        mCurrentPage = 1;
//        isLastPage = false;
//        isLoading = false;
//    }
//
//    @Override
//    public void displayMovies(final List<Movie> movies) {
//        mProgressBar.setVisibility(View.GONE);
//        if (mSrl.isRefreshing()) {
//            mSrl.setRefreshing(false);
//            resetLoadingElements();
//        }
//        isLoading = false;
//
//        mAdapter.refill(movies);
//
//        if (mAdapter.getItemCount() <= 0) {
//            mErrorText.setVisibility(View.VISIBLE);
//        } else {
//            mErrorText.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void displayErrorMessage(String message) {
//        mProgressBar.setVisibility(View.GONE);
//        if (mSrl.isRefreshing()) {
//            mSrl.setRefreshing(false);
//        }
//        isLoading = false;
//
//        if (mAdapter.getItemCount() <= 0) {
//            mErrorText.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void displayMovieDetails(Movie movie, ImageView imageView) {
//        Intent intent = new Intent(this, MovieDetailActivity.class);
//
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(Constants.MOVIE, movie);
//        bundle.putString(Constants.MOVIE_POSTER_PATH, Constants.MOVIE_POSTER_BASE_URL + movie.getPosterPath());
//        bundle.putString(Constants.IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(imageView));
//        intent.putExtra(Constants.MOVIE, bundle);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ImageTransformTransition imageTransformTransition = new ImageTransformTransition();
//            getWindow().setSharedElementExitTransition(imageTransformTransition);
//            getWindow().setExitTransition(new Fade());
//        }
//
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                this,
//                imageView,
//                ViewCompat.getTransitionName(imageView));
//        startActivity(intent, options.toBundle());
//    }
}
