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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class messageBoard extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private Context context;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private messageBoardAdapter postAdapter;
    private Chip events, recommendations, crimeInformation, lostPets, localServices, generalNews;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);
        events = findViewById(R.id.EventsChip);
        recommendations = findViewById(R.id.RecommendationsChip);
        crimeInformation = findViewById(R.id.CrimeInfoChip);
        lostPets = findViewById(R.id.lostPetsChip);
        localServices = findViewById(R.id.localServicesChip);
        generalNews = findViewById(R.id.GeneralPostChip);

        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationMessage);

        //set posts selected
        bottomNavigationView.setSelectedItemId(nav_messageBoard);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId())
            {
                case nav_noticeBoard:
                    startActivity(new Intent(getApplicationContext(),noticeBoard.class));
                    overridePendingTransition(0,0);
                    return true;

                case nav_messageBoard:
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
        recyclerView= findViewById(R.id.recyclerViewMessage);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        getCommunities(this::readPost);
    }

    private void readPost(ArrayList<String> communities){
        final ArrayList<createPost> createPostArrayList = new ArrayList<>();
        for (String community: communities) {
            firebase.readPostForMessageBoard(community).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    createPost post;
                    for (DataSnapshot content : snapshot.getChildren()) {
                        post = content.getValue(createPost.class);
                        Objects.requireNonNull(post).setPost(post.getPost());
                        createPostArrayList.add(post);
                    }

                    Collections.sort(createPostArrayList);

                    postAdapter = new messageBoardAdapter(createPostArrayList, context);
                    recyclerView.setAdapter(postAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(messageBoard.this, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.floating_action_button){
            Intent addPost = new Intent(messageBoard.this, posts.class);
            startActivity(addPost);
        }
    }

    private void getCommunities(messageBoard.userCommunitiesI userCommunities){
        firebase.getUserCommunities().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> communitiesList = new ArrayList<>();
                Community aCommunity;
                for(DataSnapshot content: snapshot.getChildren()){
                    aCommunity = content.getValue(Community.class);
                    assert aCommunity != null;
                    communitiesList.add(aCommunity.getName());
                }

                userCommunities.onCallback(communitiesList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(messageBoard.this, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    public void OnCheckedChangeListener(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.EventsChip:
                if (isChecked) {
                    postAdapter.getFilter().filter(events.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
            case R.id.RecommendationsChip:
                if (isChecked) {
                    postAdapter.getFilter().filter(recommendations.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
            case R.id.CrimeInfoChip:
                if (isChecked) {
                    postAdapter.getFilter().filter(crimeInformation.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
            case R.id.lostPetsChip:
                if (isChecked) {
                    postAdapter.getFilter().filter(lostPets.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
            case R.id.localServicesChip:
                if (isChecked) {
                    postAdapter.getFilter().filter(localServices.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
            case R.id.GeneralPostChip:
                if (isChecked) {
                    postAdapter.getFilter().filter(generalNews.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
        }
    }

    private interface userCommunitiesI {
        void onCallback(ArrayList<String> communities);
    }

}