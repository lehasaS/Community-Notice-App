package com.inform.communitynoticeapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class noticeBoardAdapter extends RecyclerView.Adapter<noticeBoardAdapter.ViewHolder>  {
    private final ArrayList<createPost> postList;
    private final Context context;
    private final dataBaseFirebase firebase=dataBaseFirebase.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    //adapter takes an object of view holder class, we make our own view holder class
    //instead of using RecyclerView.ViewHolder
    //We want to define our own text view in the posts.

    public noticeBoardAdapter(ArrayList<createPost> posts, Context context){
        this.postList=posts;
        this.context=context;

    }

    public dataBaseFirebase getFirebase() {
        return firebase;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dispName, post, dateTime;
        ImageView postPicIV;
        MaterialCardView cardView;
        ToggleButton bookmark;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.post_contentTwo);
            dispName =itemView.findViewById(R.id.display_nameTwo);
            dateTime=itemView.findViewById(R.id.dateTime_TVTwo);
            postPicIV=itemView.findViewById(R.id.postPic_IVTwo);
            cardView=itemView.findViewById(R.id.cardviewTwo);
            bookmark=itemView.findViewById(R.id.bookmark_BtnTwo);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_card, parent, false);
        return new ViewHolder(postView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.dispName.setText(postList.get(position).getUser());
        holder.dateTime.setText(postList.get(position).getDateTime());

        SharedPreferences preferences = getSharedPreferences();
        String state = preferences.getString(position +"pressed", "no");

        //Used to make sure the button is still bookmarked when you leave and come back to the screen
        if(state.equals("yes")){
            holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.clicked_bookmark));
        }else{
            holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.ic_baseline_bookmark));
        }

        //Change bookmark colour when clicking bookmark button
        holder.bookmark.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            SharedPreferences.Editor editor = getSharedPreferences().edit();
            if (isChecked && state.equals("no")) {
                editor.putString(position + "pressed", "yes");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.clicked_bookmark));
                //addPostToBookmarks(postList.get(position));
            }
            else  if (isChecked && state.equals("yes")) {
                editor.putString(position + "pressed", "no");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.ic_baseline_bookmark));
                //TODO: Make removing work
                //removePostBookmark(postList.get(position));
            } else  if (!isChecked && state.equals("no")) {
                editor.putString(position + "pressed", "no");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.ic_baseline_bookmark));
                //TODO: Make removing work
                //removePostBookmark(postList.get(position));
            } else  if (!isChecked && state.equals("yes")) {
                editor.putString(position + "pressed", "yes");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.clicked_bookmark));
               // addPostToBookmarks(postList.get(position));
            }

        });

        preferences.getString(position + "pressed", "no");

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


    }

   /* private void removePostBookmark(createPost model) {
        firebase.removeBookmark(model).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                Toast.makeText((noticeBoard)context, "Bookmark removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    private void addPostToBookmarks(createPost model) {
        firebase.addPostToBookmarks(Objects.requireNonNull(model)).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText((noticeBoard)context, "Bookmark added", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText((noticeBoard)context, "An error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("NoticeBoardButton", Context.MODE_PRIVATE);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

}
