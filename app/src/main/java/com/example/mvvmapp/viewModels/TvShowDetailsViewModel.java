package com.example.mvvmapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mvvmapp.repositories.TvShowDetailsRepository;
import com.example.mvvmapp.responses.TvShowDetailsResponse;
import com.example.mvvmapp.database.TvShowsDataBase;
import com.example.mvvmapp.models.TvShow;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TvShowDetailsViewModel extends AndroidViewModel {
    private final TvShowDetailsRepository tvShowDetailsRepository;
    private final TvShowsDataBase tvShowsDataBase;

    public TvShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsRepository = new TvShowDetailsRepository();
        tvShowsDataBase = TvShowsDataBase.getTvShowDataBase(application);
    }

    public LiveData<TvShowDetailsResponse> getTvShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTvShowDetails(tvShowId);
    }

    public Completable addToWatchList(TvShow tvShow) {
        return tvShowsDataBase.tvShowDao().addToWatchList(tvShow);
    }

    public Flowable<TvShow> getTvShowFromWatchList(String tvShowId) {
        return tvShowsDataBase.tvShowDao().getTvShowFromWatchList(tvShowId);
    }

    public Completable removeTvShowFromWatchList(TvShow tvShow){
        return tvShowsDataBase.tvShowDao().removeFromWatchList(tvShow);
    }
}
