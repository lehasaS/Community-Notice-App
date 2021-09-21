package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class updateEmail extends AppCompatActivity {
    private TextInputLayout newEmail, newEmailAgain;
    private validateInput validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        newEmail = findViewById(R.id.updateEmail_TI);
        newEmailAgain = findViewById(R.id.updateEmailAgain_TI);
        validate = new validateInput(this,newEmail, newEmailAgain);
        Button saveChanges = findViewById(R.id.saveChanges_Btn2);
        saveChanges.setOnClickListener(view -> handleSaveChangesClick());
    }

    private void handleSaveChangesClick() {
        dataBaseFirebase firebase = dataBaseFirebase.getInstance();
        String email = Objects.requireNonNull(newEmail.getEditText()).getText().toString();
        String emailAgain = Objects.requireNonNull(newEmailAgain.getEditText()).getText().toString();

        if(validate.checkEmailValid(email,emailAgain).equals("valid")){
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