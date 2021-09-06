package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class editProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);



       /* TextView editEmail = findViewById(R.id.update_email);
        TextView editPassword = findViewById(R.id.update_password);
        editEmail.setOnClickListener(this);
        editPassword.setOnClickListener(this);*/







    }


  /*  public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.update_email:
                Intent prof_email = new Intent(editProfile.this, change_email.class);
                startActivity(prof_email);
                break;
            case R.id.update_password:
                Intent prof_password = new Intent(editProfile.this, changePassword.class);
                startActivity(prof_password);
                break;
        }
    }*/

}