package com.example.mvvmapp.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmapp.repositories.SearchTvShowRepository;
import com.example.mvvmapp.responses.TvShowsResponse;

public class SearchTvShowViewModel extends ViewModel {
    private final SearchTvShowRepository searchTvShowRepository;

    public SearchTvShowViewModel() {
        searchTvShowRepository = new SearchTvShowRepository();
    }

    public LiveData<TvShowsResponse> searchTvShow(String query, int page) {
        return searchTvShowRepository.searchTvShow(query, page);
    }
}
