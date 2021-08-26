package com.inform.communitynoticeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> {
    private final ArrayList<createPost> postList;
    private final Context context;
    //adapter takes an object of view holder class, we make our own view holder class
    //instead of using RecyclerView.ViewHolder
    //We want to define our own text view in the posts.

    public postAdapter(ArrayList<createPost> posts, Context context){
        this.postList=posts;
        this.context=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName, post;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.post_content);
            userName=itemView.findViewById(R.id.user_name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userName.setText(postList.get(position).getUser().toString());
        holder.post.setText(postList.get(position).getPost());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
