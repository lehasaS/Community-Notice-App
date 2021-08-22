package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    EditText emailET, passwordET, passwordAgainET, usernameET;
    private validateInput validate;
    private Button signUpBtn;
    private String password, email, passwordAgain, username;
    private userDetails user;
    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        validate = new validateInput(this);
        //[START] Signup Part
        emailET = findViewById(R.id.email_ET);
        usernameET= findViewById(R.id.username_ET);
        passwordET = findViewById(R.id.password_ET);
        passwordAgainET = findViewById(R.id.password_Again);
        signUpBtn = findViewById(R.id.signUp_btn);
        userAuth = FirebaseAuth.getInstance();
        signUpBtn.setOnClickListener(view -> {
            //To Do: SignUpTheUser
            handleSignUpBtnClick();
        });
        //[END] Signup Part
    }

    private void handleSignUpBtnClick() {
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        passwordAgain = passwordAgainET.getText().toString();
        username = usernameET.getText().toString();
        user = new userDetails(username, email, password);

        if(!(username.equals(""))){
            if(validate.checkEmailValid(email) && validate.checkPasswordValid(password) ){
                if(passwordAgain.equals(password)){
                    //signup user
                    userAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            //Usaed to get user info e.g. email, password, etc.
                            FirebaseUser currentUser = userAuth.getCurrentUser();
                            Toast.makeText(SignUp.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                            saveNameInFirebase(currentUser);
                        }else{
                            Toast.makeText(SignUp.this, "Error has occurred"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveNameInFirebase(FirebaseUser users){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();
        DatabaseReference nameRef = rootRef.child("Users").child(users.getUid());
        DatabaseReference newRef = nameRef.push();
        newRef.setValue(user);
    }
}