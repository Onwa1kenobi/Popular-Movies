package com.julius.popularmovies.movies;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.julius.popularmovies.R;
import com.julius.popularmovies.models.Trailer;

import java.util.List;

/**
 * Created by ameh on 22/05/2017.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    private List<Trailer> mMovieTrailers;

    MovieTrailerAdapter(List<Trailer> movieTrailers) {
        mMovieTrailers = movieTrailers;
    }

    @Override
    public MovieTrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_trailer_item, parent, false);

        return new MovieTrailerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapter.ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return mMovieTrailers.size();
    }

    @NonNull
    List<Trailer> getItems() {
        return mMovieTrailers;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        String key;
        TextView title;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text);
            cardView = (CardView) itemView.findViewById(R.id.background);
            cardView.setOnClickListener(this);
        }

        void bindView(int position) {
            Trailer trailer = mMovieTrailers.get(position);
            title.setText(trailer.getName());
            key = trailer.getKey();
        }

        @Override
        public void onClick(View view) {
            itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + key)));
        }
    }
}