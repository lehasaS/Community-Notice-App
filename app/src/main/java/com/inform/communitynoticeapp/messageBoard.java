package com.inform.communitynoticeapp;

import static com.inform.communitynoticeapp.R.id.nav_createPost;
import static com.inform.communitynoticeapp.R.id.nav_messageBoard;
import static com.inform.communitynoticeapp.R.id.nav_noticeBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class messageBoard extends AppCompatActivity {

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
                    return true;

                case nav_createPost:
                    startActivity(new Intent(getApplicationContext(),posts.class));
                    overridePendingTransition(0,0);
                    return true;

                case nav_messageBoard:
                    return true;

                case R.id.nav_profile:
                    startActivity(new Intent(getApplicationContext(),profile.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }
}