package com.inform.communitynoticeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class CommunityManagerAdapter extends RecyclerView.Adapter<CommunityManagerAdapter.ViewHolder> {

    private final ArrayList<String> communitiesList;
    private final Context context;

    public CommunityManagerAdapter(ArrayList<String> communitiesList, Context context) {
        this.communitiesList = communitiesList;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView communityTV;
        MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            communityTV = itemView.findViewById(R.id.community_TV);
            cardView = itemView.findViewById(R.id.community_manager_cardView);
        }
    }


    @NonNull
    @Override
    public CommunityManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View requestView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_manager_card, parent, false);
        return new CommunityManagerAdapter.ViewHolder(requestView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityManagerAdapter.ViewHolder holder, int position) {
        holder.communityTV.setText(communitiesList.get(position));
    }

    @Override
    public int getItemCount() {
        return communitiesList.size();
    }
}
