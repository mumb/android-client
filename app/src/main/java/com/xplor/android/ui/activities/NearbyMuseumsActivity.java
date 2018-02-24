package com.xplor.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xplor.android.models.Museum;
import com.xplor.android.ui.DrawerBaseActivity;
import com.xplor.android.ui.fragments.MuseumPagerFragment;
import com.xplor.android.ui.fragments.NearbyMuseumsFragment;

public class NearbyMuseumsActivity extends DrawerBaseActivity implements
        NearbyMuseumsFragment.NearbyMuseumsActionListener,
        MuseumPagerFragment.MuseumClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = getContentFragment();
        if (fragment == null) {
            setContentFragment(NearbyMuseumsFragment.newInstance());
        }
    }

    public static void startActivity(Context startingContext) {
        Intent intent = new Intent(startingContext, NearbyMuseumsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startingContext.startActivity(intent);
    }

    @Override
    public void onMuseumClicked(Museum museum) {
        Fragment fragment = getContentFragment();
        if (fragment instanceof NearbyMuseumsFragment) {
            MuseumDetailActivity.startActivity(this, museum, ((NearbyMuseumsFragment) fragment).getMyLatLng());
        }
    }
}
