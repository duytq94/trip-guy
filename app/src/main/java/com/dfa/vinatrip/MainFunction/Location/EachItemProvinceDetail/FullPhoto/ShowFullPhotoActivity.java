package com.dfa.vinatrip.MainFunction.Location.EachItemProvinceDetail.FullPhoto;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dfa.vinatrip.MainFunction.Location.ProvinceDetail.ProvinceDestination.ProvinceDestination;
import com.dfa.vinatrip.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowFullPhotoActivity extends AppCompatActivity {

    private ViewPager vpShowFull;
    private CustomPagerAdapter customPagerAdapter;
    private List<String> listUrlPhotos;
    private int position;
    private Toolbar toolbar;
    private android.support.v7.app.ActionBar actionBar;
    private ProvinceDestination detailDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_photo);

        // Get data from
        listUrlPhotos = getIntent().getStringArrayListExtra("ListUrlPhotos");
        position = getIntent().getIntExtra("Position", 0);
        detailDestination =
                (ProvinceDestination) getIntent().getSerializableExtra("DetailDestination");

        changeColorStatusBar();
        setupActionBar();

        vpShowFull = (ViewPager) findViewById(R.id.activity_full_photo_vp_show_full);
        customPagerAdapter = new CustomPagerAdapter(ShowFullPhotoActivity.this);
        vpShowFull.setAdapter(customPagerAdapter);

        vpShowFull.setCurrentItem(position);
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));
        }
    }

    public class CustomPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public CustomPagerAdapter(Context context) {
            this.layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return listUrlPhotos.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = layoutInflater.inflate(R.layout.item_photo_slide_show, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.ivPhotoSlideShow);
            Picasso.with(ShowFullPhotoActivity.this)
                    .load(listUrlPhotos.get(position))
                    .placeholder(R.drawable.ic_loading)
                    .into(imageView);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    public void setupActionBar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(detailDestination.getName());
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

            // Set button back
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return false;
    }
}
