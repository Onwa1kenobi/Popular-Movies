package com.julius.popularmovies.movies;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.julius.popularmovies.R;
import com.julius.popularmovies.models.Movie;
import com.julius.popularmovies.utils.Constants;
import com.julius.popularmovies.utils.ImageTransformTransition;
import com.julius.popularmovies.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment implements MoviesContract.MoviesView {

    private static final String STATE_MOVIES = "saved_state_movies";
    private static final String STATE_ADAPTER_POSITION = "saved_state_adapter_position";
    private static final String STATE_MOVIES_PAGE = "saved_state_movies_page";
    private static final String STATE_SORT_ORDER = "saved_state_sort_order";
    private static final String STATE_MENU_SELECTED = "saved_state_menu_position";
    private static final String STATE_LOADING = "saved_state_loading";

    private MoviesPresenter mPresenter;
    private MoviesAdapter mAdapter;
    private SwipeRefreshLayout mSrl;
    private ProgressBar mProgressBar;
    private TextView mErrorText;

    private String mSortOrder = Constants.SORT_BY_POPULARITY;
    private int mMenuItemId = -1;
    private int mCurrentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;


    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);

        mPresenter = new MoviesPresenter(this, MoviesInjector.provideMovieService(Constants.MOVIE_DB_API_URL));

        int mAdapterPosition = savedInstanceState != null
                ? savedInstanceState.getInt(STATE_ADAPTER_POSITION, -1)
                : -1;

        mCurrentPage = savedInstanceState != null
                ? savedInstanceState.getInt(STATE_MOVIES_PAGE, 1)
                : 1;

        mMenuItemId = savedInstanceState != null
                ? savedInstanceState.getInt(STATE_MENU_SELECTED, -1)
                : -1;

        mSortOrder = savedInstanceState != null
                ? savedInstanceState.getString(STATE_SORT_ORDER, Constants.SORT_BY_POPULARITY)
                : Constants.SORT_BY_POPULARITY;

        List restoredMovies = savedInstanceState != null
                ? savedInstanceState.getParcelableArrayList(STATE_MOVIES)
                : new ArrayList<Movie>(0);

        isLoading = savedInstanceState != null && savedInstanceState.getBoolean(STATE_LOADING);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mAdapter = new MoviesAdapter(restoredMovies, mPresenter);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.movies_columns), GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);
        if (mAdapterPosition != -1) recyclerView.scrollToPosition(mAdapterPosition);

        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                mCurrentPage += 1;
                isLoading = true;

                mPresenter.getMovieList(mCurrentPage, mSortOrder);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        mSrl = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getMovieList(1, mSortOrder);
            }
        });

        if (isLoading) {
            if (mAdapter.getItemCount() <= 0) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mSrl.setRefreshing(true);
            }
            mPresenter.getMovieList(1, mSortOrder);
        } else if (savedInstanceState != null && mAdapter.getItemCount() <= 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            mPresenter.getMovieList(mCurrentPage, mSortOrder);
        }

        if (savedInstanceState == null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mPresenter.getMovieList(mCurrentPage, mSortOrder);
        }

        mErrorText = (TextView) view.findViewById(R.id.error_text);

        setRetainInstance(true);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, new ArrayList<>(mAdapter.getItems()));
        outState.putInt(STATE_ADAPTER_POSITION, mAdapter.getCurrentViewPosition());
        outState.putInt(STATE_MOVIES_PAGE, mCurrentPage);
        outState.putInt(STATE_MENU_SELECTED, mMenuItemId);
        outState.putString(STATE_SORT_ORDER, mSortOrder);
        outState.putBoolean(STATE_LOADING, mProgressBar.getVisibility() == View.VISIBLE || mSrl.isRefreshing());
    }

    public void resetLoadingElements(String sortOrder) {
        if (mSrl != null && mSrl.isRefreshing()) {
            mSrl.setRefreshing(false);
        }
        mAdapter.getItems().clear();
        mAdapter.notifyDataSetChanged();
        mCurrentPage = 1;
        isLastPage = false;
        isLoading = false;
        mProgressBar.setVisibility(View.VISIBLE);
        mSortOrder = sortOrder;
        mPresenter.getMovieList(mCurrentPage, mSortOrder);
    }

    @Override
    public void displayMovies(final List<Movie> movies) {
        mProgressBar.setVisibility(View.GONE);
        if (mSrl.isRefreshing()) {
            mSrl.setRefreshing(false);
            resetLoadingElements(mSortOrder);
        }
        isLoading = false;

        mAdapter.refill(movies);

        if (mAdapter.getItemCount() <= 0) {
            mErrorText.setVisibility(View.VISIBLE);
        } else {
            mErrorText.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayErrorMessage(String message) {
        mProgressBar.setVisibility(View.GONE);
        if (mSrl.isRefreshing()) {
            mSrl.setRefreshing(false);
        }
        isLoading = false;

        if (mAdapter.getItemCount() <= 0) {
            mErrorText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayMovieDetails(Movie movie, ImageView imageView) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.MOVIE, movie);
        bundle.putString(Constants.MOVIE_POSTER_PATH, Constants.MOVIE_POSTER_BASE_URL + movie.getPosterPath());
        bundle.putString(Constants.IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(imageView));
        intent.putExtra(Constants.MOVIE, bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImageTransformTransition imageTransformTransition = new ImageTransformTransition();
            getActivity().getWindow().setSharedElementExitTransition(imageTransformTransition);
            getActivity().getWindow().setExitTransition(new Fade());
        }

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                imageView,
                ViewCompat.getTransitionName(imageView));
        startActivity(intent, options.toBundle());
    }

}
