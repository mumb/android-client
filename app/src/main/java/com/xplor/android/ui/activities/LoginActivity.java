package com.xplor.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xplor.android.ui.BaseActivity;
import com.xplor.android.ui.fragments.LoginFragment;

public class LoginActivity extends BaseActivity implements LoginFragment.LoginActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();

        Fragment fragment = getContentFragment();
        if (fragment == null) {
            setContentFragment(LoginFragment.newInstance());
        }
    }

    public static void startActivity(Context startingContext) {
        Intent intent = new Intent(startingContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startingContext.startActivity(intent);
    }

    @Override
    public void onLoginSuccessful() {
        NearbyMuseumsActivity.startActivity(this);
        finish();
    }

    @Override
    public void onContinueWithoutLogin() {
        NearbyMuseumsActivity.startActivity(this);
        finish();
    }
}
