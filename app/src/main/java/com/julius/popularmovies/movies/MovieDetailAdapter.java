package com.julius.popularmovies.movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.julius.popularmovies.R;
import com.julius.popularmovies.models.Review;
import com.julius.popularmovies.models.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ameh on 22/05/2017.
 */

class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> mMovieObjects;
    private List<Trailer> mMovieTrailers = new ArrayList<>();
    private int viewHolderPosition;

    MovieDetailAdapter(List<Object> movieObjects) {
        mMovieObjects = movieObjects;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    List<Object> getItems() {
        return mMovieObjects;
    }

    int getCurrentViewPosition() {
        return viewHolderPosition;
    }

    void addPlot(List<Object> plot) {
        mMovieObjects = plot;
    }

    void addTrailers(List trailers) {
        int currentSize = mMovieObjects.size();
        int amountInserted = trailers.size();
        mMovieObjects.add(trailers);
        mMovieTrailers = trailers;
        notifyItemRangeInserted(currentSize, amountInserted);
    }

    void refill(List items) {
        int currentSize = mMovieObjects.size();
        int amountInserted = items.size();
        mMovieObjects.addAll(items);
        notifyItemRangeInserted(currentSize, amountInserted);
    }

    @Override
    public int getItemCount() {
        return mMovieObjects.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view
        View view;

        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_movie_plot, parent, false);
                return new MoviePlotViewHolder(view);

            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_movie_trailers, parent, false);
                return new MovieTrailersViewHolder(view);

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_movie_reviews, parent, false);
                return new MovieReviewsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        viewHolderPosition = holder.getAdapterPosition();
        switch (holder.getItemViewType()) {
            case 0:
                MoviePlotViewHolder moviePlotViewHolder = (MoviePlotViewHolder) holder;
                moviePlotViewHolder.bindView(position);
                break;

            case 1:
                final MovieTrailersViewHolder movieTrailersViewHolder = (MovieTrailersViewHolder) holder;
                movieTrailersViewHolder.bindView(position);
                break;

            default:
                MovieReviewsViewHolder movieReviewsViewHolder = (MovieReviewsViewHolder) holder;
                movieReviewsViewHolder.bindView(position);
                break;
        }
    }

    private class MoviePlotViewHolder extends RecyclerView.ViewHolder {

        TextView moviePlot;

        MoviePlotViewHolder(View itemView) {
            super(itemView);

            moviePlot = (TextView) itemView.findViewById(R.id.plot_text);
        }

        void bindView(int position) {
            String plot = (String) mMovieObjects.get(position);
            moviePlot.setText(plot);
        }
    }

    private class MovieReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView reviewerName, reviewText;

        MovieReviewsViewHolder(View itemView) {
            super(itemView);

            reviewerName = (TextView) itemView.findViewById(R.id.username);
            reviewText = (TextView) itemView.findViewById(R.id.review_text);
        }

        void bindView(int position) {
            Review review = (Review) mMovieObjects.get(position);
            reviewerName.setText(review.getAuthor());
            reviewText.setText(review.getContent());
        }
    }

    private class MovieTrailersViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        MovieTrailerAdapter adapter;

        MovieTrailersViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.trailer_recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            adapter = new MovieTrailerAdapter(mMovieTrailers);
            recyclerView.setAdapter(adapter);
        }

        void bindView(int position) {

        }
    }
}