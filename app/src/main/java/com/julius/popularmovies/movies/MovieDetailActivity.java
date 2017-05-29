package com.julius.popularmovies.movies;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.julius.popularmovies.R;
import com.julius.popularmovies.models.Movie;
import com.julius.popularmovies.models.Review;
import com.julius.popularmovies.models.Trailer;
import com.julius.popularmovies.utils.Constants;
import com.julius.popularmovies.utils.ImageTransformTransition;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity implements MoviesContract.MovieDetailsView {

    private static final String STATE_ADAPTER_POSITION = "saved_state_adapter_position";
    private static final String STATE_TRAILERS = "saved_state_trailers";
    private static final String STATE_REVIEWS = "saved_state_reviews";

    private MovieDetailsPresenter mPresenter;
    private MovieDetailAdapter mAdapter;

    private FloatingActionButton mFab;

    private ArrayList<Object> mPlotObject;
    private List<Trailer> mTrailers = new ArrayList<>();
    private List<Review> mReviews = new ArrayList<>();
    private String moviePlot, movieTitle;

    private boolean movieIsFavourite = false, hideMenu = true;

    public static String getNumberOrdinal(int i) {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        supportPostponeEnterTransition();

        mPresenter = new MovieDetailsPresenter(this, MoviesInjector.provideMovieService(Constants.MOVIE_DB_API_URL),
                getContentResolver());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

        Bundle extras = getIntent().getExtras().getBundle(Constants.MOVIE);

        assert extras != null;
        final Movie movie = extras.getParcelable(Constants.MOVIE);

        if (movie == null) return;

        TextView movieTitleText = (TextView) findViewById(R.id.title_text);
        TextView movieReleaseDateText = (TextView) findViewById(R.id.release_date_text);
        TextView movieRatingText = (TextView) findViewById(R.id.average_rating_text);
        mFab = (FloatingActionButton) findViewById(R.id.favourite_button);
        mFab.hide();

        String rawDateString = movie.getReleaseDate();
        String month = new DateFormatSymbols(Locale.getDefault())
                .getMonths()[Integer.parseInt(rawDateString.substring(5, 7)) - 1];
        String year = rawDateString.substring(0, 4);

        movieTitle = movie.getTitle();
        movieTitleText.setText(movie.getTitle());
        movieReleaseDateText.setText(String.format(Locale.getDefault(), "%s %s, %s",
                getNumberOrdinal(Integer.parseInt(rawDateString.substring(8, 10))), month, year));
        movieRatingText.setText(String.valueOf(movie.getVoteAverage()));

        final ImageView movieImage = (ImageView) findViewById(R.id.movie_image);

        final RelativeLayout headerContainer = (RelativeLayout) findViewById(R.id.header_container);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(new ImageTransformTransition());
            getWindow().setSharedElementReturnTransition(new ImageTransformTransition());
            getWindow().setSharedElementExitTransition(new ImageTransformTransition());
            getWindow().setExitTransition(new Explode());
            getWindow().setEnterTransition(new Fade());
            String imageTransitionName = extras.getString(Constants.IMAGE_TRANSITION_NAME);
            movieImage.setTransitionName(imageTransitionName);

            getWindow().getSharedElementEnterTransition()
                    .addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(@NonNull Transition transition) {

                }

                @Override
                public void onTransitionEnd(@NonNull Transition transition) {
                    Animator anim = ObjectAnimator.ofFloat(headerContainer, "alpha", 0f, 1f);
                    anim.setInterpolator(new DecelerateInterpolator());
                    anim.setDuration(500);
                    headerContainer.setVisibility(View.VISIBLE);
                    anim.start();
                    mFab.show();
                }

                @Override
                public void onTransitionCancel(@NonNull Transition transition) {

                }

                @Override
                public void onTransitionPause(@NonNull Transition transition) {

                }

                @Override
                public void onTransitionResume(@NonNull Transition transition) {

                }
            });
        } else {
            Animator anim = ObjectAnimator.ofFloat(headerContainer, "alpha", 0f, 1f);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(500);
            headerContainer.setVisibility(View.VISIBLE);
            anim.start();
            mFab.show();
        }

        if (savedInstanceState != null) {
            headerContainer.setVisibility(View.VISIBLE);
            mFab.show();
        }

        movieIsFavourite = mPresenter.isMovieInFavourites((int) movie.getId());
        if (movieIsFavourite) {
            mFab.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            mFab.setImageResource(R.drawable.ic_favorite_border);
        }


        Glide.with(this)
                .load(extras.getString(Constants.MOVIE_POSTER_PATH))
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(400, 600)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        movieImage.setImageDrawable(glideDrawable);
                    }
                });

        int mAdapterPosition = savedInstanceState != null
                ? savedInstanceState.getInt(STATE_ADAPTER_POSITION, -1)
                : -1;

        List restoredTrailers = savedInstanceState != null
                ? savedInstanceState.getParcelableArrayList(STATE_TRAILERS)
                : new ArrayList<Trailer>(0);

        List restoredReviews = savedInstanceState != null
                ? savedInstanceState.getParcelableArrayList(STATE_REVIEWS)
                : new ArrayList<Review>(0);

        moviePlot = movie.getOverview();
        mPlotObject = new ArrayList<>();
        mPlotObject.add(moviePlot);

        mAdapter = new MovieDetailAdapter(mPlotObject);
        if (savedInstanceState != null) {
            mAdapter.addTrailers(restoredTrailers);
            mAdapter.refill(restoredReviews);

            if (mTrailers.size() > 0) {
                hideMenu = false;
                invalidateOptionsMenu();
            }
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        if (mAdapterPosition != -1) recyclerView.scrollToPosition(mAdapterPosition);

        mPresenter.getMovieDetails(movie.getId());

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieIsFavourite) {
                    mPresenter.deleteMovieFromFavourites((int) movie.getId());
                    mFab.setImageResource(R.drawable.ic_favorite_border);
                    movieIsFavourite = false;
                } else {
                    mPresenter.saveMovieToFavourites(movie);
                    mFab.setImageResource(R.drawable.ic_favorite_filled);
                    movieIsFavourite = true;
                }
            }
        });

        supportStartPostponedEnterTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_details_menu, menu);

        if (hideMenu) {
            menu.findItem(R.id.action_share).setVisible(false);
        } else {
            menu.findItem(R.id.action_share).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.action_share:
                shareMovieTrailer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_TRAILERS, new ArrayList<>(mTrailers));
        outState.putParcelableArrayList(STATE_REVIEWS, new ArrayList<>(mReviews));
        outState.putInt(STATE_ADAPTER_POSITION, mAdapter.getCurrentViewPosition());
    }

    @Override
    public void displayMovieDetails(List<Trailer> trailers, List<Review> reviews) {
        mTrailers.clear();
        mTrailers.addAll(trailers);

        mReviews.clear();
        mReviews.addAll(reviews);

        mAdapter.getItems().clear();
        mAdapter.notifyDataSetChanged();
        mPlotObject.add(moviePlot);

        mAdapter.addPlot(mPlotObject);
        mAdapter.addTrailers(mTrailers);
        mAdapter.refill(mReviews);

        if (mTrailers.size() > 0) {
            hideMenu = false;
            invalidateOptionsMenu();
        }
    }

    private void shareMovieTrailer() {
        String shareText = "Wow, have you seen this trailer for " + movieTitle + "?\nCheck it out here\n"
                + Uri.parse("http://www.youtube.com/watch?v=" + mTrailers.get(0).getKey());

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share Via..."));
    }

    @Override
    public void displayErrorMessage(String message) {
        Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
