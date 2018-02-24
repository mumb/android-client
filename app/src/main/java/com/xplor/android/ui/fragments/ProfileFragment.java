package com.xplor.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xplor.android.R;
import com.xplor.android.models.User;
import com.xplor.android.ui.BaseFragment;
import com.xplor.android.utils.Functions;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment<ProfileFragment.ProfileActionListener> {
    @BindView(R.id.imgProfile) ImageView imgProfile;
    @BindView(R.id.txtUserName) TextView txtUserName;
    @BindView(R.id.txtPoints) TextView txtPoints;
    @BindView(R.id.viewNumQuizzes) View viewNumQuizzes;
    @BindView(R.id.viewRank) View viewRank;
    @BindView(R.id.viewAccuracy) View viewAccuracy;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        bindView(view);
        User user = User.getInstance();
        txtUserName.setText(user.getName());
        Functions.loadImageIntoCircularImageView(getContext(), Functions.getPhotoUrl(user.getFacebookId()), imgProfile);
        txtPoints.setText(getString(R.string.points, String.valueOf(user.getPoints())));
        setAttemptsView(viewNumQuizzes, String.valueOf(user.getQuizCount()), getString(R.string.quizzes_lower));
        setAttemptsView(viewRank, user.getRank() == null ? getString(R.string.rank_dash) :
                String.valueOf(user.getRank()), getString(R.string.rank));
        setAttemptsView(viewAccuracy, (int) user.getAccuracy() + "%", getString(R.string.accuracy));
        return view;
    }

    private void setAttemptsView(View parentView, String num, String type) {
        TextView txtNum = parentView.findViewById(R.id.txtNum);
        TextView txtType = parentView.findViewById(R.id.txtType);
        txtNum.setText(num);
        txtType.setText(type);
    }

    @OnClick(R.id.btnLogOut)
    void onClick(View view) {
        if (fragmentActionListener != null) {
            fragmentActionListener.onProfileAction();
        }
    }

    public interface ProfileActionListener {
        void onProfileAction();
    }
}
