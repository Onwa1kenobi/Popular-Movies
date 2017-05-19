package com.julius.popularmovies.movies;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.julius.popularmovies.R;
import com.julius.popularmovies.models.Movie;
import com.julius.popularmovies.utils.Constants;

import java.util.List;

/**
 * Created by ameh on 13/05/2017.
 */
class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<Movie> mMovies;
    private MoviesPresenter mPresenter;
    private int viewHolderPosition;

    MoviesAdapter(List<Movie> movies, MoviesPresenter presenter) {
        mMovies = movies;
        mPresenter = presenter;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(position);
        viewHolderPosition = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @NonNull
    List<Movie> getItems() {
        return mMovies;
    }

    int getCurrentViewPosition() {
        return viewHolderPosition;
    }

    void refill(List<Movie> movies) {
        int currentSize = mMovies.size();
        int amountInserted = movies.size();
        mMovies.addAll(movies);
        notifyItemRangeInserted(currentSize, amountInserted);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView movieImage;
        Movie movie;

        ViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        void bindView(int position) {
            movie = mMovies.get(position);

            Glide.with(itemView.getContext())
                    .load(Constants.MOVIE_POSTER_BASE_URL + movie.getPosterPath())
                    .fitCenter()
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

            ViewCompat.setTransitionName(movieImage, movie.getTitle());
        }

        @Override
        public void onClick(View view) {
            mPresenter.getMovie(movie, movieImage);
        }
    }
}