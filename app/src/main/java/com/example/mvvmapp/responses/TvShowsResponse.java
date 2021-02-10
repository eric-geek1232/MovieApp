package com.example.mvvmapp.responses;

import com.example.mvvmapp.models.TvShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowsResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int totalPages;

    @SerializedName("tv_shows")
    private List<TvShow> tvShows;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<TvShow> getTvShows() {
        return tvShows;
    }
}
