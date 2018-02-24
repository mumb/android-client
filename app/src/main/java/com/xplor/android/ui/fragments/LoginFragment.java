package com.xplor.android.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.xplor.android.R;
import com.xplor.android.models.GenericResponse;
import com.xplor.android.models.LoginRequest;
import com.xplor.android.ui.BaseFragment;

import org.json.JSONException;

import java.util.Arrays;

import butterknife.OnClick;

public class LoginFragment extends BaseFragment<LoginFragment.LoginActionListener> {
    private static final String PERMISSION_FACEBOOK = "public_profile";
    private CallbackManager callbackManager;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        bindView(view);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        hideActivityProgress();
                        getUserDetails(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        hideActivityProgress();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        hideActivityProgress();
                        exception.printStackTrace();
                        showLoginError();
                    }
                });
        return view;
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), (json_object, response) -> {
                    hideActivityProgress();
                    if (response.getError() == null) {
                        try {
                            LoginRequest loginRequest = new LoginRequest(
                                    json_object.getString("id"),
                                    json_object.getString("name"),
                                    json_object.getString("gender"),
                                    AccessToken.getCurrentAccessToken().getToken()
                            );
                            registerExecuteUseCaseWithActivityProgress(getUseCaseManager().login(loginRequest),
                                    this::onLoginSuccess, null);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            showLoginError();
                        }
                    } else {
                        showLoginError();
                    }
                });
        Bundle permissionBundle = new Bundle();
        permissionBundle.putString("fields", "id, name, gender");
        data_request.setParameters(permissionBundle);
        showActivityProgress(getString(R.string.please_wait));
        data_request.executeAsync();
    }

    private void onLoginSuccess(GenericResponse genericResponse) {
        if (fragmentActionListener != null) {
            fragmentActionListener.onLoginSuccessful();
        }
    }

    @OnClick({
            R.id.btnLoginFb,
            R.id.btnContinueWithoutLogin
    })
    void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnContinueWithoutLogin:
                if (fragmentActionListener != null) {
                    fragmentActionListener.onContinueWithoutLogin();
                }
                break;
            case R.id.btnLoginFb:
                showActivityProgress(getString(R.string.please_wait));
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(PERMISSION_FACEBOOK));
        }

    }

    private void showLoginError() {
        Toast.makeText(getContext(), R.string.failed_login, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface LoginActionListener {
        void onLoginSuccessful();

        void onContinueWithoutLogin();
    }
}
