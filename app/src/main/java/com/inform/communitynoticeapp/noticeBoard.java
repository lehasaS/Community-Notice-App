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


public class noticeBoard extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Context context;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private ArrayList<String> selectedChips;

    public noticeBoard(){
        super(R.layout.activity_notice_board);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        Chip events = findViewById(R.id.Events_chip);
        Chip recommendations = findViewById(R.id.Recommendations_chip);
        Chip crimeInformation = findViewById(R.id.CrimeInfo_chip);
        Chip lostPets = findViewById(R.id.lostPets_chip);
        Chip localServices = findViewById(R.id.localServices_chip);
        Chip generalNews = findViewById(R.id.GeneralPost_chip);
        selectedChips = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener checkedChangeListener = (compoundButton, isChecked) -> {
            if(isChecked){
                selectedChips.add(compoundButton.getText().toString());
            }else{
                selectedChips.remove(compoundButton.getText().toString());
            }
        };

        events.setOnCheckedChangeListener(checkedChangeListener);
        recommendations.setOnCheckedChangeListener(checkedChangeListener);
        crimeInformation.setOnCheckedChangeListener(checkedChangeListener);
        lostPets.setOnCheckedChangeListener(checkedChangeListener);
        localServices.setOnCheckedChangeListener(checkedChangeListener);
        generalNews.setOnCheckedChangeListener(checkedChangeListener);
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
        getCommunities(this::readPost);
    }

    private void readPost(ArrayList<String> communities){
        final ArrayList<createPost> createPostArrayList = new ArrayList<>();
        for (String community: communities) {
            firebase.readPostForNoticeBoard(community).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    createPost post;
                    for (DataSnapshot content : snapshot.getChildren()) {
                        post = content.getValue(createPost.class);
                        createPostArrayList.add(post);
                    }

                    Collections.sort(createPostArrayList);

                    noticeBoardAdapter postAdapter = new noticeBoardAdapter(createPostArrayList, context);
                    recyclerView.setAdapter(postAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(noticeBoard.this, "Some error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void getCommunities(noticeBoard.userCommunitiesI userCommunities){
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
                Toast.makeText(noticeBoard.this, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
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

    private interface userCommunitiesI {
        void onCallback(ArrayList<String> communities);
    }

}