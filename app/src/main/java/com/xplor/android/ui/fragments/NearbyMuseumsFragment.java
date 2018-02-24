package com.xplor.android.ui.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.xplor.android.R;
import com.xplor.android.models.Museum;
import com.xplor.android.ui.MapBaseFragment;
import com.xplor.android.ui.adapters.NearbyMuseumsAdapter;
import com.xplor.android.utils.ApiSuccess;
import com.xplor.android.utils.Functions;
import com.xplor.android.utils.MapUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class NearbyMuseumsFragment extends MapBaseFragment<NearbyMuseumsFragment.NearbyMuseumsActionListener> {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    @BindView(R.id.viewPagerMuseums) ViewPager viewPagerMuseums;

    private NearbyMuseumsAdapter adapter;
    private LatLng myLatLng;

    public static NearbyMuseumsFragment newInstance() {
        return new NearbyMuseumsFragment();
    }

    @Override
    public void onViewInflated(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewPagerMuseums.setClipToPadding(false);
        int viewPagerPadding = getResources().getDimensionPixelSize(R.dimen.view_big_vertical_margin);
        viewPagerMuseums.setPadding(viewPagerPadding, 0, viewPagerPadding, 0);
        int pageMargin = getResources().getDimensionPixelSize(R.dimen.view_default_vertical_margin);
        viewPagerMuseums.setPageMargin(pageMargin);
    }

    @Override
    protected void onLocationReceived(Location location) {
        myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        getNearbyMuseums(location.getLatitude(), location.getLongitude());
    }

    private void getNearbyMuseums(double lat, double lng) {
        registerExecuteUseCaseWithActivityProgress(getUseCaseManager().getMuseums(lat, lng),
                this::onGetMuseumsList, null);
    }

    @ApiSuccess
    private void onGetMuseumsList(List<Museum> museums) {
        if (adapter == null) {
            adapter = new NearbyMuseumsAdapter(getChildFragmentManager());
            viewPagerMuseums.setAdapter(adapter);
        }
        adapter.setMuseums(museums);
        List<LatLng> latLngList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        if (museums != null && !museums.isEmpty()) {
            for (Museum museum : museums) {
                List<Double> coordinates = museum.getCoordinates();
                latLngList.add(new LatLng(coordinates.get(1), coordinates.get(0)));
                titleList.add(museum.getName());
            }
        } else {
            Functions.showToast(getContext(), R.string.no_musemus_found);
        }
        MapUtils.setNearbyMuseumsMarkers(latLngList, titleList, myLatLng, getMap());
    }

    @OnClick(R.id.containerSearch)
    void onClick(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            showErrorToast();
        } catch (GooglePlayServicesNotAvailableException e) {
            showErrorToast();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                myLatLng = place.getLatLng();
                getNearbyMuseums(myLatLng.latitude, myLatLng.longitude);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                showErrorToast();
            }
        }
    }

    private void showErrorToast() {
        Toast.makeText(getContext(), R.string.some_error_occurred, Toast.LENGTH_SHORT).show();
    }

    public LatLng getMyLatLng() {
        return myLatLng;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_nearby_museums;
    }

    public interface NearbyMuseumsActionListener {

    }
}
