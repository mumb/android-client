package com.xplor.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.willy.ratingbar.ScaleRatingBar;
import com.xplor.android.R;
import com.xplor.android.models.Museum;
import com.xplor.android.ui.BaseFragment;
import com.xplor.android.ui.widgets.XplorImageView;
import com.xplor.android.utils.Functions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MuseumPagerFragment extends BaseFragment<MuseumPagerFragment.MuseumClickListener> {
    private static final String KEY_MUSEUM = "museum";

    @BindView(R.id.rootView) View rootView;
    @BindView(R.id.imgThumbnailMuseum) XplorImageView imgThumbnailMuseum;
    @BindView(R.id.txtMuseumName) TextView txtMuseumName;
    @BindView(R.id.simpleRatingBar) ScaleRatingBar ratingBar;
    @BindView(R.id.txtDistance) TextView txtDistance;
    @BindView(R.id.txtMuseumAddress) TextView txtMuseumAddress;

    private Museum museum;

    public static MuseumPagerFragment newInstance(Museum museum) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_MUSEUM, museum);
        MuseumPagerFragment fragment = new MuseumPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_museum_viewpager_item, container, false);
        bindView(view);

        Bundle args = getArguments();
        museum = args != null ? args.getParcelable(KEY_MUSEUM) : null;
        if (museum != null) {
            txtMuseumName.setText(museum.getName());
            String distance = String.valueOf(Functions.round(museum.getDistance(), 1));
            txtDistance.setText(getString(R.string.distance_kms, distance));
            ratingBar.setRating(museum.getRating());
            txtMuseumAddress.setText(museum.getAddress());
            imgThumbnailMuseum.loadFromApi(museum.getThumbnailUrl(), R.drawable.museum_xs);
        }
        return view;
    }

    @OnClick(R.id.rootView)
    void onClick(View view) {
        if (fragmentActionListener != null && museum != null) {
            fragmentActionListener.onMuseumClicked(museum);
        }
    }

    public interface MuseumClickListener {
        void onMuseumClicked(Museum museum);
    }
}
