package com.example.mvvmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmapp.adapters.TvShowsAdapter;
import com.example.mvvmapp.R;
import com.example.mvvmapp.viewModels.MostPopularTvShowsViewModel;
import com.example.mvvmapp.databinding.ActivityMainBinding;
import com.example.mvvmapp.listeners.TvShowsListener;
import com.example.mvvmapp.models.TvShow;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TvShowsListener {

    private MostPopularTvShowsViewModel viewModel;
    private ArrayList<TvShow> tvShowArrayList;
    private TvShowsAdapter tvShowsAdapter;
    private ActivityMainBinding activityMainBinding;
    private int currentPage;
    private int totalAvailablePages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initializeActivity();
    }

    private void initializeActivity() {
        currentPage = 1;
        totalAvailablePages = 1;
        tvShowArrayList = new ArrayList<>();
        activityMainBinding.setIsLoading(false);
        activityMainBinding.recyclerViewTvShows.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTvShowsViewModel.class);
        tvShowsAdapter = new TvShowsAdapter(tvShowArrayList, this);
        activityMainBinding.recyclerViewTvShows.setAdapter(tvShowsAdapter);

        setListeners();
        getMostPopularShows();
    }

    private void setListeners() {
        activityMainBinding.recyclerViewTvShows.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.recyclerViewTvShows.canScrollVertically(1)) {
                    if (currentPage <= totalAvailablePages && !activityMainBinding.swipeLayoutTvShows.isRefreshing()) {
                        currentPage += 1;
                        getMostPopularShows();
                    }
                }
            }
        });

        activityMainBinding.imageWatchList.setOnClickListener(view -> startActivity(
                new Intent(MainActivity.this, WatchListActivity.class)
        ));

        activityMainBinding.imageSearch.setOnClickListener(view -> startActivity(
                new Intent(MainActivity.this, SearchTvShowActivity.class)
        ));

        activityMainBinding.swipeLayoutTvShows.setOnRefreshListener(() -> {
            currentPage = 1;
            tvShowArrayList.clear();
            tvShowsAdapter.notifyDataSetChanged();
            getMostPopularShows();
        });
    }

    private void getMostPopularShows() {
        toggleLoading();
        viewModel.getMostPopularTvShows(currentPage).observe(this, mostPopularTvShowsResponse -> {
            toggleLoading();
            if (mostPopularTvShowsResponse != null && mostPopularTvShowsResponse.getTvShows() != null) {
                int oldCount = tvShowArrayList.size();
                totalAvailablePages = mostPopularTvShowsResponse.getTotalPages();
                tvShowArrayList.addAll(mostPopularTvShowsResponse.getTvShows());
                tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShowArrayList.size());
                activityMainBinding.recyclerViewTvShows.setVisibility(View.VISIBLE);
                activityMainBinding.containerNoInternetConnection.setVisibility(View.GONE);
            } else {
                activityMainBinding.containerNoInternetConnection.setVisibility(View.VISIBLE);
                activityMainBinding.recyclerViewTvShows.setVisibility(View.GONE);
            }
            activityMainBinding.swipeLayoutTvShows.setRefreshing(false);
        });
    }

    private void toggleLoading() {
        if (currentPage != 1) {
            activityMainBinding.setIsLoading(!activityMainBinding.getIsLoading());
        }
    }

    @Override
    public void onTvShowClicked(TvShow tvShow) {
        Intent intent = new Intent(this, TvShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}