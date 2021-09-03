package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText emailET, passwordET, passwordAgainET, usernameET, communityET;
    private validateInput validate;
    private Button signUpBtn;
    private String password, email, passwordAgain, username, community;
    private static userDetails userCurrent;
    private ArrayList<String> communities;
    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        communities= new ArrayList<String>(Arrays.asList("Mowbray", "Cape Town", "Rondebosch", "Wynberg", "Claremont"));
        /*FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.hasChild("Communities"))) {
                    addCommunitiesToFirebase();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        validate = new validateInput(this);
        //[START] Signup Part
        emailET = findViewById(R.id.email_ET);
        usernameET= findViewById(R.id.username_ET);
        passwordET = findViewById(R.id.password_ET);
        communityET = findViewById(R.id.community_groupET);
        passwordAgainET = findViewById(R.id.password_again_ET);
        signUpBtn = findViewById(R.id.signUp_btn);
        userAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(view -> {
            handleSignUpBtnClick();
        });
        //[END] Signup Part

    }

    private void handleSignUpBtnClick() {
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        passwordAgain = passwordAgainET.getText().toString();
        username = usernameET.getText().toString();
        community = communityET.getText().toString();
        userCurrent = new userDetails(username, email);

        if(!(username.equals(""))){
            if(!(community.equals(""))){
                if(communities.contains(community)){
                    if(validate.checkEmailValid(email) && validate.checkPasswordValid(password) ){
                        if(passwordAgain.equals(password)){
                            //signup user
                            userAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this::onComplete);
                        }else{
                            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(this, "This community group does not exist", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Enter your community name", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
        }

    }


    //TODO: Refine this method such that community ID's are associated with users - Lehasa
    public void addUserToCommunityInFirebase(@NonNull FirebaseUser user, String community){
        Map<String, Object> communityUser = new HashMap<>();
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
       DatabaseReference rootRef = firebaseDatabase.getReference();
       DatabaseReference communityRef = rootRef.child("Communities").getRef();
       communityUser.put(community, user.getDisplayName());
       communityRef.setValue(communityUser);
    }


    private void saveNameInFirebase(FirebaseUser users){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();
        DatabaseReference nameRef = rootRef.child("Users").child(users.getUid());
        nameRef.setValue(userCurrent);
    }

    private void onComplete(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            //Used to get user info e.g. email, password, etc.
            FirebaseUser currentUser = userAuth.getCurrentUser();
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
            currentUser.updateProfile(profileChangeRequest);
            Toast.makeText(SignUp.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
            saveNameInFirebase(currentUser);
            addUserToCommunityInFirebase(currentUser, community);

        } else {
            Toast.makeText(SignUp.this, "Error has occurred" + task.getException(), Toast.LENGTH_SHORT).show();
        }
    }
}