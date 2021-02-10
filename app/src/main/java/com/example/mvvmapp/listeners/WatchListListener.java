package com.example.mvvmapp.listeners;

import com.example.mvvmapp.models.TvShow;

public interface WatchListListener {
    void onTvShowClicked(TvShow tvShow);

    void removeTvShowFromWatchList(TvShow tvShow, int position);
}
