package com.xplor.android.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xplor.android.R;
import com.xplor.android.models.Question;
import com.xplor.android.ui.BaseFragment;
import com.xplor.android.ui.widgets.XplorImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class QuizItemPagerFragment extends BaseFragment<QuizItemPagerFragment.QuizItemActionListener> {
    private static final String KEY_QUESTION = "question";

    @BindView(R.id.containerOptionSelection) ViewGroup containerOptionSelection;
    @BindView(R.id.viewQuizQuestion) ViewGroup viewQuizQuestion;
    @BindView(R.id.viewQuizAnswer) View viewQuizAnswer;
    @BindView(R.id.viewShowHint) View viewShowHint;
    @BindView(R.id.txtHint) TextView txtHint;
    @BindView(R.id.txtQuestion) TextView txtQuestion;
    @BindView(R.id.imgShowHint) ImageView imgShowHint;
    @BindView(R.id.imgQuestion) XplorImageView imgQuestion;
    @BindView(R.id.imgAnswerResult) ImageView imgAnswerResult;
    @BindView(R.id.txtOptionA) TextView txtOptionA;
    @BindView(R.id.txtOptionB) TextView txtOptionB;
    @BindView(R.id.txtOptionC) TextView txtOptionC;
    @BindView(R.id.txtOptionD) TextView txtOptionD;
    @BindView(R.id.txtA) TextView txtA;
    @BindView(R.id.txtB) TextView txtB;
    @BindView(R.id.txtC) TextView txtC;
    @BindView(R.id.txtD) TextView txtD;
    @BindView(R.id.txtTrivia) TextView txtTrivia;
    @BindView(R.id.txtTriviaTitle) TextView txtTriviaTitle;
    @BindView(R.id.txtAnswerResult) TextView txtAnswerResult;
    @BindView(R.id.btnSubmitAnswer) Button btnSubmitAnswer;

    private AnimatorSet setRightOut;
    private AnimatorSet setLeftIn;
    private Question question;
    private String selectedAnswer;
    private boolean isAnswerViewViewVisible;

    public static QuizItemPagerFragment newInstance(Question question) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_QUESTION, question);
        QuizItemPagerFragment fragment = new QuizItemPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_item, container, false);
        bindView(view);
        setupAnimation();
        Bundle args = getArguments();
        Question question = args != null ? args.getParcelable(KEY_QUESTION) : null;
        if (question != null) {
            setupQuiz(question);
        }
        return view;
    }

    private void setupQuiz(Question question) {
        this.question = question;
        txtQuestion.setText(question.getQuestionText());
        txtHint.setText(question.getHint());
        imgQuestion.loadFromApi(question.getQuestionImageUrl(), R.drawable.question);
        txtTrivia.setText(question.getTrivia());

        List<String> options = question.getOptions();
        String optionA = options.get(0);
        txtOptionA.setText(getString(R.string.option_a, optionA));
        txtA.setTag(optionA);

        String optionB = options.get(1);
        txtOptionB.setText(getString(R.string.option_b, optionB));
        txtB.setTag(optionB);

        String optionC = options.get(2);
        txtOptionC.setText(getString(R.string.option_c, optionC));
        txtC.setTag(optionC);

        String optionD = options.get(3);
        txtOptionD.setText(getString(R.string.option_d, optionD));
        txtD.setTag(optionD);

        if (question.isAnswered()) {
            for (int i = 0; i < containerOptionSelection.getChildCount(); i++) {
                View child = containerOptionSelection.getChildAt(i);
                String option = (String) child.getTag();
                if (option.equals(question.getUserAnswer())) {
                    setOptionSelected(child);
                }
            }
        }

        setAnswerLayout(question);
        setSubmitButtonText(question);
    }

    private void setSubmitButtonText(Question question) {
        if (question.isAnswered()) {
            btnSubmitAnswer.setText(R.string.view_trivia);
        } else {
            btnSubmitAnswer.setText(R.string.submit_answer);
        }
    }

    private void setAnswerLayout(Question question) {
        if (!question.isAnswered()) {
            return;
        }
        if (question.isUserAnswerCorrect()) {
            txtAnswerResult.setText(R.string.you_were_right);
            txtTriviaTitle.setText(R.string.did_you_know);
            imgAnswerResult.setImageResource(R.drawable.right);
        } else {
            txtAnswerResult.setText(R.string.you_were_wrong);
            txtTriviaTitle.setText(getString(R.string.it_was, question.getAnswer()));
            imgAnswerResult.setImageResource(R.drawable.wrong);
        }
    }

    @OnClick({
            R.id.btnSubmitAnswer,
            R.id.btnContinue,
            R.id.btnViewQuestion,
            R.id.viewShowHint,
            R.id.txtA,
            R.id.txtB,
            R.id.txtC,
            R.id.txtD
    })
    void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnSubmitAnswer:
                if (!question.isAnswered() && !TextUtils.isEmpty(selectedAnswer)) {
                    question.setAnswered(true);
                    question.setUserAnswerCorrect(selectedAnswer.equals(question.getAnswer()));
                    question.setUserAnswer(selectedAnswer);
                    setSubmitButtonText(question);
                    setAnswerLayout(question);
                    startFlipAnimationToTrivia();
                    submitQuizToParentFragment();
                } else if (question.isAnswered()) {
                    startFlipAnimationToTrivia();
                }
                break;
            case R.id.btnContinue:
                gotoNextQuestion();
                break;
            case R.id.btnViewQuestion:
                startFlipAnimationToQuestion();
                break;
            case R.id.viewShowHint:
                txtHint.setVisibility(View.VISIBLE);
                viewShowHint.setSelected(true);
                imgShowHint.setSelected(true);
                break;
            case R.id.txtA:
            case R.id.txtB:
            case R.id.txtC:
            case R.id.txtD:
                if (!question.isAnswered()) {
                    setOptionSelected(view);
                } else {
                    Toast.makeText(getContext(), R.string.question_already_attempted, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void submitQuizToParentFragment() {
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof QuizFragment) {
            ((QuizFragment) fragment).submitQuizSilently(question);
        }
    }

    private void gotoNextQuestion() {
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof QuizFragment) {
            ((QuizFragment) fragment).onQuestionContinueClick();
        }
    }

    private void setOptionSelected(View optionView) {
        selectedAnswer = (String) optionView.getTag();
        optionView.setSelected(true);
        for (int i = 0; i < containerOptionSelection.getChildCount(); i++) {
            View child = containerOptionSelection.getChildAt(i);
            if (child.getId() != optionView.getId()) {
                child.setSelected(false);
            }
        }
    }

    private void startFlipAnimationToTrivia() {
        isAnswerViewViewVisible = true;
        setRightOut.setTarget(viewQuizQuestion);
        setLeftIn.setTarget(viewQuizAnswer);
        setRightOut.start();
        setLeftIn.start();
    }

    private void startFlipAnimationToQuestion() {
        isAnswerViewViewVisible = false;
        setRightOut.setTarget(viewQuizAnswer);
        setLeftIn.setTarget(viewQuizQuestion);
        setRightOut.start();
        setLeftIn.start();
    }

    private void setupAnimation() {
        setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.out_animation);
        setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.in_animation);

        setLeftIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (isAnswerViewViewVisible) {
                    viewQuizAnswer.setVisibility(View.VISIBLE);
                } else {
                    viewQuizQuestion.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isAnswerViewViewVisible) {
                    viewQuizQuestion.setVisibility(View.GONE);
                } else {
                    viewQuizAnswer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        viewQuizQuestion.setCameraDistance(scale);
        viewQuizAnswer.setCameraDistance(scale);

        LayoutTransition layoutTransition = new LayoutTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }
        viewQuizQuestion.setLayoutTransition(layoutTransition);
    }

    public interface QuizItemActionListener {
    }
}
