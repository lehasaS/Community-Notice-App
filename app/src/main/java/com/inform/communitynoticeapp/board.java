package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class board extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<createPost> createPostArrayList;
    private postAdapter postAdapter;
    private Context context;

    public board(){
        super(R.layout.activity_board);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set posts selected
        bottomNavigationView.setSelectedItemId(R.id.nav_board);

        //perform itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case R.id.nav_board:
                    return true;

                case R.id.nav_post:
                    startActivity(new Intent(getApplicationContext(),posts.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.nav_profile:
                    startActivity(new Intent(getApplicationContext(),profile.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        context=this;
        createPostArrayList = new ArrayList<>();

        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        readPost();
    }

    private void readPost(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference postRef = firebaseDatabase.getReference().child("Posts").getRef();
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Datasnapshot contains {PostID1: {user, post}}
                createPost post;
                for(DataSnapshot content: snapshot.getChildren()){
                    post = content.getValue(createPost.class);
                    post.setPost(post.getPost());
                    createPostArrayList.add(post);
                }

                Collections.reverse(createPostArrayList);
                postAdapter = new postAdapter(createPostArrayList, context);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}