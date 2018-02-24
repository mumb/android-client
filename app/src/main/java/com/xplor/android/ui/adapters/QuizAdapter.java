package com.xplor.android.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.xplor.android.models.Question;
import com.xplor.android.ui.fragments.QuizItemPagerFragment;

import java.util.List;

public class QuizAdapter extends FragmentStatePagerAdapter {
    private List<Question> questions;

    public QuizAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        QuizItemPagerFragment fragment = QuizItemPagerFragment.newInstance(questions.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return questions != null ? questions.size() : 0;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // refresh all fragments when data set changed
        return PagerAdapter.POSITION_NONE;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }
}
