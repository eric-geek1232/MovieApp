package com.example.mvvmapp.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmapp.repositories.MostPopularTvShowsRepository;
import com.example.mvvmapp.responses.TvShowsResponse;

public class MostPopularTvShowsViewModel extends ViewModel {
    private final MostPopularTvShowsRepository mostPopularTvShowsRepository;

    public MostPopularTvShowsViewModel() {
        mostPopularTvShowsRepository = new MostPopularTvShowsRepository();
    }

    public LiveData<TvShowsResponse> getMostPopularTvShows(int page) {
        return mostPopularTvShowsRepository.getMostPopularTvShows(page);
    }
}
