package com.dfa.vinatrip.domains.province_detail.view_all.festival.festival_detail;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.MainApplication;
import com.dfa.vinatrip.R;
import com.dfa.vinatrip.base.BaseActivity;
import com.dfa.vinatrip.base.LoginDialog;
import com.dfa.vinatrip.custom_view.NToolbar;
import com.dfa.vinatrip.custom_view.SimpleRatingBar;
import com.dfa.vinatrip.infrastructures.ActivityModule;
import com.dfa.vinatrip.models.request.AuthRequest;
import com.dfa.vinatrip.models.response.feedback.FeedbackResponse;
import com.dfa.vinatrip.models.response.festival.FestivalResponse;
import com.dfa.vinatrip.models.response.user.User;
import com.dfa.vinatrip.utils.AppUtil;
import com.dfa.vinatrip.utils.KeyboardVisibility;
import com.dfa.vinatrip.utils.MapActivity_;
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

/**
 * Created by duonghd on 10/6/2017.
 * duonghd1307@gmail.com
 */

@SuppressLint("Registered")
@EActivity(R.layout.activity_festival_detail)
public class FestivalDetailActivity extends BaseActivity<FestivalDetailView, FestivalDetailPresenter>
        implements FestivalDetailView, LoginDialog.CallbackActivity {
    @App
    protected MainApplication mainApplication;
    @Inject
    protected FestivalDetailPresenter presenter;

    @ViewById(R.id.activity_festival_detail_ll_root)
    protected LinearLayout llRoot;
    @ViewById(R.id.activity_province_festival_detail_tb_toolbar)
    protected NToolbar nToolbar;
    @ViewById(R.id.activity_festival_detail_tv_festival_name)
    protected TextView tvfestivalName;
    @ViewById(R.id.activity_festival_detail_tv_number_of_feedback)
    protected TextView tvNumberOfFeedback;
    @ViewById(R.id.activity_festival_detail_iv_banner)
    protected ImageView ivBanner;
    @ViewById(R.id.activity_festival_detail_tv_intro)
    protected TextView tvIntro;
    @ViewById(R.id.activity_festival_detail_tv_address)
    protected TextView tvAddress;
    @ViewById(R.id.activity_festival_detail_iv_map)
    protected ImageView ivMap;
    @ViewById(R.id.activity_festival_detail_rcv_feedback)
    protected RecyclerView rcvFeedback;
    @ViewById(R.id.activity_festival_detail_tv_none_feedback)
    protected TextView tvNoneFeedback;

    @ViewById(R.id.activity_festival_detail_ll_is_login)
    protected LinearLayout llIsLogin;
    @ViewById(R.id.activity_festival_detail_ll_not_login)
    protected LinearLayout llNotLogin;
    @ViewById(R.id.activity_festival_detail_civ_user_avatar)
    protected CircleImageView civUserAvatar;
    @ViewById(R.id.activity_festival_detail_tv_user_name)
    protected TextView tvUserName;
    @ViewById(R.id.activity_festival_detail_edt_feedback_content)
    protected TextView edtFeedbackContent;
    @ViewById(R.id.activity_festival_detail_srb_feedback_rating)
    protected SimpleRatingBar srbFeedbackRating;
    @ViewById(R.id.activity_festival_detail_tv_send_feedback)
    protected TextView tvSendFeedback;

    @Extra
    protected FestivalResponse festivalResponse;

    private LoginDialog loginDialog;

    @AfterInject
    void initInject() {
        DaggerFestivalDetailComponent.builder()
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
        //presenter.getFestivalFeedback(festivalResponse.getId(), 0, 0);
        loginDialog = new LoginDialog(this);
        loginDialog.setCancelable(false);
        loginDialog.setCanceledOnTouchOutside(false);
        loginDialog.setOnDismissListener(dialog -> {
            loginDialog.clearData();
        });
    }

    private void showKeyboard() {
        KeyboardVisibility.setEventListener(this, isOpen -> {
            if (!KeyboardVisibility.isKeyboardVisible(FestivalDetailActivity.this)) {
                if (getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }

                if (loginDialog != null && loginDialog.getCurrentFocus() != null) {
                    loginDialog.getCurrentFocus().clearFocus();
                }
            }
        });
    }

    private void setupViewWithData() {
        Picasso.with(this).load(festivalResponse.getAvatar())
                .error(R.drawable.photo_not_available)
                .into(ivBanner);

        ivMap.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int ivMapHeight = ivMap.getHeight();
                        int ivMapWidth = ivMap.getWidth();
                        double latitude = festivalResponse.getLatitude();

                        double longitude = festivalResponse.getLongitude();

                        String s1 = "https://maps.googleapis.com/maps/api/staticmap?center=";
                        String s2 = "&zoom=18&scale=2&size=";
                        String s3 = "&markers=size:big%7Ccolor:0xff0000%7Clabel:%7C";

                        String url = s1 + latitude + "," + longitude + s2 + AppUtil.dpToPx(FestivalDetailActivity.this, ivMapWidth) + "x" + AppUtil.dpToPx(FestivalDetailActivity.this, ivMapHeight) + s3 + latitude + "," + longitude;
                        Picasso.with(FestivalDetailActivity.this).load(url).into(ivMap);

                        if (ivMap.getViewTreeObserver().isAlive()) {
                            ivMap.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

        tvfestivalName.setText(festivalResponse.getName());
        tvAddress.setText(festivalResponse.getAddress());
        tvIntro.setText(festivalResponse.getDescription());
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
    }

    @NonNull
    @Override
    public FestivalDetailPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void showLoading() {
        showHUD();
    }

    @Override
    public void hideLoading() {
        hideHUD();
    }

    @Click(R.id.activity_festival_detail_iv_map)
    void ivMapClick() {
        MapActivity_.intent(this)
                .title(festivalResponse.getName())
                .latitude(festivalResponse.getLatitude())
                .longitude(festivalResponse.getLongitude())
                .start();
    }

    @Click(R.id.activity_festival_detail_tv_login)
    void tvLoginCLick() {
        loginDialog.show();
    }

    @Override
    public void loginInfo(String email, String password) {
        loginDialog.dismiss();
        presenter.loginWithEmail(new AuthRequest(email, password));
    }

    @Override
    public void signInSuccess(User user) {
        llIsLogin.setVisibility(View.VISIBLE);
        llNotLogin.setVisibility(View.GONE);
        tvSendFeedback.setBackground(getResources().getDrawable(R.drawable.bg_btn_green_radius_3dp));
        Picasso.with(this).load(user.getAvatar()).into(civUserAvatar);
        tvUserName.setText(user.getUsername());
    }

    @Override
    public void apiError(Throwable throwable) {
        Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getFestivalFeedbackSuccess(List<FeedbackResponse> feedbackResponses) {

    }

    @Override
    public void postFestivalFeedbackSuccess(FeedbackResponse feedbackResponse) {

    }
}