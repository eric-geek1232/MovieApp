package com.example.mvvmapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mvvmapp.R;
import com.example.mvvmapp.adapters.ImageSliderAdapter;
import com.example.mvvmapp.databinding.ActivityTvShowDetailsBinding;
import com.example.mvvmapp.models.TvShow;
import com.example.mvvmapp.responses.TvShowDetailsResponse;
import com.example.mvvmapp.utils.TempDataHolder;
import com.example.mvvmapp.viewModels.TvShowDetailsViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.mvvmapp.utils.components.BottomSheetEpisodes.showBottomSheet;

public class TvShowDetailsActivity extends AppCompatActivity {
    private ActivityTvShowDetailsBinding activityTvShowDetailsBinding;
    private TvShowDetailsViewModel viewModel;
    private TvShow tvShow;
    private boolean isTvShowWatched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvShowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tv_show_details);
        initializeActivity();
    }

    private void initializeActivity() {
        viewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);
        tvShow = (TvShow) getIntent().getSerializableExtra("tvShow");
        checkTvShowInWatchList(String.valueOf(tvShow.getId()));
        getTvShowDetails();

        activityTvShowDetailsBinding.imageBack.setOnClickListener(view -> onBackPressed());
        activityTvShowDetailsBinding.swipeLayoutTvShowDetails.setOnRefreshListener(() -> {
            activityTvShowDetailsBinding.layoutSliderIndicators.removeAllViews();
            setLayout(false);
            activityTvShowDetailsBinding.swipeLayoutTvShowDetails.setRefreshing(false);
            activityTvShowDetailsBinding.containerNoInternetConnection.setVisibility(View.GONE);
            getTvShowDetails();
        });
    }

    private void checkTvShowInWatchList(String tvShowId) {
        CompositeDisposable compositeDisposableForTvShow = new CompositeDisposable();
        compositeDisposableForTvShow.add(
                viewModel.getTvShowFromWatchList(tvShowId)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tvShow1 -> {
                            isTvShowWatched = true;
                            activityTvShowDetailsBinding.fabWatchList.setImageResource(R.drawable.ic_added);
                            compositeDisposableForTvShow.dispose();
                        })
        );
    }

    private void getTvShowDetails() {
        activityTvShowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        viewModel.getTvShowDetails(tvShowId).observe(this, tvShowDetailsResponse -> {
            activityTvShowDetailsBinding.setIsLoading(false);
            if (tvShowDetailsResponse != null && tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                String[] pictures = tvShowDetailsResponse.getTvShowDetails().getPictures();

                setLayout(true);
                loadImageSlider(pictures);
                loadBasicTvShowDetails();
                loadMoreTvShowDetails(tvShowDetailsResponse);
            } else {
                setLayout(false);
            }
        });
    }

    private void setLayout(boolean isDone) {
        if (isDone) {
            activityTvShowDetailsBinding.viewPager2Slider.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.textName.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.imageTvShow.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.fabWatchList.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.buttonWebSite.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);
            activityTvShowDetailsBinding.containerNoInternetConnection.setVisibility(View.GONE);
        } else {
            activityTvShowDetailsBinding.viewPager2Slider.setVisibility(View.GONE);
            activityTvShowDetailsBinding.viewFadingEdge.setVisibility(View.GONE);
            activityTvShowDetailsBinding.layoutSliderIndicators.setVisibility(View.GONE);
            activityTvShowDetailsBinding.textName.setVisibility(View.GONE);
            activityTvShowDetailsBinding.textNetworkCountry.setVisibility(View.GONE);
            activityTvShowDetailsBinding.textStarted.setVisibility(View.GONE);
            activityTvShowDetailsBinding.textStatus.setVisibility(View.GONE);
            activityTvShowDetailsBinding.imageTvShow.setVisibility(View.GONE);
            activityTvShowDetailsBinding.textDescription.setVisibility(View.GONE);
            activityTvShowDetailsBinding.textReadMore.setVisibility(View.GONE);
            activityTvShowDetailsBinding.fabWatchList.setVisibility(View.GONE);
            activityTvShowDetailsBinding.viewDivider1.setVisibility(View.GONE);
            activityTvShowDetailsBinding.viewDivider2.setVisibility(View.GONE);
            activityTvShowDetailsBinding.layoutMisc.setVisibility(View.GONE);
            activityTvShowDetailsBinding.buttonWebSite.setVisibility(View.GONE);
            activityTvShowDetailsBinding.buttonEpisodes.setVisibility(View.GONE);
            activityTvShowDetailsBinding.containerNoInternetConnection.setVisibility(View.VISIBLE);
        }
    }

    private void loadImageSlider(String[] sliderImage) {
        activityTvShowDetailsBinding.viewPager2Slider.setOffscreenPageLimit(1);
        activityTvShowDetailsBinding.viewPager2Slider.setAdapter(new ImageSliderAdapter(sliderImage));
        setupSliderIndicators(sliderImage.length);

        activityTvShowDetailsBinding.viewPager2Slider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));

            indicators[i].setLayoutParams(layoutParams);
            activityTvShowDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }

        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCount = activityTvShowDetailsBinding.layoutSliderIndicators.getChildCount();

        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvShowDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (position == i) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }

    private void loadBasicTvShowDetails() {
        activityTvShowDetailsBinding.setTvShowName(tvShow.getName());
        activityTvShowDetailsBinding.setStartedDate(tvShow.getStartDate());
        activityTvShowDetailsBinding.setNetworkCountry(
                tvShow.getCountry() + "(" + tvShow.getStartDate() + ")"
        );
        activityTvShowDetailsBinding.setStatus(tvShow.getStatus());
    }

    private void loadMoreTvShowDetails(TvShowDetailsResponse tvShowDetailsResponse) {
        activityTvShowDetailsBinding.setTvShowImageURL(
                tvShowDetailsResponse.getTvShowDetails().getImage_path()
        );
        activityTvShowDetailsBinding.setDescription(
                String.valueOf(
                        HtmlCompat.fromHtml(
                                tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                )
        );
        setListeners(tvShowDetailsResponse);
        if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
            activityTvShowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
        } else {
            activityTvShowDetailsBinding.setGenre("N/A");
        }
        activityTvShowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
        activityTvShowDetailsBinding.buttonWebSite.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getURL()));
            startActivity(intent);
        });

    }

    private void setListeners(TvShowDetailsResponse tvShowDetailsResponse) {
        activityTvShowDetailsBinding.textReadMore.setOnClickListener(view -> {
            if (activityTvShowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                activityTvShowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                activityTvShowDetailsBinding.textDescription.setEllipsize(null);
                activityTvShowDetailsBinding.textReadMore.setText(R.string.read_less);
            } else {
                activityTvShowDetailsBinding.textDescription.setMaxLines(4);
                activityTvShowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                activityTvShowDetailsBinding.textReadMore.setText(R.string.read_more);
            }
        });

        activityTvShowDetailsBinding.buttonEpisodes.setOnClickListener(view -> showBottomSheet(
                tvShowDetailsResponse,
                tvShow.getName(),
                this,
                this
        ));

        activityTvShowDetailsBinding.fabWatchList.setOnClickListener(view -> {
            CompositeDisposable compositeDisposable = new CompositeDisposable();

            if (isTvShowWatched) {
                compositeDisposable.add(viewModel.removeTvShowFromWatchList(tvShow)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            isTvShowWatched = false;
                            TempDataHolder.IS_WATCH_LIST_UPDATE = true;
                            activityTvShowDetailsBinding.fabWatchList.setImageResource(R.drawable.ic_watch);
                            Toast.makeText(getApplicationContext(), "Removed from watch list", Toast.LENGTH_LONG).show();
                            compositeDisposable.dispose();
                        }));
            } else {
                compositeDisposable.add(viewModel.addToWatchList(tvShow)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            TempDataHolder.IS_WATCH_LIST_UPDATE = true;
                            activityTvShowDetailsBinding.fabWatchList.setImageResource(R.drawable.ic_added);
                            Toast.makeText(getApplicationContext(), "Added to watch list", Toast.LENGTH_LONG).show();
                            compositeDisposable.dispose();
                        })
                );
            }
        });
    }
}