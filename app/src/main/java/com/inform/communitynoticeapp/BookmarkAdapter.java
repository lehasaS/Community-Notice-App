package com.inform.communitynoticeapp;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder>{
    private final ArrayList<Post> postList;
    private final Context context;
    private final FirebaseConnector firebase= FirebaseConnector.getInstance();

    public BookmarkAdapter(ArrayList<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_card, parent, false);
        return new BookmarkAdapter.ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder holder, int position) {
        holder.dispName.setText(postList.get(position).getUser());
        holder.dateTime.setText(postList.get(position).getDateTime());
        holder.community.setText(postList.get(position).getCommunity());
        //holder.postID.setText(postList.get(position).getPostID());

        /*
        SharedPreferences preferences = getSharedPreferences();
        String state = preferences.getString(position +"pressed", "Yes");

        if(state.equals("yes")){
            holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.clicked_bookmark));
        }

        holder.bookmark.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            SharedPreferences.Editor editor = getSharedPreferences().edit();
            if (isChecked && state.equals("yes")) {
                editor.putString(position + "pressed", "no");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.ic_baseline_bookmark));
                removePostBookmark(holder.postID.getText().toString());
            } else  if (!isChecked && state.equals("no")) {
                editor.putString(position + "pressed", "no");
                editor.apply();
                holder.bookmark.setBackgroundDrawable(ContextCompat.getDrawable(holder.bookmark.getContext(), R.drawable.ic_baseline_bookmark));
                removePostBookmark(holder.postID.getText().toString());
            }

        });

        preferences.getString(position + "pressed", "no");

         */
        if (!postList.get(position).getPost().equals("")) {
            holder.post.setText(postList.get(position).getPost());
        } else {
            holder.post.getLayoutParams().height = 0;
        }

        //show post picture
        if (!postList.get(position).getImageUri().equals("")) {
            StorageReference photoRef = firebase.getFBStorage().getReferenceFromUrl(postList.get(position).getImageUri());

            final long ONE_MEGABYTE = 1024 * 1024;
            photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.postPicIV.setImageBitmap(bmp);
            }).addOnFailureListener(e -> {
                //handle failure
                Toast.makeText((Bookmarks)context, "An error occurred: "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText((Bookmarks)context, "Bookmark removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText((Bookmarks)context, "An error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("BookmarkButton", Context.MODE_PRIVATE);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView dispName, post, dateTime, postID, community;
        ImageView postPicIV;
        MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post=itemView.findViewById(R.id.post_contentThree);
            dispName =itemView.findViewById(R.id.display_nameThree);
            dateTime=itemView.findViewById(R.id.dateTime_TVThree);
            community=itemView.findViewById(R.id.community_TVThree);
            postPicIV=itemView.findViewById(R.id.postPic_IVThree);
            cardView=itemView.findViewById(R.id.cardviewThree);
            //postID=itemView.findViewById(R.id.postIDThree);
        }
    }
}
