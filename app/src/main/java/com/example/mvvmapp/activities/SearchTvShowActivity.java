package com.example.mvvmapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmapp.adapters.TvShowsAdapter;
import com.example.mvvmapp.R;
import com.example.mvvmapp.viewModels.SearchTvShowViewModel;
import com.example.mvvmapp.databinding.ActivitySearchTvShowBinding;
import com.example.mvvmapp.listeners.TvShowsListener;
import com.example.mvvmapp.models.TvShow;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchTvShowActivity extends AppCompatActivity implements TvShowsListener {

    private ActivitySearchTvShowBinding activitySearchTvShowBinding;
    private SearchTvShowViewModel viewModel;
    private TvShowsAdapter tvShowsAdapter;
    private List<TvShow> tvShowList;
    private int currentPage;
    private int totalAvailablePages;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchTvShowBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_tv_show);
        initializeActivity();
    }

    private void initializeActivity() {
        tvShowList = new ArrayList<>();
        currentPage = 1;
        totalAvailablePages = 1;
        activitySearchTvShowBinding.imageBack.setOnClickListener(view -> onBackPressed());
        activitySearchTvShowBinding.recyclerViewTvShowsFound.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchTvShowViewModel.class);
        tvShowsAdapter = new TvShowsAdapter(tvShowList, this);
        activitySearchTvShowBinding.recyclerViewTvShowsFound.setAdapter(tvShowsAdapter);
        setListeners();
    }

    private void setListeners() {
        activitySearchTvShowBinding.inputSearchTvShow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (timer != null) {
                    timer.cancel();
                    clearTvShowList();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> searchTvShow(editable.toString()));
                        }
                    }, 800);
                } else {
                    activitySearchTvShowBinding.containerLayoutState.setVisibility(View.GONE);
                    clearTvShowList();
                }
            }
        });

        activitySearchTvShowBinding.recyclerViewTvShowsFound.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchTvShowBinding.recyclerViewTvShowsFound.canScrollVertically(1)) {
                    if (!activitySearchTvShowBinding.inputSearchTvShow.getText().toString().isEmpty()) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1;
                            searchTvShow(activitySearchTvShowBinding.inputSearchTvShow.getText().toString());
                        }
                    }
                }
            }
        });
    }

    private void searchTvShow(String query) {
        toggleLoading();
        activitySearchTvShowBinding.containerLayoutState.setVisibility(View.GONE);

        viewModel.searchTvShow(query, currentPage).observe(this, tvShowsResponse -> {
            toggleLoading();
            if (tvShowsResponse != null) {
                totalAvailablePages = tvShowsResponse.getTotalPages();
                if (tvShowsResponse.getTvShows() != null) {
                    int oldCount = tvShowList.size();
                    tvShowList.addAll(tvShowsResponse.getTvShows());
                    tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShowList.size());

                    if (tvShowList.size() == 0) {
                        setResultAnimation(R.raw.anim_tv_no_results, getString(R.string.no_results));
                    } else {
                        activitySearchTvShowBinding.containerLayoutState.setVisibility(View.GONE);
                    }
                }
            } else {
                setResultAnimation(R.raw.anim_no_signal, getString(R.string.No_Internet_Connection));
            }
        });
    }

    private void setResultAnimation(int animation, String title) {
        activitySearchTvShowBinding.containerLayoutState.setVisibility(View.VISIBLE);
        activitySearchTvShowBinding.animSearchTvShow.setAnimation(animation);
        activitySearchTvShowBinding.animSearchTvShow.playAnimation();
        activitySearchTvShowBinding.animSearchTvShow.setRepeatCount(Animation.INFINITE);
        activitySearchTvShowBinding.textTitleAnim.setText(title);
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            activitySearchTvShowBinding.setIsLoading(
                    activitySearchTvShowBinding.getIsLoading() == null
                    || !activitySearchTvShowBinding.getIsLoading()
            );
        } else {
            activitySearchTvShowBinding.setIsLoadingMore(
                    activitySearchTvShowBinding.getIsLoadingMore() == null
                            || !activitySearchTvShowBinding.getIsLoadingMore()
            );
        }
    }

    private void clearTvShowList() {
        totalAvailablePages = 1;
        currentPage = 1;
        tvShowList.clear();
        tvShowsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTvShowClicked(TvShow tvShow) {
        Intent intent = new Intent(this, TvShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}