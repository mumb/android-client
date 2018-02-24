package com.xplor.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.xplor.android.R;
import com.xplor.android.models.Museum;
import com.xplor.android.ui.DrawerBaseActivity;
import com.xplor.android.ui.fragments.MuseumDetailFragment;

public class MuseumDetailActivity extends DrawerBaseActivity implements MuseumDetailFragment.MuseumDetailActionListener {
    private static final String KEY_MUSEUM = "museum";
    private static final String KEY_MY_LAT_LNG = "myLatLng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenTitle(getString(R.string.app_name));
        Fragment fragment = getContentFragment();
        if (fragment == null) {
            Intent intent = getIntent();
            fragment = MuseumDetailFragment.newInstance(intent.getParcelableExtra(KEY_MUSEUM),
                    intent.getParcelableExtra(KEY_MY_LAT_LNG));
            setContentFragment(fragment);
        }
    }

    public static void startActivity(Context startingContext, Museum museum, LatLng myLatLng) {
        Intent intent = new Intent(startingContext, MuseumDetailActivity.class);
        intent.putExtra(KEY_MUSEUM, museum);
        intent.putExtra(KEY_MY_LAT_LNG, myLatLng);
        startingContext.startActivity(intent);
    }
}
