package com.xplor.android.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.xplor.android.App;
import com.xplor.android.R;
import com.xplor.android.domain.UseCaseCallback;
import com.xplor.android.domain.UseCaseHandler;
import com.xplor.android.domain.UseCaseManager;
import com.xplor.android.models.RetryCallEvent;
import com.xplor.android.models.User;
import com.xplor.android.models.XplorError;
import com.xplor.android.utils.Constants;
import com.xplor.android.utils.Functions;
import com.xplor.android.utils.XplorConsumer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment<T> extends Fragment {
    private static final String TAG = Functions.getTag(BaseFragment.class);
    protected T fragmentActionListener;
    protected BaseActivity baseActivity;
    private String strProgress;
    private Unbinder unbinder;
    private App appInstance;
    private UseCaseHandler<?> useCaseHandler;

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            baseActivity = (BaseActivity) context;
        }
        appInstance = (App) context.getApplicationContext();

        //Set listener
        Class<T> listenerClass = null;
        try {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                listenerClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                listenerClass = (Class<T>) ((ParameterizedType) ((Class<?>) type)
                        .getGenericSuperclass()).getActualTypeArguments()[0];
            }
        } catch (Exception ignored) {
        }
        if (listenerClass != null) {
            if (!listenerClass.isAssignableFrom(context.getClass())) {
                throw new RuntimeException(context.toString()
                        + " must implement " + listenerClass.getSimpleName());
            } else {
                fragmentActionListener = listenerClass.cast(context);
            }
        }
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (useCaseHandler != null) {
            useCaseHandler.cancel();
        }
    }

    protected App getApp() {
        return appInstance;
    }

    protected UseCaseManager getUseCaseManager() {
        if (baseActivity != null) {
            return baseActivity.getUseCaseManager();
        }
        return null;
    }

    protected void registerExecuteUseCase(UseCaseHandler<?> useCaseHandler) {
        if (baseActivity != null) {
            baseActivity.hideSnackbar();
        }
        this.useCaseHandler = useCaseHandler;
        getUseCaseManager().execute(useCaseHandler);
    }

    protected <S, E> void registerExecuteUseCase(
            UseCaseHandler<S> useCaseHandler, XplorConsumer<S> successFunc, XplorConsumer<E> errorFunc) {
        useCaseHandler.setCallback(new CommonUseCaseCallback<>(successFunc, errorFunc));
        registerExecuteUseCase(useCaseHandler);
    }

    protected <S, E> void registerExecuteUseCaseWithActivityProgress(
            UseCaseHandler<S> useCaseHandler, XplorConsumer<S> successFunc, XplorConsumer<E> errorFunc,
            int progressStrId) {
        useCaseHandler.setCallback(new CommonUseCaseCallback<>(successFunc, errorFunc));
        registerExecuteUseCase(useCaseHandler);
        if (progressStrId != -1) {
            showActivityProgress(getString(progressStrId));
        }
    }

    protected <S, E> void registerExecuteUseCaseWithActivityProgress(
            UseCaseHandler<S> useCaseHandler, XplorConsumer<S> successFunc, XplorConsumer<E> errorFunc) {
        registerExecuteUseCaseWithActivityProgress(useCaseHandler, successFunc, errorFunc,
                R.string.please_wait);
    }

    protected void showActivityProgress(String progressText) {
        this.strProgress = progressText;
        if (baseActivity != null) {
            baseActivity.showActivityProgressWithText(progressText);
        }
    }

    protected void hideActivityProgress() {
        if (baseActivity != null) {
            baseActivity.hideActivityProgress();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void retry(RetryCallEvent call) {
        registerExecuteUseCase(useCaseHandler);
        if (!TextUtils.isEmpty(strProgress)) {
            showActivityProgress(strProgress);
        }
    }

    protected void bindView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    public class CommonUseCaseCallback<S, E> implements UseCaseCallback<S, XplorError<E>> {
        XplorConsumer<S> successFunc;
        XplorConsumer<E> errorFunc;

        public CommonUseCaseCallback(XplorConsumer<S> successFunc, XplorConsumer<E> errorFunc) {
            this.successFunc = successFunc;
            this.errorFunc = errorFunc;
        }

        @Override
        public void onSuccess(S successObj) {
            if (baseActivity != null) {
                baseActivity.hideActivityProgress();
            }
            successFunc.accept(successObj);
        }

        @Override
        public void onError(XplorError<E> error) {
            handleError(error);
            if (errorFunc != null) {
                E errorObj = error.getErrorObj();
                if (errorObj != null) {
                    errorFunc.accept(errorObj);
                }
            }
        }
    }

    protected void handleError(XplorError error) {
        if (baseActivity != null) {
            baseActivity.hideActivityProgress();
            int errorCode = error.getErrorCode();
            if (errorCode > 0) {
                int errorStrId = -1;
                switch (errorCode) {
                    case Constants.ERROR_UNEXPECTED:
                        errorStrId = R.string.generic_error;
                        break;
                    case Constants.ERROR_NO_NETWORK:
                        errorStrId = R.string.error_no_network;
                        break;
                    case Constants.ERROR_UNAUTHORIZED:
                        User.removeUserInstance();
                        break;
                }
                if (errorStrId != -1) {
                    baseActivity.showNetworkError(errorStrId, true);
                }
            }
        }
    }
}
