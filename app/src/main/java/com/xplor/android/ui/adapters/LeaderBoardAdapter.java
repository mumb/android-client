package com.xplor.android.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xplor.android.R;
import com.xplor.android.models.Leader;
import com.xplor.android.utils.Functions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Leader> leaders;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.row_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Leader leader = leaders.get(position);
        holder.txtName.setText(leader.getName());
        holder.txtPoints.setText(String.valueOf(leader.getPoints()));
        Functions.loadImageIntoCircularImageView(holder.imgProfile.getContext(),
                leader.getImageUrl(), holder.imgProfile);
    }

    @Override
    public int getItemCount() {
        return leaders != null ? leaders.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView imgProfile;
        private TextView txtName;
        private TextView txtPoints;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtName = itemView.findViewById(R.id.txtName);
            txtPoints = itemView.findViewById(R.id.txtPoints);
        }
    }

    public void setLeaders(List<Leader> leaders) {
        this.leaders = leaders;
        notifyDataSetChanged();
    }
}
