package com.inform.communitynoticeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout emailTI, passwordTI;
    private ValidateInput validate;
    private View progressBar;
    private final FirebaseConnector firebase = FirebaseConnector.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //[START] login Part
        emailTI = findViewById(R.id.email_TI);
        passwordTI = findViewById(R.id.password_TI);
        validate = new ValidateInput(this,emailTI, passwordTI, null, null, null);
        Button loginBtn = findViewById(R.id.login_btn);
        TextView signUpText = findViewById(R.id.signUp_TV);
        progressBar = findViewById(R.id.progressBar);
        loginBtn.setOnClickListener(this);
        signUpText.setOnClickListener(this);
        //[END] Login Part
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.login_btn:
                handleLoginBtnClick();
                break;
            case R.id.signUp_TV:
                handleSignUpClick();
                break;
        }
    }

    public void showVerifyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please verify your email!");
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void handleLoginBtnClick() {
        showProgressBar();
        String email = Objects.requireNonNull(emailTI.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(passwordTI.getEditText()).getText().toString().trim();

        if(validate.checkEmailValid(email).equals("valid") && validate.checkEnteredPasswordValid(password).equals("valid")){
            firebase.signInUser(email, password).addOnCompleteListener(task -> {
                if(firebase.checkIfEmailIsVerified()){
                    if (task.isSuccessful()) {
                        //Used to get user info e.g. email, password, etc.
                        Toast.makeText(LogIn.this, "You have Logged in successfully!", Toast.LENGTH_SHORT).show();
                        Intent post = new Intent(LogIn.this, NoticeBoard.class);
                        startActivity(post);
                    } else {
                        hideProgressBar();
                        Toast.makeText(LogIn.this, "Error has occurred: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    showVerifyDialog();
                    hideProgressBar();
                }
            });
        }else {
            hideProgressBar();
        }
    }

    private void handleSignUpClick() {
        Intent signUp = new Intent(LogIn.this, SignUp.class);
        startActivity(signUp);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}