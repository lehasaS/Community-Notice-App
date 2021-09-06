package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailET, passwordET;
    private validateInput validate;
    private FirebaseAuth userAuth;
    private View progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validate = new validateInput(this);

        //[START] login Part
        emailET = findViewById(R.id.email_ET);
        passwordET = findViewById(R.id.password_ET);
        userAuth = FirebaseAuth.getInstance();
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

    private void handleLoginBtnClick() {
        showProgressBar();
        String password = passwordET.getText().toString();
        String email = emailET.getText().toString();

        if(validate.checkEmailValid(email) && validate.checkPasswordValid(password)){
            userAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    //Used to get user info e.g. email, password, etc.
                    Toast.makeText(MainActivity.this, "You have Logged in successfully!", Toast.LENGTH_SHORT).show();
                    Intent post = new Intent(MainActivity.this, posts.class);
                    startActivity(post);
                }else{
                    hideProgressBar();
                    Toast.makeText(MainActivity.this, "Error has occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleSignUpClick() {
        Intent signUp = new Intent(MainActivity.this, SignUp.class);
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