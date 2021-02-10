package com.example.mvvmapp.utils.components;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;

import com.example.mvvmapp.R;
import com.example.mvvmapp.activities.TvShowDetailsActivity;
import com.example.mvvmapp.adapters.EpisodesAdapter;
import com.example.mvvmapp.responses.TvShowDetailsResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomSheetEpisodes {
    public static void showBottomSheet(TvShowDetailsResponse tvShowDetailsResponse, String name, Context context, Activity activity) {
        BottomSheetDialog episodesBottomSheetDialog = new BottomSheetDialog(context);
        com.example.mvvmapp.databinding.BottomSheetEpisodesBinding bottomSheetEpisodesBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.bottom_sheet_episodes,
                activity.findViewById(R.id.episodes_container),
                false
        );

        episodesBottomSheetDialog.setContentView(bottomSheetEpisodesBinding.getRoot());
        bottomSheetEpisodesBinding.recyclerViewEpisodes.setAdapter(
                new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
        );

        bottomSheetEpisodesBinding.textTitle.setText(
                String.format("Episodes| %s", name)
        );

        bottomSheetEpisodesBinding.imageClose.setOnClickListener(view1 -> episodesBottomSheetDialog.dismiss());

        FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(
                com.google.android.material.R.id.design_bottom_sheet
        );

        if (frameLayout != null) {
            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        episodesBottomSheetDialog.show();
    }
}
