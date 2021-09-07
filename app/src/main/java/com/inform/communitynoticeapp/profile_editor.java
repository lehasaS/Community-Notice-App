package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class profile_editor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);

        TextView email_ = findViewById(R.id.update_email);
        TextView password_ = findViewById(R.id.update_password);

        //email_.setOnClickListener(this);
        //password_.setOnClickListener(this);


    }



    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.update_email:
                Intent edit = new Intent(profile_editor.this,change_email.class);
                startActivity(edit);

            case R.id.update_password:
                Intent edit_ = new Intent(profile_editor.this,profile_editor.class);
                startActivity(edit_);
                break;
        }
    }
}