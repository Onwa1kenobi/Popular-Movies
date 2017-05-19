package com.julius.popularmovies.movies;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ameh on 14/05/2017.
 */
class MoviesInjector {

    private static Retrofit provideRetrofit(String baseURL) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.retryOnConnectionFailure(true);

        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    static MovieService provideMovieService(String baseURL) {
        return provideRetrofit(baseURL).create(MovieService.class);
    }
}
