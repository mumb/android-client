package com.xplor.android;

import android.app.Application;
import android.support.annotation.NonNull;

import com.xplor.android.domain.UseCaseManager;
import com.xplor.android.domain.UseCaseThreadPoolScheduler;
import com.xplor.android.storage.AppPreferences;
import com.xplor.android.storage.RepositoriesManager;
import com.xplor.android.storage.remote.RemoteRepositoryConfig;

public class App extends Application {
    private AppPreferences mPreferences;
    private UseCaseManager useCaseManager;
    private RepositoriesManager repositoriesManager;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    protected void init() {
        mPreferences = new AppPreferences(this);
    }

    public UseCaseManager getUseCaseManager() {
        if (useCaseManager == null) {
            useCaseManager = new UseCaseManager(new UseCaseThreadPoolScheduler(),
                    getRepositoriesManager());

        }
        return useCaseManager;
    }

    public RepositoriesManager getRepositoriesManager() {
        if (repositoriesManager == null) {
            repositoriesManager = new RepositoriesManager(this,
                    new RemoteRepositoryConfig(this));
        }
        return repositoriesManager;
    }

    @NonNull
    public AppPreferences getPreferences() {
        return mPreferences;
    }
}
