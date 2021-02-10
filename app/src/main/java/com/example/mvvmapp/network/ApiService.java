package com.example.mvvmapp.network;

import com.example.mvvmapp.responses.TvShowDetailsResponse;
import com.example.mvvmapp.responses.TvShowsResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Call;

public interface ApiService {
    @GET("most-popular")
    Call<TvShowsResponse> getMostPopularTvShows(@Query("page") int page);

    @GET("show-details")
    Call<TvShowDetailsResponse> getTvShowDetails(@Query("q") String tvShowId);

    @GET("search")
    Call<TvShowsResponse> searchTvShow(@Query("q") String query, @Query("page") int page);
}
