package com.inform.communitynoticeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> implements MaterialCardView.OnCheckedChangeListener {
    private final ArrayList<createPost> postList;
    private final Context context;
    private final dataBaseFirebase firebase=dataBaseFirebase.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
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

    public Context getContext() {
        return context;
    }

    public dataBaseFirebase getFirebase() {
        return firebase;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dispName, post, dateTime;
        ImageView postPicIV;
        MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.post_content);
            dispName =itemView.findViewById(R.id.display_name);
            dateTime=itemView.findViewById(R.id.dateTime_TV);
            postPicIV=itemView.findViewById(R.id.postPic_IV);
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
        holder.dateTime.setText(postList.get(position).getDateTime());

        if (!postList.get(position).getPost().equals("")) {
            holder.post.setText(postList.get(position).getPost());
        } else {
            holder.post.getLayoutParams().height = 0;
        }

        //show post picture
        if (!postList.get(position).getImageUri().equals("")) {
            StorageReference photoRef = storage.getReferenceFromUrl(postList.get(position).getImageUri());

            final long ONE_MEGABYTE = 1024 * 1024;
            photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.postPicIV.setImageBitmap(bmp);
            }).addOnFailureListener(e -> {
                //handle failure
            });
        } else {
            holder.postPicIV.getLayoutParams().height = 0;
            holder.postPicIV.requestLayout();
        }

        //holder.cardView.setCheckedIcon();
        holder.cardView.setOnLongClickListener(view -> {
            holder.cardView.setChecked(!holder.cardView.isChecked());
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

}
