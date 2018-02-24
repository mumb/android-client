package com.xplor.android.storage.local;


import android.content.Context;

import com.xplor.android.storage.DataRepository;

public class LocalDataRepository implements DataRepository {
    private final Context appContext;

    public LocalDataRepository(Context appContext) {
        this.appContext = appContext;
    }
}
