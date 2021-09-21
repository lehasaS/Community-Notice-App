package com.inform.communitynoticeapp;

import static com.inform.communitynoticeapp.R.id.nav_bookmarks;
import static com.inform.communitynoticeapp.R.id.nav_messageBoard;
import static com.inform.communitynoticeapp.R.id.nav_noticeBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;

public class bookmarks extends AppCompatActivity {

    private RecyclerView recyclerView;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context context;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        context=this;


        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set posts selected
        bottomNavigationView.setSelectedItemId(nav_bookmarks);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId())
            {
                case nav_noticeBoard:
                    startActivity(new Intent(getApplicationContext(),noticeBoard.class));
                    overridePendingTransition(0,0);
                    return true;

                case nav_messageBoard:
                    startActivity(new Intent(getApplicationContext(),messageBoard.class));
                    overridePendingTransition(0,0);
                    return true;

                case nav_bookmarks:
                    return true;

                case R.id.nav_profile:
                    startActivity(new Intent(getApplicationContext(),profile.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });

        recyclerView= findViewById(R.id.recyclerViewNotice);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //displayBookmarks();

    }

    private void displayBookmarks() {
        //TODO: Display bookmarks(This will be done once remove works)
    }

   /* private void removeBookmark(createPost model) {
        firebase.removeBookmark(model).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                Toast.makeText(bookmarks.this, "Bookmark removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/


}