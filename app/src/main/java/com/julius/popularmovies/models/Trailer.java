package com.julius.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ameh on 22/05/2017.
 */

public class Trailer implements Parcelable {

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    private String key, name;

    public Trailer() {

    }

    private Trailer(Parcel in) {
        key = in.readString();
        name = in.readString();
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
    }
}
