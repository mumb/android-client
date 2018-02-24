package com.xplor.android.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class AppPreferences {
    private final SharedPreferences mPreferences;
    private final Gson gson;

    public AppPreferences(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new Gson();
    }

    private void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}
