package com.xplor.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.xplor.android.R;
import com.xplor.android.models.Answer;
import com.xplor.android.models.AnswerRequest;
import com.xplor.android.models.Question;
import com.xplor.android.models.QuizResponse;
import com.xplor.android.ui.BaseFragment;
import com.xplor.android.ui.adapters.QuizAdapter;
import com.xplor.android.utils.ApiSuccess;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class QuizFragment extends BaseFragment<QuizFragment.QuizFragmentActionListener> {
    private static final String KEY_QUIZ_ID = "quizId";
    @BindView(R.id.viewPagerQuiz) ViewPager viewPagerQuiz;
    @BindView(R.id.txtQuizName) TextView txtQuizName;
    @BindView(R.id.donutProgress) DonutProgress donutProgress;

    private QuizResponse quizResponse;
    private QuizAdapter quizAdapter;
    private List<Question> questions;

    private int correct;
    private int incorrect;
    private int skipped;

    public static QuizFragment newInstance(String quizId) {
        Bundle args = new Bundle();
        args.putString(KEY_QUIZ_ID, quizId);
        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        bindView(view);
        donutProgress.setText("0");
        setupViewPager();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        String quizId = args != null ? args.getString(KEY_QUIZ_ID) : null;
        if (quizResponse == null && !TextUtils.isEmpty(quizId)) {
            registerExecuteUseCaseWithActivityProgress(getUseCaseManager().getQuiz(quizId),
                    this::onGetQuiz, null);
        }
    }

    @ApiSuccess
    private void onGetQuiz(QuizResponse quizResponse) {
        this.quizResponse = quizResponse;
        txtQuizName.setText(quizResponse.getQuizCategory());
        if (quizAdapter == null) {
            quizAdapter = new QuizAdapter(getChildFragmentManager());
            viewPagerQuiz.setAdapter(quizAdapter);
        }
        questions = quizResponse.getQuestions();
        quizAdapter.setQuestions(questions);
        setDonutProgress();
    }

    private void setupViewPager() {
        viewPagerQuiz.setOffscreenPageLimit(1);
        viewPagerQuiz.setClipToPadding(false);
        int viewPagerPadding = getResources().getDimensionPixelSize(R.dimen.view_big_vertical_margin);
        viewPagerQuiz.setPadding(viewPagerPadding, 0, viewPagerPadding, 0);
        int pageMargin = getResources().getDimensionPixelSize(R.dimen.view_default_vertical_margin);
        viewPagerQuiz.setPageMargin(pageMargin);
    }

    private void setDonutProgress() {
        List<Question> questions = quizResponse.getQuestions();
        int percentageAttempt = 0;
        if (questions != null && !questions.isEmpty()) {
            float attempts = 0;
            for (Question question : questions) {
                if (question.isAnswered()) {
                    attempts++;
                }
            }
            percentageAttempt = (int) ((attempts / questions.size()) * 100);
        }
        donutProgress.setProgress(percentageAttempt);
        donutProgress.setText(String.valueOf(percentageAttempt));
    }

    public void submitQuizSilently(Question question) {
        int currentPosition = viewPagerQuiz.getCurrentItem();
        questions.set(currentPosition, question);
        submitQuiz(true);
        setDonutProgress();
    }

    public void onQuestionContinueClick() {
        int currentPosition = viewPagerQuiz.getCurrentItem();
        if (currentPosition < questions.size() - 1) {
            viewPagerQuiz.setCurrentItem(currentPosition + 1, true);
        } else {
            correct = 0;
            incorrect = 0;
            skipped = 0;
            for (Question question : questions) {
                if (!question.isAnswered()) {
                    skipped++;
                } else if (question.isUserAnswerCorrect()) {
                    correct++;
                } else {
                    incorrect++;
                }
            }
            QuizSummaryFragment fragment = QuizSummaryFragment.newInstance(
                    quizResponse.getMuseumName(),
                    quizResponse.getQuizCategory(),
                    correct,
                    incorrect,
                    skipped
            );
            fragment.setFinalSubmitClickListener(() -> submitQuiz(false));
            fragment.show(getChildFragmentManager(), "");
        }
    }

    private void submitQuiz(boolean isSilent) {
        if (isSilent) {
            registerExecuteUseCase(getUseCaseManager().submitQuiz(quizResponse.getQuizId(), getAnswerRequest()));
        } else {
            registerExecuteUseCaseWithActivityProgress(
                    getUseCaseManager().submitQuizWithProfileRefresh(quizResponse.getQuizId(), getAnswerRequest()),
                    this::onQuizSubmit,
                    null
            );
        }
    }

    private AnswerRequest getAnswerRequest() {
        List<Answer> answers = new ArrayList<>();
        for (Question question : questions) {
            if (question.isAnswered()) {
                answers.add(new Answer(question.getQuestionId(), question.getUserAnswer()));
            }
        }
        return new AnswerRequest(answers);
    }

    @ApiSuccess
    private void onQuizSubmit(QuizResponse quizResponse) {
        if (fragmentActionListener != null) {
            fragmentActionListener.onQuizSubmitSuccess(quizResponse.getPoints(), correct, incorrect, skipped);
        }
    }

    public interface QuizFragmentActionListener {
        void onQuizSubmitSuccess(int points, int correct, int incorrect, int skipped);
    }
}
