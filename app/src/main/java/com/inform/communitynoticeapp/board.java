package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class board extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<createPost> createPostArrayList, postArrayList;
    private postAdapter postAdapter;
    private Context context;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();

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
        postArrayList=firebase.readPostsFromFirebase(createPostArrayList);
        postAdapter = new postAdapter(postArrayList, context);
        recyclerView.setAdapter(postAdapter);
    }



}