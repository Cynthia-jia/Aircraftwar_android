package com.hit.aircraft_war.Store;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.aircraft_war.R;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder>{

    private List<RankMember> dataList;

    public RankAdapter(List<RankMember> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rank.setText(String.valueOf(dataList.get(position).getRank()));
        holder.name.setText(dataList.get(position).getName());
        holder.score.setText(String.valueOf(dataList.get(position).getScore()));
        holder.time.setText(dataList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView rank;
        TextView name;
        TextView score;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.rank = itemView.findViewById(R.id.tv_rank);
            this.name = itemView.findViewById(R.id.tv_player);
            this.score = itemView.findViewById(R.id.tv_score);
            this.time = itemView.findViewById(R.id.tv_time);
        }
    }
}
