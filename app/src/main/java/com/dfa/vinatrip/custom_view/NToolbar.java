package com.dfa.vinatrip.custom_view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfa.vinatrip.R;
import com.dfa.vinatrip.utils.MainCallbackListener;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by duonghd on 10/9/2017.
 */

@EViewGroup(R.layout.view_ntoolbar)
public class NToolbar extends AppBarLayout implements MainCallbackListener {
    @ViewById(R.id.view_ntoolbar)
    protected Toolbar toolbar;
    @ViewById(R.id.view_ntoolbar_iv_left_icon)
    protected ImageView ivLeftIcon;
    @ViewById(R.id.view_ntoolbar_iv_right_icon)
    protected ImageView ivRightIcon;
    @ViewById(R.id.view_ntoolbar_tv_title)
    protected TextView tvTitle;
    @ViewById(R.id.view_ntoolbar_civ_app_icon)
    protected CircleImageView civAppIcon;
    @ViewById(R.id.view_ntoolbar_ll_root)
    protected LinearLayout llRoot;
    
    private AppCompatActivity activity;
    private boolean showLeftIcon = false;
    private boolean showRightIcon = false;
    private boolean showAppIcon = false;
    private boolean showToolbarColor = false;
    private boolean checkSetup = true;

    public NToolbar(Context context) {
        super(context);
    }

    public NToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean defaultSetup(AppCompatActivity activity) {
        this.activity = activity;
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            ivLeftIcon.setVisibility(showLeftIcon ? VISIBLE : GONE);
            ivRightIcon.setVisibility(showRightIcon ? VISIBLE : GONE);
            civAppIcon.setVisibility(showAppIcon ? VISIBLE : GONE);
            toolbar.setBackgroundColor(
                    showToolbarColor ? activity.getResources().getColor(R.color.colorMain) :
                            activity.getResources().getColor(R.color.transparent));
        } else {
            checkSetup = false;
        }
        return checkSetup;
    }

    @Override
    public boolean setup(AppCompatActivity activity, String title) {
        if (defaultSetup(activity)) {
            if (tvTitle != null) {
                tvTitle.setText(title);
            }
        }
        return checkSetup;
    }

    @Override
    public boolean setup(AppCompatActivity activity, String title, @DrawableRes int icLeft) {
        if (setup(activity, title)) {
            if (ivLeftIcon != null) {
                if (icLeft != 0) {
                    ivLeftIcon.setVisibility(View.VISIBLE);
                    ivLeftIcon.setImageResource(icLeft);
                } else {
                    ivLeftIcon.setVisibility(View.INVISIBLE);
                }
            }
        }
        return checkSetup;
    }

    @Override
    public boolean setup(AppCompatActivity activity, String title, @DrawableRes int icLeft, @DrawableRes int icRight) {
        if (setup(activity, title, icLeft)) {
            if (ivRightIcon != null) {
                if (icRight != 0) {
                    ivRightIcon.setVisibility(View.VISIBLE);
                    ivRightIcon.setImageResource(icRight);
                } else {
                    ivRightIcon.setVisibility(View.GONE);
                }
            }
        }
        return checkSetup;
    }

    @Override
    public void showAppIcon() {
        if (checkSetup) {
            showAppIcon = true;
            civAppIcon.setVisibility(VISIBLE);
        }
    }

    @Override
    public void hideAppIcon() {
        if (checkSetup) {
            showAppIcon = false;
            civAppIcon.setVisibility(GONE);
        }
    }

    @Override
    public void showLeftIcon() {
        if (checkSetup) {
            showLeftIcon = true;
            ivLeftIcon.setVisibility(VISIBLE);
        }
    }

    @Override
    public void hideLeftIcon() {
        if (checkSetup) {
            showLeftIcon = false;
            ivLeftIcon.setVisibility(GONE);
        }
    }

    @Override
    public void showRightIcon() {
        if (checkSetup) {
            showRightIcon = true;
            ivRightIcon.setVisibility(VISIBLE);
        }
    }

    @Override
    public void hideRightIcon() {
        if (checkSetup) {
            showRightIcon = false;
            ivRightIcon.setVisibility(GONE);
        }
    }

    @Override
    public void showToolbarColor() {
        if (checkSetup) {
            showToolbarColor = true;
            toolbar.setBackgroundColor(activity.getResources().getColor(R.color.colorMain));
        }
    }

    @Override
    public void hideToolbarColor() {
        if (checkSetup) {
            showToolbarColor = false;
            toolbar.setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public void setTitle(String title) {
        if (checkSetup) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void setOnLeftClickListener(View.OnClickListener onLeftClickListener) {
        ivLeftIcon.setOnClickListener(onLeftClickListener);
    }

    @Override
    public void setOnRightClickListener(View.OnClickListener onRightClickListener) {
        ivRightIcon.setOnClickListener(onRightClickListener);
    }

    @Override
    public void setOnLlRootListener(View.OnClickListener onLlRootClickListener) {
        llRoot.setOnClickListener(onLlRootClickListener);
    }
}