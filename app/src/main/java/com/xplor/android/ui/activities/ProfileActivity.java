package com.xplor.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xplor.android.R;
import com.xplor.android.ui.BaseActivity;
import com.xplor.android.ui.fragments.ProfileFragment;

import butterknife.OnClick;

public class ProfileActivity extends BaseActivity implements ProfileFragment.ProfileActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHomeAsUp();
        Fragment fragment = getContentFragment();
        if (fragment == null) {
            setContentFragment(ProfileFragment.newInstance());
        }
    }

    public static void startActivity(Context startingContext) {
        Intent intent = new Intent(startingContext, ProfileActivity.class);
        startingContext.startActivity(intent);
    }

    @Override
    public void onProfileAction() {
        logoutUser();
    }

    @OnClick(R.id.img_home_action)
    void onClick(View view) {
        finish();
    }
}
