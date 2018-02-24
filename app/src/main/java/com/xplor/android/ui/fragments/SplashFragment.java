package com.xplor.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xplor.android.R;
import com.xplor.android.models.User;
import com.xplor.android.models.XplorError;
import com.xplor.android.ui.BaseFragment;
import com.xplor.android.utils.Constants;

public class SplashFragment extends BaseFragment<SplashFragment.SplashFragmentActionListener> {

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        bindView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        registerExecuteUseCase(getUseCaseManager().getUser(), this::onUserExist, null);
    }

    public void onUserExist(User user) {
        fragmentActionListener.onSessionVerificationComplete();
    }

    @Override
    public void handleError(XplorError error) {
        if (error.getErrorCode() == Constants.ERROR_UNAUTHORIZED) {
            fragmentActionListener.onSessionVerificationComplete();
        }
    }

    public interface SplashFragmentActionListener {
        void onSessionVerificationComplete();
    }
}
