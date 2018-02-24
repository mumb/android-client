package com.xplor.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xplor.android.R;

public class Functions {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm != null ? cm.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String getTag(Class classType) {
        return classType.getSimpleName();
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static String getPhotoUrl(String facebookId) {
        Uri uri = Uri.parse("https://graph.facebook.com/").buildUpon()
                .appendPath(facebookId)
                .appendPath("picture")
                .appendQueryParameter("type", "small")
                .build();
        return uri.toString();
    }

    public static void loadImageIntoCircularImageView(Context context, String url, ImageView imgProfile) {
        Picasso.with(context).load(url).
                noFade().
                placeholder(R.drawable.profile_image_placeholder).
                into(imgProfile);
    }

    public static void showToast(Context context, @StringRes int stringRes) {
        Toast toast = Toast.makeText(context, stringRes, Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) {
            v.setGravity(Gravity.CENTER);
        }
        toast.show();
    }
}
