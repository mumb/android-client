package com.xplor.android.ui.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.xplor.android.ui.widgets.utils.TypefaceUtils;

public class XplorTextView extends AppCompatTextView {

    public XplorTextView(Context context) {
        super(context);
        init(context, null);
    }

    public XplorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public XplorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypefaceUtils.initView(this, context, attrs);
        setIncludeFontPadding(false);
    }
}
