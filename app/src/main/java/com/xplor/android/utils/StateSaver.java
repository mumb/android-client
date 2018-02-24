package com.xplor.android.utils;

import android.os.Bundle;

import com.xplor.android.models.User;

import java.util.Arrays;
import java.util.List;

public class StateSaver implements Saveable {
    private final List<? extends Saveable> componentsToSave;
    private static StateSaver instance;

    private StateSaver(User user) {
        componentsToSave = (Arrays.asList(user));
    }

    public static StateSaver getInstance() {
        if (instance == null) {
            instance = new StateSaver(User.getInstance());
        }
        return instance;
    }

    @Override
    public void saveInstanceState(Bundle bundle) {
        Bundle instanceState = new Bundle();
        for (Saveable saveable : componentsToSave) {
            saveable.saveInstanceState(instanceState);
        }
        bundle.putBundle("global-state", instanceState);
    }

    @Override
    public void restoreInstanceState(Bundle bundle) {
        if (bundle == null) {
            return;
        }

        Bundle instanceState = bundle.getBundle("global-state");
        if (instanceState == null) {
            return;
        }

        for (Saveable saveable : componentsToSave) {
            saveable.restoreInstanceState(instanceState);
        }
    }
}
