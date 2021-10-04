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
import java.util.Objects;

public class messageBoard extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private Context context;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private ArrayList<createPost> createPostArrayList;
    private messageBoardAdapter postAdapter;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);
        Chip events = findViewById(R.id.EventsChip);
        Chip recommendations = findViewById(R.id.RecommendationsChip);
        Chip crimeInformation = findViewById(R.id.CrimeInfoChip);
        Chip lostPets = findViewById(R.id.lostPetsChip);
        Chip localServices = findViewById(R.id.localServicesChip);
        Chip generalNews = findViewById(R.id.GeneralPostChip);

        CompoundButton.OnCheckedChangeListener checkedChangeListener = (compoundButton, isChecked) -> {
            if(isChecked){
                postAdapter.getFilter().filter(compoundButton.getText().toString().toLowerCase());
            }else{
                postAdapter.getFilter().filter(null);
            }
        };

        events.setOnCheckedChangeListener(checkedChangeListener);
        recommendations.setOnCheckedChangeListener(checkedChangeListener);
        crimeInformation.setOnCheckedChangeListener(checkedChangeListener);
        lostPets.setOnCheckedChangeListener(checkedChangeListener);
        localServices.setOnCheckedChangeListener(checkedChangeListener);
        generalNews.setOnCheckedChangeListener(checkedChangeListener);

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
        createPostArrayList = new ArrayList<>();
        getUserObject(this::readPost);
    }

    private void readPost(userDetails user){
        firebase.readPostForMessageBoard(user.getCommunity()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                createPost post;
                for(DataSnapshot content: snapshot.getChildren()){
                    post = content.getValue(createPost.class);
                    createPostArrayList.add(0,post);
                }
                postAdapter = new messageBoardAdapter(createPostArrayList, context);
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

    private void getUserObject(userDetailsI userDetail){
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

    private interface userDetailsI {
        void onCallback(userDetails user);
    }

}