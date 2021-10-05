package com.inform.communitynoticeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private TextInputLayout emailTI, passwordTI, passwordAgainTI, displayNameTI, communityTI;
    private validateInput validate;
    private String dispName;
    private userDetails userCurrent;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();
        String[] communityArray = new String[]{"Mowbray", "Cape Town", "Rondebosch", "Claremont"};

        //[START] Signup Part
        emailTI = findViewById(R.id.emailTI);
        displayNameTI = findViewById(R.id.displayNameTI);
        passwordTI = findViewById(R.id.passwordTI);
        passwordAgainTI = findViewById(R.id.passwordAgainTI);
        communityTI = findViewById(R.id.communityTI);
        Button signUpBtn = findViewById(R.id.signUp_btn2);
        validate=new validateInput(this, emailTI, passwordTI, passwordAgainTI, displayNameTI, communityTI);
        ArrayAdapter<String> communities = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, communityArray);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteCommunity);
        textView.setAdapter(communities);
        signUpBtn.setOnClickListener(view -> handleSignUpBtnClick());
        //[END] Signup Part

    }


    private void handleSignUpBtnClick() {
        dispName = Objects.requireNonNull(displayNameTI.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(emailTI.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(passwordTI.getEditText()).getText().toString().trim();
        String passwordAgain = Objects.requireNonNull(passwordAgainTI.getEditText()).getText().toString().trim();
        String community = Objects.requireNonNull(communityTI.getEditText()).getText().toString().trim();
        String role = "Community Member";//default role
        userCurrent = new userDetails(dispName, email, community, role);

        if(validate.checkEmailValid(email).equals("valid") && validate.checkPasswordValid(password, passwordAgain).equals("valid") ){
            if(validate.checkDisplayName(dispName).equals("valid") && validate.checkCommunity(community).equals("valid")) {
                //signup user
               firebase.signUpUser(email, password).addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       //Used to get user info e.g. email, password, etc.
                       firebase.sendVerificationEmail().addOnCompleteListener(task1 -> {
                           if(task1.isSuccessful()){
                               firebase.updateDispName(dispName);
                               firebase.saveNameInFirebase(userCurrent);
                               setDefaultPic();
                               Toast.makeText(SignUp.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
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


    public void setDefaultPic() {
        firebase.getStorageRef().child("profilePics/defaultPic.jpg").getDownloadUrl().addOnSuccessListener(defaultPicUri -> {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(defaultPicUri)
                    .build();
            firebase.getUser().updateProfile(profileChangeRequest);
        });
    }
}