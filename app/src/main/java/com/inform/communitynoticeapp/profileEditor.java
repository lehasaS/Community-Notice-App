package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class profileEditor extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);

        TextView updateEmail = findViewById(R.id.updateEmailTV);
        TextView updatePassword = findViewById(R.id.updatePasswordTV);


        updateEmail.setOnClickListener(this);
        updatePassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.updateEmailTV:
                Intent updateEmail = new Intent(profileEditor.this, changeEmail.class);
                startActivity(updateEmail);

            case R.id.updatePasswordTV:
                Intent updatePassword = new Intent(profileEditor.this, changePassword.class);
                startActivity(updatePassword);
                break;
        }

    }
}