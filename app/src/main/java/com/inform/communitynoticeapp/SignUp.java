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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText emailET, passwordET, passwordAgainET, usernameET, communityET;
    private validateInput validate;
    private String passwordAgain;
    private String username;
    private String community;
    private String role;
    private static userDetails userCurrent;
    private ArrayList<String> communities;
    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        communities=new ArrayList<>(Arrays.asList("Mowbray", "Cape Town", "Rondebosch", "Claremont"));

        FirebaseDatabase.getInstance().getReference().child("Communities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.hasChildren())){
                    saveCommunitiesInFirebase();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        validate = new validateInput(this);
        //[START] Signup Part
        Spinner roles = findViewById(R.id.spinner);
        emailET = findViewById(R.id.email_ET);
        usernameET= findViewById(R.id.username_ET);
        passwordET = findViewById(R.id.password_ET);
        communityET = findViewById(R.id.community_groupET);
        passwordAgainET = findViewById(R.id.password_again_ET);
        Button signUpBtn = findViewById(R.id.signUp_btn);
        userAuth = FirebaseAuth.getInstance();

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
        userCurrent = new userDetails(username, email, getCommunityID());

        if(validate.checkEmailValid(email) && validate.checkPasswordValid(password) ){
            if(checkUsername(username) && checkCommunity(community) && checkRole(role) && checkPassword(password)) {
                //signup user
                userAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this::onComplete);
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

    private void saveNameInFirebase(@NonNull FirebaseUser users){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();
        DatabaseReference nameRef = rootRef.child("Users").child(role).child(users.getUid());
        nameRef.setValue(userCurrent);
    }

    private void saveCommunitiesInFirebase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();
        DatabaseReference communityRef = rootRef.child("Communities").getRef();
        DatabaseReference newRef = communityRef.push();

        for(String com: communities){
            newRef.setValue(com);
            newRef = communityRef.push();
        }
    }


    public String getCommunityID(){
        final String[] communityID = new String[1];
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();
        DatabaseReference communityRef = rootRef.child("Communities");
        communityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(Objects.requireNonNull(dataSnapshot.getValue()).equals(community)){
                        communityID[0] =dataSnapshot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return communityID[0];
    }

    private void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            //Used to get user info e.g. email, password, etc.
            FirebaseUser currentUser = userAuth.getCurrentUser();
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
            assert currentUser != null;
            currentUser.updateProfile(profileChangeRequest);
            Toast.makeText(SignUp.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
            saveNameInFirebase(currentUser);
            Intent login = new Intent(SignUp.this, MainActivity.class);
            startActivity(login);
        } else {
            Toast.makeText(SignUp.this, "Error has occurred" + task.getException(), Toast.LENGTH_SHORT).show();
        }
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