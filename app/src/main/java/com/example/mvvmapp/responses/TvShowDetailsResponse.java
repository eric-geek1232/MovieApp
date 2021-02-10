package com.example.mvvmapp.responses;

import com.example.mvvmapp.models.TvShowDetails;
import com.google.gson.annotations.SerializedName;

public class TvShowDetailsResponse {
    @SerializedName("tvShow")
    private TvShowDetails tvShowDetails;

    public TvShowDetails getTvShowDetails() {
        return tvShowDetails;
    }
}
