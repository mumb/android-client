package com.xplor.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xplor.android.R;
import com.xplor.android.ui.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class QuizCompleteFragment extends BaseFragment<QuizCompleteFragment.QuizCompleteActionListener> {
    private static final String KEY_POINTS = "points";
    private static final String KEY_CORRECT = "correct";
    private static final String KEY_INCORRECT = "incorrect";
    private static final String KEY_SKIPPED = "skipped";

    @BindView(R.id.txtPoints) TextView txtPoints;
    @BindView(R.id.viewCorrect) View viewCorrect;
    @BindView(R.id.viewWrong) View viewWrong;
    @BindView(R.id.viewSkipped) View viewSkipped;

    public static QuizCompleteFragment newInstance(Bundle args) {
        QuizCompleteFragment fragment = new QuizCompleteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_complete, container, false);
        bindView(view);
        Bundle args = getArguments();
        if (args != null) {
            txtPoints.setText(getString(R.string.points, String.valueOf(args.getInt(KEY_POINTS))));
            setAttemptsView(viewCorrect, args.getInt(KEY_CORRECT), R.string.correct);
            setAttemptsView(viewWrong, args.getInt(KEY_INCORRECT), R.string.wrong);
            setAttemptsView(viewSkipped, args.getInt(KEY_SKIPPED), R.string.skipped);
        }
        return view;
    }

    private void setAttemptsView(View parentView, int attempts, @StringRes int attemptType) {
        TextView txtNumAttempt = parentView.findViewById(R.id.txtNum);
        TextView txtAttemptType = parentView.findViewById(R.id.txtType);
        txtNumAttempt.setText(String.valueOf(attempts));
        txtAttemptType.setText(attemptType);
    }

    @OnClick(R.id.btnXplorMore)
    void onClick(View view) {
        if (fragmentActionListener != null) {
            fragmentActionListener.onQuizComplete();
        }
    }

    public interface QuizCompleteActionListener {
        void onQuizComplete();
    }
}
