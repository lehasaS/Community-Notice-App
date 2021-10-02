package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class manageRequests extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private ArrayList<request> requestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        context = this;

        recyclerView= findViewById(R.id.recyclerViewRequests);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        requestsList = new ArrayList<>();
        displayPendingRequests();
    }

    private void displayPendingRequests() {
        firebase.readRequests().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                request aRequest;
                for(DataSnapshot content: snapshot.getChildren()){
                    aRequest = content.getValue(request.class);
                    Objects.requireNonNull(aRequest).setReason(aRequest.getReason());
                    if (aRequest.getStatus().equals("Pending")) {
                        requestsList.add(0, aRequest);
                    }
                }

                requestsAdapter reqAdapter = new requestsAdapter(requestsList, context);
                recyclerView.setAdapter(reqAdapter);
                requestsList=null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}