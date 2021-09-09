package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        TextView usernameTV = findViewById(R.id.username_TV);
        usernameTV.setText(firebase.getUser().getDisplayName());
        TextView communityTV = findViewById(R.id.commGroup_TV);


        typeET=findViewById(R.id.type_ET);
        Button postBtn = findViewById(R.id.post_Btn);
        ImageView takePhoto = findViewById(R.id.takePhoto_IV);
        ImageView galleryPhoto = findViewById(R.id.addPhoto_IV);

        takePhoto.setOnClickListener(this);
        galleryPhoto.setOnClickListener(this);

        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set posts selected
        bottomNavigationView.setSelectedItemId(R.id.nav_post);

        //perform itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case R.id.nav_board:
                    startActivity(new Intent(getApplicationContext(),board.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.nav_post:
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM 'at' HH:mm");
        String dateNow = dateFormat.format(date);

        String text = typeET.getText().toString();

        firebase.addPostsToFirebase(text, dateNow).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(posts.this, "Post Submitted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(posts.this, "Some error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
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

    //TODO: Handle taking photos and also storing them in firebase
    private void handleTakingPhoto() {
    }

    private void handleAddingPhoto() {
    }
}
