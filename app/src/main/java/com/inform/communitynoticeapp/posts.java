package com.inform.communitynoticeapp;

import static com.inform.communitynoticeapp.R.id.nav_createPost;
import static com.inform.communitynoticeapp.R.id.nav_messageBoard;
import static com.inform.communitynoticeapp.R.id.nav_noticeBoard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set posts selected
        bottomNavigationView.setSelectedItemId(nav_createPost);

        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId())
            {
                case nav_noticeBoard:
                    startActivity(new Intent(getApplicationContext(),noticeBoard.class));
                    overridePendingTransition(0,0);
                    return true;

                case nav_createPost:
                    return true;

                case nav_messageBoard:
                    startActivity(new Intent(getApplicationContext(),messageBoard.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.nav_profile:
                    startActivity(new Intent(getApplicationContext(),profile.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        postBtn.setOnClickListener(view -> addPost());

    }

    private void addPost(){
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM 'at' HH:mm");
        String dateNow = dateFormat.format(date);

        String text = typeET.getText().toString();

        firebase.addPostsToFirebase(text, dateNow).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(posts.this, "Post Submitted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(posts.this, "Some error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
            }
            typeET.setText("");
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
