package com.inform.communitynoticeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> implements MaterialCardView.OnCheckedChangeListener {
    private final ArrayList<createPost> postList;
    private final Context context;
    //adapter takes an object of view holder class, we make our own view holder class
    //instead of using RecyclerView.ViewHolder
    //We want to define our own text view in the posts.

    public postAdapter(ArrayList<createPost> posts, Context context){
        this.postList=posts;
        this.context=context;
    }

    @Override
    public void onCheckedChanged(MaterialCardView card, boolean isChecked) {
        if(card.isClickable()){
            card.getCheckedIcon();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dispName, post, dateTime;
        MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.post_content);
            dispName =itemView.findViewById(R.id.user_name);
            dateTime=itemView.findViewById(R.id.dateTime_TV);
            cardView=itemView.findViewById(R.id.cardview);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);
        return new ViewHolder(postView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dispName.setText(postList.get(position).getUser());
        holder.post.setText(postList.get(position).getPost());
        holder.dateTime.setText(postList.get(position).getDateTime());

        //holder.cardView.setCheckedIcon();
        holder.cardView.setOnLongClickListener(view -> {
            if(!holder.cardView.isChecked()){
                holder.cardView.setChecked(true);
            }else{
                 holder.cardView.setChecked(false);
            }
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
