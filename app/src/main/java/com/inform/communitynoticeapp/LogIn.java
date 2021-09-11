package com.inform.communitynoticeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    EditText emailET, passwordET;
    private validateInput validate;
    private View progressBar;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validate = new validateInput(this);

        //[START] login Part
        emailET = findViewById(R.id.email_ET);
        passwordET = findViewById(R.id.password_ET);
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
        builder.setPositiveButton("OK", (dialog, id) -> {
            dialog.dismiss();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void handleLoginBtnClick() {
        showProgressBar();
        String password = passwordET.getText().toString();
        String email = emailET.getText().toString();

        if(validate.checkEmailValid(email) && validate.checkPasswordValid(password)){
            firebase.signInUser(email, password).addOnCompleteListener(task -> {
                if(firebase.checkIfEmailIsVerified()){
                    if (task.isSuccessful()) {
                        //Used to get user info e.g. email, password, etc.
                        Toast.makeText(LogIn.this, "You have Logged in successfully!", Toast.LENGTH_SHORT).show();
                        Intent post = new Intent(LogIn.this, posts.class);
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