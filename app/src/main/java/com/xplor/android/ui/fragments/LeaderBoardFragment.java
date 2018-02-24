package com.xplor.android.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.xplor.android.R;
import com.xplor.android.models.Leader;
import com.xplor.android.ui.BaseFragment;
import com.xplor.android.ui.adapters.LeaderBoardAdapter;
import com.xplor.android.utils.ApiSuccess;

import java.util.List;

import butterknife.BindView;

public class LeaderBoardFragment extends BaseFragment<LeaderBoardFragment.LeaderBoardActionListener> {
    @BindView(R.id.rvLeaders) RecyclerView rvLeaders;
    @BindView(R.id.containerImgLeaderBoard) View containerImgLeaderBoard;

    private LeaderBoardAdapter adapter;

    public static LeaderBoardFragment newInstance() {
        return new LeaderBoardFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        bindView(view);
        rvLeaders.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeaderBoardAdapter();
        rvLeaders.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        registerExecuteUseCaseWithActivityProgress(
                getUseCaseManager().getLeaderBoard(),
                this::onGetLeaderBoard,
                null
        );
    }

    @ApiSuccess
    private void onGetLeaderBoard(List<Leader> leaders) {
        containerImgLeaderBoard.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_animation);
        containerImgLeaderBoard.startAnimation(animation);
        adapter.setLeaders(leaders);
        rvLeaders.scheduleLayoutAnimation();
    }

    public interface LeaderBoardActionListener {

    }
}
