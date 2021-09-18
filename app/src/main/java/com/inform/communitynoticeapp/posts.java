package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class posts extends AppCompatActivity implements View.OnClickListener {

    private EditText typeET;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        TextView dispNameTV = findViewById(R.id.dispName_TV);
        dispNameTV.setText(firebase.getUser().getDisplayName());
        TextView communityTV = findViewById(R.id.commGroup_TV);
        typeET=findViewById(R.id.type_ET);
        Button postBtn = findViewById(R.id.post_Btn);
        ImageView takePhoto = findViewById(R.id.takePhoto_IV);
        ImageView galleryPhoto = findViewById(R.id.addPhoto_IV);

        firebase.getUserDetailsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails details = snapshot.getValue(userDetails.class);
                assert details != null;
                communityTV.setText(details.getCommunity());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        takePhoto.setOnClickListener(this);
        galleryPhoto.setOnClickListener(this);

        postBtn.setOnClickListener(view -> checkRole());

    }

    private void checkRole(){
        firebase.getUserDetailsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails details = snapshot.getValue(userDetails.class);
                assert details != null;
                if(details.getRole().equals("Community Member")){
                    //tell user you cant post here
                    //askUserToPost();
                    postToMessageBoard();
                }
                else{
                    askWhereToPost();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(posts.this, "Some error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.takePhoto_IV:
                handleTakingPhoto();
                break;
            case R.id.addPhoto_IV:
                handleAddingPhoto();
                break;
        }
    }

    private void askWhereToPost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Post To?");
        builder.setPositiveButton("Message Board", (dialog, id) -> {
            postToMessageBoard();
        });
        builder.setNegativeButton("Notice Board", (dialog, id) -> {
            postToNoticeBoard();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void postToMessageBoard() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM 'at' HH:mm");
        String dateNow = dateFormat.format(date);

        String text = typeET.getText().toString();

        firebase.addPostToMessageBoardNode(text, dateNow).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(posts.this, "Post Submitted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(posts.this, "Some error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
            }
            typeET.setText("");
        });
        goToMessageBoard();
    }

    private void goToMessageBoard() {
        Intent goToMessageBoard = new Intent(posts.this, messageBoard.class);
        startActivity(goToMessageBoard);
    }

    private void postToNoticeBoard() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM 'at' HH:mm");
        String dateNow = dateFormat.format(date);

        String text = typeET.getText().toString();

        firebase.addPostToNoticeBoardNode(text, dateNow).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(posts.this, "Post Submitted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(posts.this, "Some error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
            }
            typeET.setText("");
        });
       goToNoticeBoard();
    }

    private void goToNoticeBoard() {
        Intent goToNoticeBoard = new Intent(posts.this, noticeBoard.class);
        startActivity(goToNoticeBoard);
    }

    //TODO: Handle taking photos and also storing them in firebase
    private void handleTakingPhoto() {
    }

    private void handleAddingPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            Uri imageUri = data.getData();
            firebase.uploadPicture(imageUri);
        }
    }
}
