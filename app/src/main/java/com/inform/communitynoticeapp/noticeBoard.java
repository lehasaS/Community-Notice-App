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
import java.util.Objects;


public class noticeBoard extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Context context;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private ArrayList<createPost> createPostArrayList;
    private noticeBoardAdapter postAdapter;
    private Chip events, recommendations, crimeInformation, lostPets, localServices, generalNews;

    public noticeBoard(){
        super(R.layout.activity_notice_board);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_board);
        events = findViewById(R.id.Events_chip);
        recommendations = findViewById(R.id.Recommendations_chip);
        crimeInformation = findViewById(R.id.CrimeInfo_chip);
        lostPets = findViewById(R.id.lostPets_chip);
        localServices = findViewById(R.id.localServices_chip);
        generalNews = findViewById(R.id.GeneralPost_chip);

        CompoundButton.OnCheckedChangeListener checkedChangeListener = (compoundButton, isChecked) -> {
            if(isChecked){
                postAdapter.getFilter().filter(compoundButton.getText().toString().toLowerCase());
            }else{
                postAdapter.getFilter().filter("");
            }
        };

        events.setOnCheckedChangeListener(checkedChangeListener);
        recommendations.setOnCheckedChangeListener(checkedChangeListener);
        crimeInformation.setOnCheckedChangeListener(checkedChangeListener);
        lostPets.setOnCheckedChangeListener(checkedChangeListener);
        localServices.setOnCheckedChangeListener(checkedChangeListener);
        generalNews.setOnCheckedChangeListener(checkedChangeListener);
        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationNotice);

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
        getUserObject(this::readPost);
    }

    private void readPost(userDetails user){
        firebase.readPostForNoticeBoard(user.getCommunity()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                createPostArrayList.clear();
                createPost post;
                for(DataSnapshot content: snapshot.getChildren()){
                    post = content.getValue(createPost.class);
                    createPostArrayList.add(0,post);
                }
                postAdapter = new noticeBoardAdapter(createPostArrayList, context);
                recyclerView.setAdapter(postAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getUserObject(noticeBoard.userDetailsI userDetail){
        firebase.getUserDetailsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails details = snapshot.getValue(userDetails.class);
                assert details != null;
                userDetail.onCallback(details);
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

    @SuppressLint("NonConstantResourceId")
    public void OnCheckedChangeListener(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.Events_chip:
                if (isChecked) {
                    postAdapter.getFilter().filter(events.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
            case R.id.Recommendations_chip:
                if (isChecked) {
                    postAdapter.getFilter().filter(recommendations.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
            case R.id.CrimeInfo_chip:
                if (isChecked) {
                    postAdapter.getFilter().filter(crimeInformation.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
            case R.id.lostPets_chip:
                if (isChecked) {
                    postAdapter.getFilter().filter(lostPets.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
            case R.id.localServices_chip:
                if (isChecked) {
                    postAdapter.getFilter().filter(localServices.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
            case R.id.GeneralPost_chip:
                if (isChecked) {
                    postAdapter.getFilter().filter(generalNews.getText().toString().toLowerCase());
                } else {
                    postAdapter.getFilter().filter("");
                }
                break;
        }
    }

    private interface userDetailsI {
        void onCallback(userDetails user);
    }

}