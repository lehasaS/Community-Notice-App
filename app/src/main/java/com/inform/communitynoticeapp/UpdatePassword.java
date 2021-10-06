package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class UpdatePassword extends AppCompatActivity {
    private TextInputLayout newPassword, newPasswordAgain;
    private ValidateInput validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        newPassword = findViewById(R.id.newPasswordTI);
        newPasswordAgain = findViewById(R.id.newPasswordAgainTI);
        Button saveChanges = findViewById(R.id.saveChanges_Btn);
        validate = new ValidateInput(this,null, newPassword, newPasswordAgain, null, null);

        saveChanges.setOnClickListener(view -> handleSaveChangesClick());
    }

    private void handleSaveChangesClick() {
        FirebaseConnector firebase = FirebaseConnector.getInstance();
        String password = Objects.requireNonNull(newPassword.getEditText()).getText().toString();
        String passwordAgain = Objects.requireNonNull(newPasswordAgain.getEditText()).getText().toString();

        if(validate.checkPasswordValid(password, passwordAgain).equals("valid")){
            firebase.updatePassword(password).addOnCompleteListener(task -> Toast.makeText(UpdatePassword.this, "Password successfully updated!", Toast.LENGTH_SHORT).show());
        }

    }
}