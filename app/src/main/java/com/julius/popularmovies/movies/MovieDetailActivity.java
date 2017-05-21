package com.julius.popularmovies.movies;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.julius.popularmovies.R;
import com.julius.popularmovies.models.Movie;
import com.julius.popularmovies.utils.Constants;
import com.julius.popularmovies.utils.ImageTransformTransition;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        supportPostponeEnterTransition();

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
        TextView moviePlot = (TextView) findViewById(R.id.plot_text);

        String rawDateString = movie.getReleaseDate();
        String month = new DateFormatSymbols(Locale.getDefault()).getMonths()[Integer.parseInt(rawDateString.substring(5, 7)) - 1];
        String year = rawDateString.substring(0, 4);

        movieTitleText.setText(movie.getTitle());
        movieReleaseDateText.setText(String.format(Locale.getDefault(), "%s, %s", month, year));
        movieRatingText.setText(String.valueOf(movie.getVoteAverage()));
        moviePlot.setText(movie.getOverview());

        final ImageView movieImage = (ImageView) findViewById(R.id.movie_image);

        final RelativeLayout headerContainer = (RelativeLayout) findViewById(R.id.header_container);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(new ImageTransformTransition());
            getWindow().setSharedElementReturnTransition(new ImageTransformTransition());
            getWindow().setExitTransition(new Explode());
            getWindow().setEnterTransition(new Fade());
            String imageTransitionName = extras.getString(Constants.IMAGE_TRANSITION_NAME);
            movieImage.setTransitionName(imageTransitionName);

            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
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
        }

        if (savedInstanceState != null) {
            headerContainer.setVisibility(View.VISIBLE);
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


        supportStartPostponedEnterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
