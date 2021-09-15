package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class updatePassword extends AppCompatActivity {
    private EditText newPassword, newPasswordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        newPassword = findViewById(R.id.eneterNewPassword_ET);
        newPasswordAgain = findViewById(R.id.enterNewPasswordAgain_ET);
        Button saveChanges = findViewById(R.id.saveChanges_Btn);

        saveChanges.setOnClickListener(view -> handleSaveChangesClick());
    }

    private void handleSaveChangesClick() {
        dataBaseFirebase firebase = dataBaseFirebase.getInstance();
        validateInput validate = new validateInput(this);
        String password = newPassword.getText().toString();
        String passwordAgain = newPasswordAgain.getText().toString();

        if(validate.checkPasswordValid(password, passwordAgain)){
            firebase.updatePassword(password).addOnCompleteListener(task -> {
                Toast.makeText(updatePassword.this, "Password successfully updated!", Toast.LENGTH_SHORT).show();
            });
        }

    }
}