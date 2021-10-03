package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class manageCommunities extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private TextInputLayout communityTI;
    private ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_communities);

        context = this;
        communityTI = findViewById(R.id.community_TI);
        Button addBtn = findViewById(R.id.addCommunity_btn);

        recyclerView= findViewById(R.id.recyclerViewCommunities);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        displayCommunities();
        addBtn.setOnClickListener(view -> handleAddBtnClick());
    }

    private void displayCommunities() {
        listener = new ValueEventListener() {
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

                CommunityManagerAdapter comAdapter = new CommunityManagerAdapter(communitiesList, context);
                recyclerView.setAdapter(comAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(manageCommunities.this, "An error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();

            }
        };

        firebase.readCommunities().addValueEventListener(listener);

    }

    private void handleAddBtnClick () {
        String newCommunity = Objects.requireNonNull(communityTI.getEditText()).getText().toString().trim();

        if (listener!=null)
            firebase.readCommunities().removeEventListener(listener);

        firebase.addCommunityToFirebase(newCommunity)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(manageCommunities.this, "Community added", Toast.LENGTH_SHORT).show();
                    displayCommunities();
                }).addOnFailureListener(e -> {
                    Toast.makeText(manageCommunities.this, "Some error occurred: " + e.toString(), Toast.LENGTH_SHORT).show();
                });
    }
}