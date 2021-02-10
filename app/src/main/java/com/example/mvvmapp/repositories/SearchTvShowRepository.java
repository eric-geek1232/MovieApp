package com.example.mvvmapp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mvvmapp.network.ApiClient;
import com.example.mvvmapp.network.ApiService;
import com.example.mvvmapp.responses.TvShowsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTvShowRepository {
     private final ApiService apiService;

     public SearchTvShowRepository() {
         apiService = ApiClient.getRetrofit().create(ApiService.class);
     }

     public LiveData<TvShowsResponse> searchTvShow(String query, int page){
         MutableLiveData<TvShowsResponse> data = new MutableLiveData<>();

         apiService.searchTvShow(query, page).enqueue(new Callback<TvShowsResponse>() {
             @Override
             public void onResponse(@NonNull Call<TvShowsResponse> call, @NonNull Response<TvShowsResponse> response) {
                 data.setValue(response.body());
             }

             @Override
             public void onFailure(@NonNull Call<TvShowsResponse> call, @NonNull Throwable t) {
                 data.setValue(null);
             }
         });

         return data;
     }
}
