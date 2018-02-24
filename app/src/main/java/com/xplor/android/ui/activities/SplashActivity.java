package com.xplor.android.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.xplor.android.models.User;
import com.xplor.android.ui.BaseActivity;
import com.xplor.android.ui.fragments.SplashFragment;

public class SplashActivity extends BaseActivity implements SplashFragment.SplashFragmentActionListener {
    private static final int MANADATORY_WAIT_MS = 2000;
    private boolean isSessionVerificationComplete;
    private boolean isMandatoryWaitComplete;
    private Handler handler = new Handler();
    private Runnable waitRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();

        Fragment fragment = getContentFragment();
        if (fragment == null) {
            setContentFragment(SplashFragment.newInstance());
        }
    }

    @Override
    public void onSessionVerificationComplete() {
        isSessionVerificationComplete = true;
        startNextActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        waitRunnable = () -> {
            isMandatoryWaitComplete = true;
            startNextActivity();
        };
        handler.postDelayed(waitRunnable, MANADATORY_WAIT_MS);
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(waitRunnable);
        super.onStop();
    }

    private void startNextActivity() {
        if (isSessionVerificationComplete && isMandatoryWaitComplete) {
            if (!User.getInstance().isLoggedIn()) {
                LoginActivity.startActivity(this);
            } else {
                NearbyMuseumsActivity.startActivity(this);
            }
        }
    }
}
