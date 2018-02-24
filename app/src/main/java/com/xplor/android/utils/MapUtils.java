package com.xplor.android.utils;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.xplor.android.R;

import java.util.ArrayList;
import java.util.List;

import static com.xplor.android.utils.Constants.YOU;

public class MapUtils {
    private static final float DEFAULT_ZOOM = 15f;

    public static void plotPolyLines(GoogleMap map, String apiKey, LatLng origin, LatLng dest) {
        //Define list to get all latlng for the route

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        String originLatLng = origin.latitude + "," + origin.longitude;
        String destLatLng = dest.latitude + "," + dest.longitude;
        DirectionsApiRequest req = DirectionsApi.getDirections(context, originLatLng, destLatLng);
        req.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                if (result.routes != null && result.routes.length > 0) {
                    DirectionsRoute route = result.routes[0];
                    List<LatLng> path = new ArrayList<>();
                    if (route.legs != null) {
                        for (int i = 0; i < route.legs.length; i++) {
                            DirectionsLeg leg = route.legs[i];
                            if (leg.steps != null) {
                                for (int j = 0; j < leg.steps.length; j++) {
                                    DirectionsStep step = leg.steps[j];
                                    if (step.steps != null && step.steps.length > 0) {
                                        for (int k = 0; k < step.steps.length; k++) {
                                            DirectionsStep step1 = step.steps[k];
                                            EncodedPolyline points1 = step1.polyline;
                                            if (points1 != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                for (com.google.maps.model.LatLng coord1 : coords1) {
                                                    path.add(new LatLng(coord1.lat, coord1.lng));
                                                }
                                            }
                                        }
                                    } else {
                                        EncodedPolyline points = step.polyline;
                                        if (points != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords = points.decodePath();
                                            for (com.google.maps.model.LatLng coord : coords) {
                                                path.add(new LatLng(coord.lat, coord.lng));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        if (path.size() > 0) {
                            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.YELLOW).width(15);
                            map.addPolyline(opts);
                        }
                        map.getUiSettings().setZoomControlsEnabled(true);
                    });
                }
            }

            @Override
            public void onFailure(Throwable e) {

            }

        });
    }

    public static void setNearbyMuseumsMarkers(@Nullable List<LatLng> latLngList, List<String> titles,
                                               LatLng yourLocation, GoogleMap map) {
        map.clear();
        List<Marker> markers = new ArrayList<>();
        if (latLngList != null && !latLngList.isEmpty()) {
            for (int i = 0; i < latLngList.size(); i++) {
                LatLng latLng = latLngList.get(i);
                String title = titles.get(i);
                markers.add(addSingleMarker(latLng, title, map, R.drawable.geo_red));
            }
        }
        markers.add(addSingleMarker(yourLocation, YOU, map, R.drawable.ic_person_pin));
        zoomCameraAppropriately(markers, map);
    }

    public static Marker addSingleMarker(LatLng latLng, String title, GoogleMap map, @DrawableRes int drawableRes) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(drawableRes);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(icon).title(title).visible(true);
        return map.addMarker(markerOptions);
    }

    public static void zoomCameraAppropriately(@NonNull List<Marker> markers, GoogleMap map) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu;
        if (markers.size() > 1) {
            cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        } else {
            cu = CameraUpdateFactory.newLatLngZoom(markers.get(0).getPosition(), DEFAULT_ZOOM);
        }
        map.animateCamera(cu);
    }
}
