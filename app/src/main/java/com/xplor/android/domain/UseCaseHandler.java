package com.xplor.android.domain;

import android.os.Handler;
import android.os.Looper;

import com.xplor.android.models.XplorError;
import com.xplor.android.storage.RepositoryHandler;
import com.xplor.android.storage.XplorDataException;
import com.xplor.android.utils.Constants;

public abstract class UseCaseHandler<R> implements Runnable {
    private boolean isCancelled;
    private boolean isActive;
    private Handler handler = new Handler(Looper.getMainLooper());
    private UseCaseCallback<R, XplorError> useCaseCallback;
    private RepositoryHandler repositoryHandler;

    @Override
    public void run() {
        try {
            R output = runUseCase();
            if (output != null) {
                publishResult(output);
            } else {
                publishError();
            }
        } catch (XplorDataException e) {
            publishError(e.getErrorObject(), e.getErrorCode());
        }
    }

    public abstract R runUseCase() throws XplorDataException;

    public <T extends XplorError> void setCallback(UseCaseCallback<R, T> resultCallback) {
        this.useCaseCallback = (UseCaseCallback<R, XplorError>) resultCallback;
    }

    public void registerRepositoryHandler(RepositoryHandler handler) {
        repositoryHandler = handler;
    }

    public void cancel() {
        isCancelled = true;
        if (repositoryHandler != null) {
            repositoryHandler.cancel();
        }
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setActiveState(boolean activeState) {
        isActive = activeState;
    }

    public boolean isActive() {
        return isActive;
    }

    protected void publishResult(R result) {
        postToMainThread(result);
        setActiveState(false);
    }

    //TODO : Need to work on error handling
    protected void publishError() {
        publishError(null, Constants.ERROR_UNKNOWN);
    }

    protected void publishError(Object errorRequest, int errorCode) {
        if (!isCancelled()) {
            XplorError error = null;
            if (errorRequest != null) {
                if (errorRequest instanceof XplorError) {
                    error = (XplorError) errorRequest;
                    ((XplorError<Object>) errorRequest).setErrorCode(errorCode);
                }
            }
            if (!(errorRequest instanceof XplorError)) {
                error = new XplorError(null, null, errorCode);
                error.setErrorObj(errorRequest);
            }
            postErrorToMainThread(error);
        }
        setActiveState(false);
    }

    private void postToMainThread(R result) {
        if (!isCancelled()) {
            handler.post(() -> {
                if (useCaseCallback != null) {
                    useCaseCallback.onSuccess(result);
                }
            });
        }
    }

    private void postErrorToMainThread(XplorError error) {
        if (!isCancelled()) {
            handler.post(() -> {
                if (useCaseCallback != null) {
                    useCaseCallback.onError(error);
                }
            });
        }
    }
}
