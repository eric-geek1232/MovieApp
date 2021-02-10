package com.example.mvvmapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmapp.R;
import com.example.mvvmapp.databinding.ItemContainerEpisodeBinding;
import com.example.mvvmapp.models.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder> {
    private final List<Episode> episodes;
    private LayoutInflater layoutInflater;

    public EpisodesAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public EpisodesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ItemContainerEpisodeBinding itemContainerEpisodeBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.item_container_episode,
                parent,
                false
        );

        return new EpisodesViewHolder(itemContainerEpisodeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesViewHolder holder, int position) {
        holder.onBindEpisode(episodes.get(position));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public static class EpisodesViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerEpisodeBinding itemContainerEpisodeBinding;

        public EpisodesViewHolder(ItemContainerEpisodeBinding itemContainerEpisodeBinding) {
            super(itemContainerEpisodeBinding.getRoot());

            this.itemContainerEpisodeBinding = itemContainerEpisodeBinding;
        }

        public void onBindEpisode(Episode episode) {
            String title = "S";
            String season = episode.getSeason();
            String episodeNumber = episode.getEpisode();

            if (season.length() == 1) {
                season = "0".concat(season);
            }

            if (episodeNumber.length() == 1) {
                episodeNumber = "0".concat(episodeNumber);
            }

            episodeNumber = "E".concat(episodeNumber);
            title = title.concat(season).concat(episodeNumber);
            itemContainerEpisodeBinding.setTitle(title);
            itemContainerEpisodeBinding.setName(episode.getName());
            itemContainerEpisodeBinding.setAirDate(episode.getAirDate());

        }
    }
}
