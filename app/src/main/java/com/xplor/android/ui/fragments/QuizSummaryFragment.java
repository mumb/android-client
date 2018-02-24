package com.xplor.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xplor.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizSummaryFragment extends BottomSheetDialogFragment {
    private static final String KEY_MUSEUM_NAME = "name";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_CORRECT = "correct";
    private static final String KEY_WRONG = "wrong";
    private static final String KEY_SKIPPED = "skipped";

    @BindView(R.id.txtMuseumName) TextView txtMuseumName;
    @BindView(R.id.txtCategoryName) TextView txtCategoryName;
    @BindView(R.id.viewCorrect) View viewCorrect;
    @BindView(R.id.viewWrong) View viewWrong;
    @BindView(R.id.viewSkipped) View viewSkipped;

    private FinalSubmitClickListener finalSubmitClickListener;

    public static QuizSummaryFragment newInstance(String museumName, String categoryName,
                                                  int correct, int wrong, int skipped) {
        Bundle args = new Bundle();
        args.putString(KEY_MUSEUM_NAME, museumName);
        args.putString(KEY_CATEGORY, categoryName);
        args.putInt(KEY_CORRECT, correct);
        args.putInt(KEY_WRONG, wrong);
        args.putInt(KEY_SKIPPED, skipped);
        QuizSummaryFragment fragment = new QuizSummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_summary, container, false);
        ButterKnife.bind(this, view);
        Bundle args = getArguments();
        if (args != null) {
            txtMuseumName.setText(args.getString(KEY_MUSEUM_NAME));
            txtCategoryName.setText(args.getString(KEY_CATEGORY));
            setAttemptsView(viewCorrect, args.getInt(KEY_CORRECT), R.string.correct);
            setAttemptsView(viewWrong, args.getInt(KEY_WRONG), R.string.wrong);
            setAttemptsView(viewSkipped, args.getInt(KEY_SKIPPED), R.string.skipped);
        }
        return view;
    }

    private void setAttemptsView(View parentView, int attempts, @StringRes int attemptType) {
        TextView txtNumAttempt = parentView.findViewById(R.id.txtNumAttempt);
        TextView txtAttemptType = parentView.findViewById(R.id.txtAttemptType);
        txtNumAttempt.setText(String.valueOf(attempts));
        txtAttemptType.setText(attemptType);
    }

    @OnClick(R.id.btnSubmit)
    void onClick(View view) {
        if (finalSubmitClickListener != null) {
            finalSubmitClickListener.onFinalSubmit();
        }
    }

    public void setFinalSubmitClickListener(FinalSubmitClickListener finalSubmitClickListener) {
        this.finalSubmitClickListener = finalSubmitClickListener;
    }

    public interface FinalSubmitClickListener {
        void onFinalSubmit();
    }
}
