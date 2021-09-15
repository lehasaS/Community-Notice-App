package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class updateEmail extends AppCompatActivity {
    private EditText newEmail, newEmailAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        newEmail = findViewById(R.id.eneterNewEmail_ET);
        newEmailAgain = findViewById(R.id.enterNewEmailAgain_ET);
        Button saveChanges = findViewById(R.id.saveChanges_Btn);

        saveChanges.setOnClickListener(view -> handleSaveChangesClick());
    }

    private void handleSaveChangesClick() {
        dataBaseFirebase firebase = dataBaseFirebase.getInstance();
        validateInput validate = new validateInput(this);
        String email = newEmail.getText().toString();
        String emailAgain = newEmailAgain.getText().toString();

        if(validate.checkEmailValid(email)==validate.checkEmailValid(emailAgain)){
            firebase.updateEmail(email).addOnCompleteListener(task -> {
                firebase.getUser().sendEmailVerification();
                Toast.makeText(updateEmail.this, "Email successfully updated!", Toast.LENGTH_SHORT).show();
                firebase.getUserAuth().signOut();
            });
        }

        Intent login = new Intent(updateEmail.this, LogIn.class);
        startActivity(login);
    }
}