package com.xplor.android.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

public class XplorMapView extends MapView {
    public XplorMapView(Context context) {
        super(context);
    }

    public XplorMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public XplorMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public XplorMapView(Context context, GoogleMapOptions googleMapOptions) {
        super(context, googleMapOptions);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_DOWN:
                this.getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
