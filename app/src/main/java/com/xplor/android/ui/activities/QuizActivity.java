package com.xplor.android.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.xplor.android.ui.DrawerBaseActivity;
import com.xplor.android.ui.fragments.QuizFragment;
import com.xplor.android.ui.fragments.QuizItemPagerFragment;

public class QuizActivity extends DrawerBaseActivity implements
        QuizFragment.QuizFragmentActionListener,
        QuizItemPagerFragment.QuizItemActionListener {
    private static final String KEY_QUIZ_ID = "quizId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = getContentFragment();
        if (fragment == null) {
            setContentFragment(QuizFragment.newInstance(getIntent().getStringExtra(KEY_QUIZ_ID)));
        }
    }

    public static void startActivity(Context startingContext, String quizId) {
        Intent intent = new Intent(startingContext, QuizActivity.class);
        intent.putExtra(KEY_QUIZ_ID, quizId);
        startingContext.startActivity(intent);
    }

    @Override
    public void onQuizSubmitSuccess(int points, int correct, int incorrect, int skipped) {
        QuizCompleteActivity.startActivity(this, points, correct, incorrect, skipped);
        finish();
    }
}
