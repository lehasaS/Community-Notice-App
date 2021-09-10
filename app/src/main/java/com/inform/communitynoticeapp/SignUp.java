package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText emailET, passwordET, passwordAgainET, usernameET, communityET;
    private validateInput validate;
    private String passwordAgain;
    private String username;
    private String community;
    private String role;
    private userDetails userCurrent;
    private ArrayList<String> communities;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        communities=new ArrayList<>(Arrays.asList("Mowbray", "Cape Town", "Rondebosch", "Claremont"));
        firebase.addCommunitiesToFirebase(communities);
        validate = new validateInput(this);

        //[START] Signup Part
        Spinner roles = findViewById(R.id.spinner);
        emailET = findViewById(R.id.email_ET);
        usernameET= findViewById(R.id.username_ET);
        passwordET = findViewById(R.id.password_ET);
        communityET = findViewById(R.id.community_groupET);
        passwordAgainET = findViewById(R.id.password_again_ET);
        Button signUpBtn = findViewById(R.id.signUp_btn);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles_array, android.R.layout.simple_spinner_dropdown_item);
        roles.setAdapter(adapter);
        roles.setOnItemSelectedListener(this);
        signUpBtn.setOnClickListener(view -> handleSignUpBtnClick());
        //[END] Signup Part

    }


    private void handleSignUpBtnClick() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        passwordAgain = passwordAgainET.getText().toString();
        username = usernameET.getText().toString();
        community = communityET.getText().toString();
        userCurrent = new userDetails(username, email, community);

        if(validate.checkEmailValid(email) && validate.checkPasswordValid(password) ){
            if(checkUsername(username) && checkCommunity(community) && checkRole(role) && checkPassword(password)) {
                //signup user
               firebase.signUpUser(email, password).addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       //Used to get user info e.g. email, password, etc.
                       firebase.sendVerificationEmail().addOnCompleteListener(task1 -> {
                           if(task1.isSuccessful()){
                               firebase.ChangeUserName(username);
                               Toast.makeText(SignUp.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                               firebase.saveNameInFirebase(role, userCurrent);
                               Intent login = new Intent(SignUp.this, LogIn.class);
                               startActivity(login);
                           }else {
                               Toast.makeText(SignUp.this, "Error has occurred" + task1.getException(), Toast.LENGTH_SHORT).show();
                           }
                       });
                   } else {
                       Toast.makeText(SignUp.this, "Error has occurred" + task.getException(), Toast.LENGTH_SHORT).show();
                   }

               });
            }
        }

    }

    public boolean checkUsername(@NonNull String username){
        boolean val=true;
        if(username.equals("")){
            Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
            val = false;
        }
        return val;
    }

    public boolean checkRole(@NonNull String role){
        boolean val=true;
        if(role.equals("")){
            Toast.makeText(this, "Select a role", Toast.LENGTH_SHORT).show();
            val=false;
        }
        return val;
    }

    public boolean checkCommunity(@NonNull String communit){
        boolean val=true;
        if(communit.equals("")){
            Toast.makeText(this, "Enter your community name ", Toast.LENGTH_SHORT).show();
            val = false;
        }

        if(!(communities.contains(community))){
            Toast.makeText(this, "This community group does not exist", Toast.LENGTH_SHORT).show();
            val=false;
        }
        return val;
    }

    public boolean checkPassword(@NonNull String password){
        boolean val = true;
        if(!(passwordAgain.equals(password))){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            val=false;
        }
        return val;
    }

    @Override
    public void onItemSelected(@NonNull AdapterView<?> adapterView, View view, int i, long l) {
        role = (String) adapterView.getItemAtPosition(i);

        if(role.equals("Choose Role")){
            role="";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(SignUp.this, "Select a role", Toast.LENGTH_SHORT).show();
    }
}