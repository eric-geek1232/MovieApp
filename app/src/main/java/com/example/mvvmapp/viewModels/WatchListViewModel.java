package com.example.mvvmapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.mvvmapp.database.TvShowsDataBase;
import com.example.mvvmapp.models.TvShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchListViewModel extends AndroidViewModel {

    private final TvShowsDataBase tvShowsDataBase;

    public WatchListViewModel(@NonNull Application application) {
        super(application);
        tvShowsDataBase = TvShowsDataBase.getTvShowDataBase(application);
    }

    public Flowable<List<TvShow>> loadWatchList() {
        return tvShowsDataBase.tvShowDao().getWatchList();
    }

    public Completable removeTvShowFromWatchList(TvShow tvShow){
        return tvShowsDataBase.tvShowDao().removeFromWatchList(tvShow);
    }
}
