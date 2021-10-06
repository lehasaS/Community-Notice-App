package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class JoinCommunities extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private final FirebaseConnector firebase = FirebaseConnector.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_communities);

        context = this;

        recyclerView = findViewById(R.id.recyclerViewJoinCommunities);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        displayCommunities();
    }

    private void displayCommunities() {
        firebase.readCommunities().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> communitiesList = new ArrayList<>();
                Community aCommunity;
                for(DataSnapshot content: snapshot.getChildren()){
                    aCommunity = content.getValue(Community.class);
                    assert aCommunity != null;
                    communitiesList.add(aCommunity.getName());
                }

                Collections.sort(communitiesList);

                JoinCommunitiesAdapter comAdapter = new JoinCommunitiesAdapter(communitiesList, context);
                recyclerView.setAdapter(comAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(JoinCommunities.this, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}