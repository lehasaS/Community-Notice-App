package com.inform.communitynoticeapp;
import static com.inform.communitynoticeapp.R.id.nav_bookmarks;
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


public class noticeBoard extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Context context;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private ArrayList<createPost> createPostArrayList;

    public noticeBoard(){
        super(R.layout.activity_notice_board);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);

        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set posts selected
        bottomNavigationView.setSelectedItemId(nav_noticeBoard);

        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId())
            {
                case nav_noticeBoard:
                    return true;

                case nav_messageBoard:
                    startActivity(new Intent(getApplicationContext(),messageBoard.class));
                    overridePendingTransition(0,0);
                    return true;

                case nav_bookmarks:
                    startActivity(new Intent(getApplicationContext(),bookmarks.class));
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
        FloatingActionButton addPost = findViewById(R.id.floating_action_button);

        addPost.setOnClickListener(this);

        recyclerView= findViewById(R.id.recyclerViewNotice);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        createPostArrayList = new ArrayList<>();
        readPost();


    }

    private void readPost(){
        firebase.readPostForNoticeBoard().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                createPost post;
                for(DataSnapshot content: snapshot.getChildren()){
                    post = content.getValue(createPost.class);
                    Objects.requireNonNull(post).setPost(post.getPost());
                    createPostArrayList.add(0,post);
                }
                noticeBoardAdapter postAdapter = new noticeBoardAdapter(createPostArrayList, context);
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
            Intent addPost = new Intent(noticeBoard.this, posts.class);
            startActivity(addPost);
        }
    }

}