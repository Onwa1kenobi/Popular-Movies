package com.julius.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ameh on 13/05/2017.
 */

public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private long id;
    private String poster_path, backdrop_path;
    private String title;
    private String overview;
    private String release_date;
    private Double vote_average;

    private Movie(Parcel in) {
        id = in.readLong();
        poster_path = in.readString();
        backdrop_path = in.readString();
        title = in.readString();
        overview = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
    }

    public long getId() {
        return id;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public Double getVoteAverage() {
        return vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeDouble(vote_average);
    }
}
