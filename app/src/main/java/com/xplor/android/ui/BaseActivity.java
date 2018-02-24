package com.xplor.android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xplor.android.App;
import com.xplor.android.BuildConfig;
import com.xplor.android.R;
import com.xplor.android.domain.UseCaseManager;
import com.xplor.android.models.RetryCallEvent;
import com.xplor.android.models.User;
import com.xplor.android.ui.activities.LoginActivity;
import com.xplor.android.ui.widgets.iconify.IconDrawable;
import com.xplor.android.utils.StateSaver;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    private int activityTheme;
    private int themeToolbarDrawableColor;
    private static final int THEME_DEFAULT = 1;
    private static final int THEME_DARK = 2;

    @Nullable
    @BindView(R.id.base_coordinatorLayout)
    CoordinatorLayout mainCoordinatorLayout;

    @Nullable
    @BindView(R.id.app_toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.txt_toolbar_title)
    TextView txtToolbarTitle;

    @Nullable
    @BindView(R.id.img_home_action)
    ImageView imgToolbarHomeIcon;

    @Nullable
    @BindView(R.id.container_progress)
    ViewGroup containerProgress;

    @Nullable
    @BindView(R.id.activity_progressbar)
    ProgressBar activityProgressBar;

    @Nullable
    @BindView(R.id.txtActivityProgress)
    TextView txtProgress;

    private Snackbar snackbarInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypedValue outValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.themeName, outValue, false);
        activityTheme = outValue.data;

        StateSaver.getInstance().restoreInstanceState(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        setActionBar();

        if (containerProgress != null) {
            containerProgress.setOnTouchListener((view, motionEvent) -> true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        StateSaver.getInstance().saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    protected int getContentView() {
        return R.layout.activity_base;
    }

    protected void setActionBar() {
        if (toolbar != null) {
            int toolbarColor, titleColor;
            if (activityTheme == THEME_DEFAULT) {
                toolbarColor = ContextCompat.getColor(this, android.R.color.transparent);
                themeToolbarDrawableColor = Color.WHITE;
                titleColor = Color.WHITE;
            } else {
                toolbarColor = ContextCompat.getColor(this, android.R.color.white);
                themeToolbarDrawableColor = ContextCompat.getColor(this, R.color.textColor);
                titleColor = ContextCompat.getColor(this, R.color.textColor);
            }
            toolbar.setBackgroundColor(toolbarColor);
            if (txtToolbarTitle != null) {
                txtToolbarTitle.setTextColor(titleColor);
            }
            setSupportActionBar(toolbar);
        }
    }

    protected void hideActionBar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
    }

    protected void setScreenTitle(String title) {
        if (txtToolbarTitle != null) {
            txtToolbarTitle.setText(title);
        }
    }

    protected void setHomeAsUp() {
        if (imgToolbarHomeIcon != null) {
            IconDrawable icBack = new IconDrawable(this, BuildConfig.IC_ARROW_LEFT)
                    .sizePx(getResources().getDimensionPixelSize(R.dimen.toolbar_icon_size)).color(themeToolbarDrawableColor);
            imgToolbarHomeIcon.setImageDrawable(icBack);
        }
    }

    protected void setHomeAsMenu() {
        if (imgToolbarHomeIcon != null) {
            IconDrawable icBack = new IconDrawable(this, BuildConfig.IC_MENU)
                    .sizePx(getResources().getDimensionPixelSize(R.dimen.toolbar_icon_size)).color(themeToolbarDrawableColor);
            imgToolbarHomeIcon.setImageDrawable(icBack);
        }
    }

    protected void setHomeAsClose() {
        if (imgToolbarHomeIcon != null) {
            IconDrawable icClose = new IconDrawable(this, BuildConfig.IC_CROSS)
                    .sizePx(getResources().getDimensionPixelSize(R.dimen.toolbar_icon_size)).color(themeToolbarDrawableColor);
            imgToolbarHomeIcon.setImageDrawable(icClose);
        }
    }

    public UseCaseManager getUseCaseManager() {
        return ((App) getApplicationContext()).getUseCaseManager();
    }

    protected App getApp() {
        return (App) getApplicationContext();
    }

    public void showActivityProgressWithText(String progressText) {
        if (containerProgress != null && txtProgress != null) {
            containerProgress.setVisibility(View.VISIBLE);
            if (activityProgressBar != null) {
                activityProgressBar.setVisibility(View.VISIBLE);
            }
            if (!TextUtils.isEmpty(progressText)) {
                txtProgress.setVisibility(View.VISIBLE);
                txtProgress.setText(progressText);
            } else {
                txtProgress.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void hideActivityProgress() {
        if (containerProgress != null) {
            containerProgress.setVisibility(View.GONE);
        }
    }

    public void showNetworkError(int errorStrResId, boolean showRetry) {
        if (mainCoordinatorLayout != null) {
            snackbarInfo = Snackbar.make(mainCoordinatorLayout, errorStrResId, Snackbar.LENGTH_INDEFINITE);
            if (showRetry) {
                snackbarInfo.setAction(R.string.retry, v -> {
                    snackbarInfo.dismiss();
                    EventBus.getDefault().post(new RetryCallEvent());
                });
            }
            View snackbarView = snackbarInfo.getView();
            snackbarInfo.setActionTextColor(Color.YELLOW);
            snackbarView.setBackgroundColor(Color.BLACK);
            TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbarInfo.show();
        }
    }

    public void hideSnackbar() {
        if (snackbarInfo != null) {
            if (snackbarInfo.isShown()) {
                snackbarInfo.dismiss();
            }
        }
    }

    protected void setContentFragment(Fragment fragment) {
        setContentFragment(fragment, false);
    }

    protected void setContentFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().
                replace(R.id.activity_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    protected Fragment getContentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.activity_container);
    }

    protected void logoutUser() {
        User.removeUserInstance();
        getUseCaseManager().logout();
        LoginActivity.startActivity(this);
    }
}
