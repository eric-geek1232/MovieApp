package com.example.mvvmapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmapp.R;
import com.example.mvvmapp.databinding.ItemContainerTvShowBinding;
import com.example.mvvmapp.listeners.TvShowsListener;
import com.example.mvvmapp.models.TvShow;

import java.util.List;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowsViewHolder> {
    private final List<TvShow> tvShows;
    private LayoutInflater layoutInflater;
    private final TvShowsListener tvShowsListener;

    public TvShowsAdapter(List<TvShow> tvShows, TvShowsListener tvShowsListener) {
        this.tvShows = tvShows;
        this.tvShowsListener = tvShowsListener;
    }

    @NonNull
    @Override
    public TvShowsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_tv_show, parent, false
        );

        return new TvShowsViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowsViewHolder holder, int position) {
        holder.onBindTvShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public class TvShowsViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TvShowsViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());

            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void onBindTvShow(TvShow tvShow) {
            itemContainerTvShowBinding.setTvShow(tvShow);
            itemContainerTvShowBinding.executePendingBindings();
            itemContainerTvShowBinding.getRoot().setOnClickListener(view -> tvShowsListener.onTvShowClicked(tvShow));
        }
    }
}
