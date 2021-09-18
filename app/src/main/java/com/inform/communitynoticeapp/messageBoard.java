package com.inform.communitynoticeapp;

import static com.inform.communitynoticeapp.R.id.nav_messageBoard;
import static com.inform.communitynoticeapp.R.id.nav_noticeBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class messageBoard extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<createPost> createPostArrayList;
    private FloatingActionButton addPost;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);


        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set posts selected
        bottomNavigationView.setSelectedItemId(nav_messageBoard);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId())
            {
                case nav_noticeBoard:
                    startActivity(new Intent(getApplicationContext(),noticeBoard.class));
                    overridePendingTransition(0,0);

                case nav_messageBoard:
                    return true;

                case R.id.nav_profile:
                    startActivity(new Intent(getApplicationContext(),profile.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        context=this;
        addPost=findViewById(R.id.floating_action_button);

        addPost.setOnClickListener(this);

        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        createPostArrayList = new ArrayList<>();
        readPost();
    }

    private void readPost(){
        firebase.readPostForMessageBoard().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                createPost post;
                for(DataSnapshot content: snapshot.getChildren()){
                    post = content.getValue(createPost.class);
                    Objects.requireNonNull(post).setPost(post.getPost());
                    createPostArrayList.add(0,post);
                }

                postAdapter postAdapter = new postAdapter(createPostArrayList, context);
                recyclerView.setAdapter(postAdapter);
                createPostArrayList=null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id==R.id.floating_action_button){
            Intent addPost = new Intent(messageBoard.this, posts.class);
            startActivity(addPost);
        }
    }
}