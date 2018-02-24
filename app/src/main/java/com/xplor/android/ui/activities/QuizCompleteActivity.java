package com.xplor.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xplor.android.ui.BaseActivity;
import com.xplor.android.ui.fragments.QuizCompleteFragment;

public class QuizCompleteActivity extends BaseActivity implements QuizCompleteFragment.QuizCompleteActionListener {
    private static final String KEY_POINTS = "points";
    private static final String KEY_CORRECT = "correct";
    private static final String KEY_INCORRECT = "incorrect";
    private static final String KEY_SKIPPED = "skipped";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        Fragment fragment = getContentFragment();
        if (fragment == null) {
            setContentFragment(QuizCompleteFragment.newInstance(getIntent().getExtras()));
        }
    }

    public static void startActivity(Context startingContext, int points, int correct, int incorrect, int skipped) {
        Intent intent = new Intent(startingContext, QuizCompleteActivity.class);
        Bundle arg = new Bundle();
        arg.putInt(KEY_POINTS, points);
        arg.putInt(KEY_CORRECT, correct);
        arg.putInt(KEY_INCORRECT, incorrect);
        arg.putInt(KEY_SKIPPED, skipped);
        intent.putExtras(arg);
        startingContext.startActivity(intent);
    }

    @Override
    public void onQuizComplete() {
        finish();
    }
}
