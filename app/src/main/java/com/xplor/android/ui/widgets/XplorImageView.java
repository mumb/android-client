package com.xplor.android.ui.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

public class XplorImageView extends AppCompatImageView {
    private ScaleType mPendingScaleType;
    private boolean mIsZoomEnabled;

    public XplorImageView(Context context) {
        super(context);
        init(context, null);
    }

    public XplorImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public XplorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
    }

    public void loadFromFile(String filePath) {
        File bitmapFile = new File(filePath);
        Context context = getContext();
        Picasso.with(context).invalidate(bitmapFile);
        Picasso.with(context).load(bitmapFile).into(this);
    }

    public void loadFromApi(String url) {
        Picasso.with(getContext()).load(url).into(this);
    }

    public void loadFromApi(String url, int placeHolder) {
        Picasso.with(getContext()).load(url).
                memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).
                networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).
                placeholder(placeHolder).into(this);
    }

    public void loadFromApi(String url, Drawable placeHolder) {
        Picasso.with(getContext()).load(url).placeholder(placeHolder).into(this);
    }
}
