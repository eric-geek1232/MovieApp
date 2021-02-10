package com.example.mvvmapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mvvmapp.adapters.WatchListAdapter;
import com.example.mvvmapp.R;
import com.example.mvvmapp.utils.TempDataHolder;
import com.example.mvvmapp.viewModels.WatchListViewModel;
import com.example.mvvmapp.databinding.ActivityWatchListBinding;
import com.example.mvvmapp.listeners.WatchListListener;
import com.example.mvvmapp.models.TvShow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity implements WatchListListener {
    private ActivityWatchListBinding activityWatchListBinding;
    private WatchListViewModel viewModel;
    private WatchListAdapter watchListAdapter;
    private List<TvShow> watchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchListBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_list);
        initializeActivity();
    }

    private void initializeActivity(){
        viewModel = new ViewModelProvider(this).get(WatchListViewModel.class);
        activityWatchListBinding.imageBack.setOnClickListener(view -> onBackPressed());
        loadWatchList();
    }

    private void loadWatchList() {
        watchList = new ArrayList<>();
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                viewModel.loadWatchList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchListBinding.setIsLoading(false);
                    if (watchList.size() > 0) {
                        watchList.clear();
                    }

                    watchList.addAll(tvShows);
                    watchListAdapter = new WatchListAdapter(watchList, this);
                    activityWatchListBinding.recyclerViewWatchList.setAdapter(watchListAdapter);
                    activityWatchListBinding.recyclerViewWatchList.setVisibility(View.VISIBLE);
                    checkListStatus();
                    compositeDisposable.dispose();
                })
        );
    }

    private void checkListStatus() {
        if (watchList.size() == 0) {
            activityWatchListBinding.containerNothingToShow.setVisibility(View.VISIBLE);
        } else {
            activityWatchListBinding.containerNothingToShow.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TempDataHolder.IS_WATCH_LIST_UPDATE) {
            loadWatchList();
            checkListStatus();
            TempDataHolder.IS_WATCH_LIST_UPDATE = false;
        }
    }

    @Override
    public void onTvShowClicked(TvShow tvShow) {
        Intent intent = new Intent(this, TvShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTvShowFromWatchList(TvShow tvShow, int position) {
        CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
        compositeDisposableForDelete.add(
                viewModel.removeTvShowFromWatchList(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(()->{
                    watchList.remove(position);
                    watchListAdapter.notifyItemRemoved(position);
                    watchListAdapter.notifyItemRangeChanged(position, watchListAdapter.getItemCount());
                    checkListStatus();
                    compositeDisposableForDelete.dispose();
                })
        );
    }
}