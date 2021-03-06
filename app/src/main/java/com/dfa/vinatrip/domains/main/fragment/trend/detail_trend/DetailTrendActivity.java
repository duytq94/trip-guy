package com.dfa.vinatrip.domains.main.fragment.trend.detail_trend;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beesightsoft.caf.exceptions.ApiThrowable;
import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.domains.chat.ShowFullPhotoActivity_;
import com.dfa.vinatrip.domains.main.fragment.trend.Trend;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.widgets.RotateLoading;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static com.dfa.vinatrip.utils.Constants.MY_TOUR;

@EActivity(R.layout.activity_detail_trend)
public class DetailTrendActivity extends BaseActivity<DetailTrendView, DetailTrendPresenter>
        implements DetailTrendView {

    @Extra
    protected Trend trend;

    @ViewById(R.id.my_toolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.activity_detail_trend_tv_title)
    protected TextView tvTitle;
    @ViewById(R.id.activity_detail_trend_tv_intro)
    protected TextView tvIntro;
    @ViewById(R.id.activity_detail_trend_tv_content)
    protected TextView tvContent;
    @ViewById(R.id.activity_detail_trend_lv_photo)
    protected TwoWayView lvPhoto;

    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;
    private QuickAdapter<String> adapter;
    private List<String> urlList;

    @App
    protected MainApplication application;

    @Inject
    protected DetailTrendPresenter presenter;

    @AfterInject
    protected void initInject() {
        DaggerDetailTrendComponent.builder()
                .applicationComponent(application.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }

    @AfterViews
    public void init() {
        setupAppBar();
        imageLoader = ImageLoader.getInstance();
        this.imageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_green)
                .showImageForEmptyUri(R.drawable.photo_not_available)
                .showImageOnFail(R.drawable.photo_not_available)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        urlList = new ArrayList<>(Arrays.asList(trend.getUrl().split(" ")));
        setupAdapter();
        if (trend.getFromWebsite().equals(MY_TOUR)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvIntro.setVisibility(View.VISIBLE);

            tvTitle.setText(trend.getTitle());
            tvIntro.setText(Html.fromHtml(trend.getIntro()));
        } else {
            tvTitle.setVisibility(View.GONE);
            tvIntro.setVisibility(View.GONE);
        }

        tvContent.setText(Html.fromHtml(trend.getContent()));

        trend.setCountView(trend.getCountView() + 1);
        presenter.updateTrendCount(trend);
    }

    public void setupAppBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Xu hướng");
            // Set button back
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setupAdapter() {
        adapter = new QuickAdapter<String>(this, R.layout.item_photo_selected) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                ImageView ivPhoto = helper.getView(R.id.item_photo_selected_iv_photo);
                RotateLoading rotateLoading = helper.getView(R.id.item_photo_selected_rotate_loading);

                ivPhoto.setOnClickListener(v -> {
                    ShowFullPhotoActivity_.intent(DetailTrendActivity.this)
                            .listUrl((ArrayList<String>) urlList).position(helper.getPosition()).start();
                });
                imageLoader.displayImage(item, ivPhoto, imageOptions, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        rotateLoading.setVisibility(View.VISIBLE);
                        rotateLoading.start();
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        rotateLoading.setVisibility(View.GONE);
                        rotateLoading.stop();
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        rotateLoading.setVisibility(View.GONE);
                        rotateLoading.stop();
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        rotateLoading.setVisibility(View.GONE);
                        rotateLoading.stop();
                    }
                });
            }
        };
        lvPhoto.setAdapter(adapter);
        adapter.addAll(urlList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void showLoading() {
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Override
    public void apiError(Throwable throwable) {
        ApiThrowable apiThrowable = (ApiThrowable) throwable;
        Toast.makeText(this, apiThrowable.firstErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateTrendCountSuccess(String message) {

    }

    @NonNull
    @Override
    public DetailTrendPresenter createPresenter() {
        return presenter;
    }
}
