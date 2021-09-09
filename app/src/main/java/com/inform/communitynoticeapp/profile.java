package com.inform.communitynoticeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class profile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth userAuth;
    private Context context;


    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userAuth= FirebaseAuth.getInstance();
        TextView welcomeMessageTV = findViewById(R.id.welcomeMessage_TV);
        Button logoutBtn = findViewById(R.id.logout_Btn);
        Button editButton = findViewById(R.id.editProfile_Btn);
        logoutBtn.setOnClickListener(this);
        editButton.setOnClickListener(this);
        FirebaseUser user = userAuth.getCurrentUser();
        assert user != null;
        welcomeMessageTV.setText(getString(R.string.Greeting)+ user.getDisplayName()+"!");

        context = this;

        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set posts selected
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        //perform itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId())
            {
                case R.id.nav_board:
                    startActivity(new Intent(getApplicationContext(),board.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.nav_post:
                    startActivity(new Intent(getApplicationContext(),posts.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.nav_profile:
                    return true;


            }
            return false;
        });

        //TODO: Make profile picture display in user account

    }



    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.logout_Btn:
                showLogoutDialog();
                break;
            case R.id.editProfile_Btn:
                Intent editProfile = new Intent(profile.this, profileEditor.class);
                startActivity(editProfile);
                break;

        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            userAuth.signOut();
            ((Activity) context).finish();
            Intent login = new Intent(profile.this, LogIn.class);
            startActivity(login);

        });
        builder.setNegativeButton("No", (dialog, id) -> {
            // User cancelled the dialog
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}