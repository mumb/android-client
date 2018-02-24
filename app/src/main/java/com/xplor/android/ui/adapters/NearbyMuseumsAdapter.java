package com.xplor.android.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.xplor.android.models.Museum;
import com.xplor.android.ui.fragments.MuseumPagerFragment;

import java.util.List;

public class NearbyMuseumsAdapter extends FragmentStatePagerAdapter {
    private List<Museum> museums;
    private long baseId = 0;

    public NearbyMuseumsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MuseumPagerFragment.newInstance(museums.get(position));
    }

    @Override
    public int getCount() {
        return museums != null ? museums.size() : 0;
    }

    public void setMuseums(List<Museum> museums) {
        this.museums = museums;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }
}
