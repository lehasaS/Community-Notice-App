package com.inform.communitynoticeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

public class messageBoardAdapter extends RecyclerView.Adapter<messageBoardAdapter.ViewHolder>{
    private final ArrayList<createPost> postList;
    private final Context context;
    private final dataBaseFirebase firebase=dataBaseFirebase.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public messageBoardAdapter(ArrayList<createPost> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card, parent, false);
        return new messageBoardAdapter.ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dispName.setText(postList.get(position).getUser());
        holder.dateTime.setText(postList.get(position).getDateTime());
        holder.postID.setText(postList.get(position).getPostID());

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
                addPostToBookmarks(postList.get(position));
            } else  if (isChecked && state.equals("yes")) {
                editor.putString(position + "pressed", "no");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.ic_baseline_bookmark));
                removePostBookmark(holder.postID.getText().toString());
            } else  if (!isChecked && state.equals("no")) {
                editor.putString(position + "pressed", "no");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.ic_baseline_bookmark));
                removePostBookmark(holder.postID.getText().toString());
            } else  if (!isChecked && state.equals("yes")) {
                editor.putString(position + "pressed", "yes");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.clicked_bookmark));
                addPostToBookmarks(postList.get(position));
            }

        });


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
                Toast.makeText((messageBoard)context, "An error occurred: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            holder.postPicIV.getLayoutParams().height = 0;
            holder.postPicIV.requestLayout();
        }


    }

    private void removePostBookmark(String postID) {
        firebase.removeBookmark(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    dataSnapshot.getRef().removeValue();
                }
                Toast.makeText((messageBoard)context, "Bookmark removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText((messageBoard)context, "An error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPostToBookmarks(createPost post) {
        firebase.addPostToBookmarks(post).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText((messageBoard)context, "Bookmark added", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText((messageBoard)context, "An error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("MessageBoardButton", Context.MODE_PRIVATE);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dispName, post, dateTime, postID;
        ImageView postPicIV;
        MaterialCardView cardView;
        ToggleButton bookmark;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.post_content);
            dispName =itemView.findViewById(R.id.display_name);
            dateTime=itemView.findViewById(R.id.dateTime_TV);
            postPicIV=itemView.findViewById(R.id.postPic_IV);
            cardView=itemView.findViewById(R.id.cardview);
            bookmark=itemView.findViewById(R.id.bookmark_Btn);
            postID=itemView.findViewById(R.id.postID);
        }
    }
}