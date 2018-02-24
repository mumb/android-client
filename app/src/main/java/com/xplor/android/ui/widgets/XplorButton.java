package com.xplor.android.ui.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.xplor.android.R;
import com.xplor.android.ui.widgets.iconify.Iconify;
import com.xplor.android.ui.widgets.utils.TypefaceUtils;

public class XplorButton extends AppCompatButton {

    public XplorButton(Context context) {
        this(context, null);
    }

    public XplorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public XplorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypefaceUtils.initView(this, context, attrs);
        }

        Drawable icDrawable = Iconify.getDrawable(context, attrs);
        if (icDrawable != null) {
            setCompoundDrawablesWithIntrinsicBounds(icDrawable, null, null, null);
            setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.button_drawable_padding));
        }
    }
}
