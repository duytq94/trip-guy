package com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.custom_view.SimpleRatingBar;
import com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail.adapter.RecyclerHotelFeedbackAdapter;
import com.dfa.vinatrip.domains.province_detail.view_all.hotel.hotel_detail.adapter.RecyclerImageAdapter;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.request.FeedbackRequest;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;
import com.dfa.vinatrip.models.response.hotel.HotelResponse;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.utils.KeyboardVisibility;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("Registered")
@EActivity(R.layout.activity_hotel_detail)
public class HotelDetailActivity extends BaseActivity<HotelDetailView, HotelDetailPresenter>
        implements HotelDetailView {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected HotelDetailPresenter presenter;
    
    @ViewById(R.id.activity_hotel_detail_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_province_hotel_detail_tb_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_hotel_detail_tv_hotel_name)
    protected TextView tvHotelName;
    @ViewById(R.id.activity_hotel_detail_tv_number_of_feedback)
    protected TextView tvNumberOfFeedback;
    @ViewById(R.id.activity_hotel_detail_iv_banner)
    protected ImageView ivBanner;
    @ViewById(R.id.activity_hotel_detail_tv_phone)
    protected TextView tvPhone;
    @ViewById(R.id.activity_hotel_detail_tv_address)
    protected TextView tvAddress;
    @ViewById(R.id.activity_hotel_detail_iv_map)
    protected ImageView ivMap;
    @ViewById(R.id.activity_hotel_detail_rcv_feedback)
    protected RecyclerView rcvFeedback;
    @ViewById(R.id.activity_hotel_detail_tv_none_feedback)
    protected TextView tvNoneFeedback;
    @ViewById(R.id.activity_hotel_detail_rcv_photo)
    protected RecyclerView rcvPhoto;
    
    @ViewById(R.id.activity_hotel_detail_ll_is_login)
    protected LinearLayout llIsLogin;
    @ViewById(R.id.activity_hotel_detail_ll_not_login)
    protected LinearLayout llNotLogin;
    @ViewById(R.id.activity_hotel_detail_civ_user_avatar)
    protected CircleImageView civUserAvatar;
    @ViewById(R.id.activity_hotel_detail_tv_user_name)
    protected TextView tvUserName;
    @ViewById(R.id.activity_hotel_detail_edt_feedback_content)
    protected TextView edtFeedbackContent;
    @ViewById(R.id.activity_hotel_detail_srb_feedback_rating)
    protected SimpleRatingBar srbFeedbackRating;
    @ViewById(R.id.activity_hotel_detail_tv_send_feedback)
    protected TextView tvSendFeedback;
    
    @Extra
    protected HotelResponse hotelResponse;
    
    @AfterInject
    void initInject() {
        DaggerHotelDetailComponent.builder()
                .applicationComponent(mainApplication.getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build().inject(this);
    }
    
    @AfterViews
    void init() {
        AppUtil.setupUI(llRoot);
        showKeyboard();
        
        nToolbar.setup(this, "TripGuy");
        nToolbar.showLeftIcon();
        nToolbar.showToolbarColor();
        nToolbar.setOnLeftClickListener(v -> onBackPressed());
        
        setupViewWithData();
        presenter.getHotelFeedback(hotelResponse.getId(), 0, 0);
    }
    
    private void showKeyboard() {
        KeyboardVisibility.setEventListener(this, isOpen -> {
            if (KeyboardVisibility.isKeyboardVisible(HotelDetailActivity.this)) {
                Log.e("keyboard", "show");
            } else {
                if (getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }
                Log.e("keyboard", "hide");
            }
        });
    }
    
    private void setupViewWithData() {
        Picasso.with(this).load(hotelResponse.getAvatar())
                .error(R.drawable.photo_not_available)
                .into(ivBanner);
        
        ivMap.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int ivMapHeight = ivMap.getHeight();
                        int ivMapWidth = ivMap.getWidth();
                        double latitude = hotelResponse.getLatitude();
                        
                        double longitude = hotelResponse.getLongitude();
                        
                        String s1 = "https://maps.googleapis.com/maps/api/staticmap?center=";
                        String s2 = "&zoom=18&scale=2&size=";
                        String s3 = "&markers=size:big%7Ccolor:0xff0000%7Clabel:%7C";
                        
                        String url = s1 + latitude + "," + longitude + s2 + ivMapWidth + "x" + ivMapHeight + s3 + latitude + "," + longitude;
                        Picasso.with(HotelDetailActivity.this).load(url).into(ivMap);
                        
                        if (ivMap.getViewTreeObserver().isAlive()) {
                            ivMap.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
        
        tvHotelName.setText(hotelResponse.getName());
        tvPhone.setText(hotelResponse.getPhone_number());
        tvAddress.setText(hotelResponse.getAddress());
        if (presenter.getCurrentUser() != null) {
            llIsLogin.setVisibility(View.VISIBLE);
            llNotLogin.setVisibility(View.GONE);
            tvSendFeedback.setBackground(getResources().getDrawable(R.drawable.bg_btn_green_radius_3dp));
            Picasso.with(this).load(presenter.getCurrentUser().getAvatar()).into(civUserAvatar);
            tvUserName.setText(presenter.getCurrentUser().getUsername());
        } else {
            llIsLogin.setVisibility(View.GONE);
            llNotLogin.setVisibility(View.VISIBLE);
            tvSendFeedback.setBackground(getResources().getDrawable(R.drawable.bg_btn_gray_radius_3dp_not_press));
        }
        
        rcvPhoto.setHasFixedSize(true);
        rcvPhoto.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvPhoto.setAdapter(new RecyclerImageAdapter(this, hotelResponse.getImages()));
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
        
    }
    
    @NonNull
    @Override
    public HotelDetailPresenter createPresenter() {
        return presenter;
    }
    
    @Override
    public void onBackPressed() {
        finish();
    }
    
    @Click(R.id.activity_hotel_detail_tv_send_feedback)
    void sendFeedbackClick() {
        if (presenter.getCurrentUser() != null && validateFeedbackInput()) {
            presenter.sendFeedback(hotelResponse.getId(),
                    new FeedbackRequest(edtFeedbackContent.getText().toString(), srbFeedbackRating.getRating()));
        }
    }
    
    private boolean validateFeedbackInput() {
        boolean validateResult = false;
        if (edtFeedbackContent.getText().toString().length() == 0) {
            Toast.makeText(this, "Nội dung còn trống!", Toast.LENGTH_SHORT).show();
        } else if (srbFeedbackRating.getRating() == 0) {
            Toast.makeText(this, "Bạn chưa chọn số sao!", Toast.LENGTH_SHORT).show();
        } else {
            validateResult = true;
        }
        return validateResult;
    }
    
    @Override
    public void getHotelFeedbackSuccess(List<FeedbackResponse> feedbackResponses) {
        if (feedbackResponses.size() != 0) {
            rcvFeedback.setVisibility(View.VISIBLE);
            tvNoneFeedback.setVisibility(View.GONE);
            
            this.rcvFeedback.setHasFixedSize(true);
            this.rcvFeedback.setLayoutManager(new LinearLayoutManager(this));
            this.rcvFeedback.setAdapter(new RecyclerHotelFeedbackAdapter(this, feedbackResponses));
            
            tvNumberOfFeedback.setText(String.format("%s đánh giá", feedbackResponses.size()));
        } else {
            rcvFeedback.setVisibility(View.GONE);
            tvNoneFeedback.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void postHotelFeedbackSuccess(FeedbackResponse feedbackResponse) {
        Toast.makeText(this, "Cảm ơn bạn đã gửi đánh giá.", Toast.LENGTH_SHORT).show();
    }
}