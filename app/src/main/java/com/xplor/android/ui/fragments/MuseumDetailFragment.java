package com.xplor.android.ui.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.xplor.android.R;
import com.xplor.android.models.Museum;
import com.xplor.android.models.MuseumDetailResponse;
import com.xplor.android.models.QuizCategory;
import com.xplor.android.models.UpcomingEvent;
import com.xplor.android.models.User;
import com.xplor.android.ui.MapBaseFragment;
import com.xplor.android.ui.activities.QuizActivity;
import com.xplor.android.ui.adapters.PhotosAdapter;
import com.xplor.android.ui.widgets.XplorImageView;
import com.xplor.android.utils.ApiSuccess;
import com.xplor.android.utils.Constants;
import com.xplor.android.utils.Functions;
import com.xplor.android.utils.MapUtils;
import com.xplor.android.utils.SpaceItemDecorator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MuseumDetailFragment extends MapBaseFragment<MuseumDetailFragment.MuseumDetailActionListener> {
    private static final String KEY_MUSEUM = "museum";
    private static final String KEY_MY_LAT_LNG = "myLatLng";

    @BindView(R.id.containerMuseumInfo) LinearLayout containerMuseumInfo;
    @BindView(R.id.containerQuizCategories) LinearLayout containerQuizCategories;
    @BindView(R.id.rvPhotos) RecyclerView rvPhotos;
    @BindView(R.id.containerUpcomingEvents) LinearLayout containerUpcomingEvents;
    @BindView(R.id.txtMuseumAddress) TextView txtMuseumAddress;
    @BindView(R.id.txtMuseumName) TextView txtMuseumName;
    @BindView(R.id.txtNumPhotosAdded) TextView txtNumPhotosAdded;
    @BindView(R.id.txtUpcomingEvents) TextView txtUpcomingEvents;
    @BindView(R.id.txtDistance) TextView txtDistance;
    @BindView(R.id.txtMuseumCityCountry) TextView txtMuseumCityCountry;
    @BindView(R.id.txtMuseumDescription) TextView txtMuseumDescription;
    @BindView(R.id.btnAbout) Button btnAbout;
    @BindView(R.id.btnQuizzes) Button btnQuizzes;
    @BindView(R.id.imgThumbnailMuseum) XplorImageView imgThumbnailMuseum;

    private Museum museum;
    private MuseumDetailResponse museumDetailResponse;
    private PhotosAdapter photosAdapter;

    public static MuseumDetailFragment newInstance(Museum museum, LatLng myLatLng) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_MUSEUM, museum);
        args.putParcelable(KEY_MY_LAT_LNG, myLatLng);
        MuseumDetailFragment fragment = new MuseumDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewInflated(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        photosAdapter = new PhotosAdapter();
        rvPhotos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        int space = getResources().getDimensionPixelSize(R.dimen.view_default_vertical_margin);
        rvPhotos.addItemDecoration(new SpaceItemDecorator(space));
        rvPhotos.setAdapter(photosAdapter);
        Bundle args = getArguments();
        museum = args != null ? args.getParcelable(KEY_MUSEUM) : null;
        if (museum != null) {
            txtMuseumAddress.setText(museum.getAddress());
            txtMuseumName.setText(museum.getName());
            txtMuseumCityCountry.setText(getString(R.string.city_country, museum.getCity(), museum.getCountry()));
            String distance = String.valueOf(Functions.round(museum.getDistance(), 1));
            txtDistance.setText(getString(R.string.distance_kms, distance));
        }
        setAboutOrQuizzesSelection(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (museumDetailResponse == null && museum != null) {
            registerExecuteUseCaseWithActivityProgress(
                    getUseCaseManager().getMuseumDetail(museum.getId()),
                    this::onGetMuseumDetail,
                    null);
        }
    }

    @ApiSuccess
    private void onGetMuseumDetail(MuseumDetailResponse museumDetailResponse) {
        this.museumDetailResponse = museumDetailResponse;
        txtMuseumDescription.setText(museumDetailResponse.getDescription());
        setMuseumPhotos();
        setUpcomingEvents();
        setQuizCategories();
    }

    private void setMuseumPhotos() {
        imgThumbnailMuseum.loadFromApi(museumDetailResponse.getThumbnailUrl(), R.drawable.museum_lg);
        List<String> photos = museumDetailResponse.getPhotos();
        if (photos != null && !photos.isEmpty()) {
            txtNumPhotosAdded.setText(getString(R.string.photos_added, photos.size()));
            photosAdapter.setPhotos(photos);
        } else {
            txtNumPhotosAdded.setText(getString(R.string.photos_added, 0));
            rvPhotos.setVisibility(View.GONE);
        }
    }

    private void setUpcomingEvents() {
        List<UpcomingEvent> events = museumDetailResponse.getUpcomingEvents();
        if (events != null && !events.isEmpty()) {
            txtUpcomingEvents.setText(getString(R.string.upcoming_events, events.size()));
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (int i = 0; i < events.size(); i++) {
                View eventView = inflater.inflate(R.layout.view_upcoming_event, containerUpcomingEvents, false);
                XplorImageView imgEventPhoto = eventView.findViewById(R.id.imgEventPhoto);
                TextView txtEventName = eventView.findViewById(R.id.txtEventName);
                TextView txtEventDate = eventView.findViewById(R.id.txtEventDate);

                UpcomingEvent event = events.get(i);
                imgEventPhoto.loadFromApi(event.getImageUrl(), R.drawable.event);
                txtEventName.setText(event.getName());
                txtEventDate.setText(event.getDate());

                int topMargin = getResources().getDimensionPixelSize(R.dimen.view_big_vertical_margin);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) eventView.getLayoutParams();
                if (i != 0) {
                    params.topMargin = topMargin;
                }
                containerUpcomingEvents.addView(eventView, params);
            }
        } else {
            txtUpcomingEvents.setText(getString(R.string.upcoming_events, 0));
            containerUpcomingEvents.setVisibility(View.GONE);
        }
    }

    private void setQuizCategories() {
        List<QuizCategory> quizCategories = museumDetailResponse.getQuizCategories();
        if (quizCategories != null && !quizCategories.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (int i = 0; i < quizCategories.size(); i++) {
                View categoryView = inflater.inflate(R.layout.view_quiz_category, containerQuizCategories, false);
                TextView txtQuizCategory = categoryView.findViewById(R.id.txtQuizCategory);
                QuizCategory quizCategory = quizCategories.get(i);
                txtQuizCategory.setText(quizCategory.getCategory());

                int topMargin = getResources().getDimensionPixelSize(R.dimen.edit_text_left_padding);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) categoryView.getLayoutParams();
                if (i != 0) {
                    params.topMargin = topMargin;
                }
                containerQuizCategories.addView(categoryView, params);
                categoryView.setOnClickListener(view -> {
                    if (User.getInstance().isLoggedIn()) {
                        QuizActivity.startActivity(getContext(), quizCategory.getQuizId());
                    } else {
                        Functions.showToast(getContext(), R.string.need_to_log_in_start_quiz);
                    }
                });
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);
        Bundle args = getArguments();
        LatLng myLatLng = args != null ? args.getParcelable(KEY_MY_LAT_LNG) : null;
        if (myLatLng != null) {
            drawMapFeatures(args.getParcelable(KEY_MY_LAT_LNG));
        }
    }

    private void drawMapFeatures(LatLng myLatLng) {
        LatLng museumLatLng;
        String museumName = "";
        if (museum != null) {
            List<Double> coordinates = museum.getCoordinates();
            museumLatLng = new LatLng(coordinates.get(1), coordinates.get(0));
            museumName = museum.getName();
        } else {
            museumLatLng = new LatLng(19.112025, 72.897578);
        }

        List<Marker> markers = new ArrayList<>(2);
        markers.add(MapUtils.addSingleMarker(museumLatLng, museumName, getMap(), R.drawable.geo));
        markers.add(MapUtils.addSingleMarker(myLatLng, Constants.YOU, getMap(), R.drawable.ic_person_pin));
        MapUtils.zoomCameraAppropriately(markers, getMap());
        MapUtils.plotPolyLines(
                map,
                getString(R.string.google_maps_key),
                myLatLng,
                museumLatLng
        );
    }

    @Override
    protected void onLocationReceived(Location location) {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mueseum_detail;
    }

    @OnClick({
            R.id.btnAbout,
            R.id.btnQuizzes,
            R.id.btnExploreQuizzes
    })
    void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnAbout:
                setAboutOrQuizzesSelection(true);
                break;
            case R.id.btnQuizzes:
                setAboutOrQuizzesSelection(false);
                break;
            case R.id.btnExploreQuizzes:
                setAboutOrQuizzesSelection(false);
        }
    }

    private void setAboutOrQuizzesSelection(boolean isAboutSelected) {
        if (isAboutSelected) {
            btnAbout.setSelected(true);
            btnQuizzes.setSelected(false);
            containerMuseumInfo.setVisibility(View.VISIBLE);
            containerQuizCategories.setVisibility(View.GONE);
        } else {
            btnAbout.setSelected(false);
            btnQuizzes.setSelected(true);
            containerMuseumInfo.setVisibility(View.GONE);
            containerQuizCategories.setVisibility(View.VISIBLE);
        }
    }

    public interface MuseumDetailActionListener {

    }
}
