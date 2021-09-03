package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

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
    private Button loginBtn;
    private TextView signUpText;
    private String password, email;
    private FirebaseAuth userAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validate = new validateInput(this);

        //[START] login Part
        emailET = findViewById(R.id.email_ET);
        passwordET = findViewById(R.id.password_ET);
        userAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.login_btn);
        signUpText = findViewById(R.id.signUp_TV);

        loginBtn.setOnClickListener(this);
        signUpText.setOnClickListener(this);
        //[END] Login Part
    }

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
        password = passwordET.getText().toString();
        email = emailET.getText().toString();

        if(validate.checkEmailValid(email) && validate.checkPasswordValid(password)){
            userAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    //Used to get user info e.g. email, password, etc.
                    Toast.makeText(MainActivity.this, "You have Logged in successfully!", Toast.LENGTH_SHORT).show();
                    Intent post = new Intent(MainActivity.this, posts.class);
                    startActivity(post);
                }else{
                    Toast.makeText(MainActivity.this, "Error has occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleSignUpClick() {
        Intent signUp = new Intent(MainActivity.this, SignUp.class);
        startActivity(signUp);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}