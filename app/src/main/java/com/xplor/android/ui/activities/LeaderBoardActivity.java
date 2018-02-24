package com.xplor.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xplor.android.R;
import com.xplor.android.ui.BaseActivity;
import com.xplor.android.ui.fragments.LeaderBoardFragment;

import butterknife.OnClick;

public class LeaderBoardActivity extends BaseActivity implements LeaderBoardFragment.LeaderBoardActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenTitle(getString(R.string.app_name));
        setHomeAsUp();
        Fragment fragment = getContentFragment();
        if (fragment == null) {
            fragment = LeaderBoardFragment.newInstance();
            setContentFragment(fragment);
        }
    }

    public static void startActivity(Context startingContext) {
        Intent intent = new Intent(startingContext, LeaderBoardActivity.class);
        startingContext.startActivity(intent);
    }

    @OnClick(R.id.img_home_action)
    void onClick(View view) {
        finish();
    }
}
