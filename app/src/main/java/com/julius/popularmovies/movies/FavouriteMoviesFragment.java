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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteMoviesFragment extends Fragment implements MoviesContract.MoviesView {

    private static final String STATE_MOVIES = "saved_state_movies";
    private static final String STATE_ADAPTER_POSITION = "saved_state_adapter_position";
    private static final String STATE_LOADING = "saved_state_loading";

    private MoviesPresenter mPresenter;
    private MoviesAdapter mAdapter;
    private SwipeRefreshLayout mSrl;
    private ProgressBar mProgressBar;
    private TextView mErrorText;

    private boolean isLoading = false;


    public FavouriteMoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite_movies, container, false);

        mPresenter = new MoviesPresenter(this,
                MoviesInjector.provideMovieService(Constants.MOVIE_DB_API_URL));

        int mAdapterPosition = savedInstanceState != null
                ? savedInstanceState.getInt(STATE_ADAPTER_POSITION, -1)
                : -1;

        List restoredMovies = savedInstanceState != null
                ? savedInstanceState.getParcelableArrayList(STATE_MOVIES)
                : new ArrayList<Movie>(0);

        isLoading = savedInstanceState != null && savedInstanceState.getBoolean(STATE_LOADING);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        mAdapter = new MoviesAdapter(restoredMovies, mPresenter);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),
                getResources().getInteger(R.integer.movies_columns),
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);
        if (mAdapterPosition != -1) recyclerView.scrollToPosition(mAdapterPosition);

        mSrl = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getFavouriteMovies(getContext().getContentResolver());
            }
        });

        mErrorText = (TextView) view.findViewById(R.id.error_text);

        if (isLoading) {
            if (mAdapter.getItemCount() <= 0) {
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mSrl.setRefreshing(true);
            }
            mPresenter.getFavouriteMovies(getContext().getContentResolver());
        } else if (savedInstanceState != null && mAdapter.getItemCount() <= 0) {
            mProgressBar.setVisibility(View.VISIBLE);
            mPresenter.getFavouriteMovies(getContext().getContentResolver());
        }

        if (savedInstanceState == null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mPresenter.getFavouriteMovies(getContext().getContentResolver());
        }

        setRetainInstance(true);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, new ArrayList<>(mAdapter.getItems()));
        outState.putInt(STATE_ADAPTER_POSITION, mAdapter.getCurrentViewPosition());
        outState.putBoolean(STATE_LOADING,
                mProgressBar.getVisibility() == View.VISIBLE || mSrl.isRefreshing());
    }

    @Override
    public void onResume() {
        super.onResume();
        resetLoadingElements();
        mPresenter.getFavouriteMovies(getContext().getContentResolver());
    }

    public void resetLoadingElements() {
        if (mSrl.isRefreshing()) {
            mSrl.setRefreshing(false);
        }
        mAdapter.getItems().clear();
        mAdapter.notifyDataSetChanged();
        isLoading = false;
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayMovies(final List<Movie> movies) {
        if (mSrl.isRefreshing()) {
            mSrl.setRefreshing(false);
            resetLoadingElements();
        }
        mProgressBar.setVisibility(View.GONE);
        isLoading = false;

        mAdapter.refill(movies);

        if (mAdapter.getItemCount() <= 0) {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.setText(getString(R.string.empty_favourites));
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
        bundle.putString(Constants.MOVIE_POSTER_PATH,
                Constants.MOVIE_POSTER_BASE_URL + movie.getPosterPath());
        bundle.putString(Constants.IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(imageView));
        intent.putExtra(Constants.MOVIE, bundle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImageTransformTransition imageTransformTransition = new ImageTransformTransition();
            getActivity().getWindow().setSharedElementExitTransition(imageTransformTransition);
            getActivity().getWindow().setSharedElementReenterTransition(imageTransformTransition);
            getActivity().getWindow().setSharedElementReturnTransition(imageTransformTransition);
            getActivity().getWindow().setExitTransition(new Fade());
        }

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                imageView,
                ViewCompat.getTransitionName(imageView));
        startActivity(intent, options.toBundle());
    }
}
