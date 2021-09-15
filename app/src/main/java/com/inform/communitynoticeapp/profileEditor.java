package com.inform.communitynoticeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

public class profileEditor extends AppCompatActivity implements View.OnClickListener {

    private EditText dispNameET, communityET;
    private ImageView profilePicIV;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private validateInput validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);
        validate = new validateInput(this);

        profilePicIV = findViewById(R.id.profile_pic_IV);
        dispNameET = findViewById(R.id.new_username_ET);
        communityET = findViewById(R.id.new_community_ET);
        Button uploadPicBtn = findViewById(R.id.upload_pic_Btn);
        TextView updateEmail = findViewById(R.id.updateEmail_TV);
        TextView updatePassword = findViewById(R.id.updatePassword_TV);
        Button saveBtn = findViewById(R.id.editProfile_Btn);

        updateEmail.setOnClickListener(this);
        updatePassword.setOnClickListener(this);
        saveBtn.setOnClickListener(view -> handleSaveBtnClick());
        uploadPicBtn.setOnClickListener(view -> handlePicBtnClick());

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.updateEmail_TV:
                Intent updateEmail = new Intent(profileEditor.this, updateEmail.class);
                startActivity(updateEmail);

            case R.id.updatePassword_TV:
                Intent updatePassword = new Intent(profileEditor.this, updatePassword.class);
                startActivity(updatePassword);
                break;
        }

    }

    private void handleSaveBtnClick() {
        String dispName = dispNameET.getText().toString();
        String community = communityET.getText().toString();
        if (validate.checkDispName(dispName) && validate.checkCommunity(community)) {
            firebase.updateDispName(dispName);
            firebase.updateCommunity(community);
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        }
        Intent profile = new Intent(profileEditor.this, profile.class);
        startActivity(profile);
    }

    private void handlePicBtnClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            Uri imageUri = data.getData();
            profilePicIV.setImageURI(imageUri);
            firebase.updateDisplayPicture(imageUri);
        }
    }
}