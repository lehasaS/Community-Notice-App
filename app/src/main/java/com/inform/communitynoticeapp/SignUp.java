package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText emailET, passwordET, passwordAgainET, dispNameET, communityET;
    private validateInput validate;
    private String dispName;
    private String role;
    private userDetails userCurrent;
    //private ArrayList<String> communities;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //communities=new ArrayList<>(Arrays.asList("Mowbray", "Cape Town", "Rondebosch", "Claremont"));
        //firebase.addCommunitiesToFirebase(communities);
        validate = new validateInput(this);

        //[START] Signup Part
        Spinner roles = findViewById(R.id.spinner);
        emailET = findViewById(R.id.email_ET);
        dispNameET = findViewById(R.id.dispName_ET);
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
        String passwordAgain = passwordAgainET.getText().toString();
        dispName = dispNameET.getText().toString();
        String community = communityET.getText().toString();
        userCurrent = new userDetails(dispName, email, community, role);

        if(validate.checkEmailValid(email) && validate.checkPasswordValid(password, passwordAgain) ){
            if(validate.checkDispName(dispName) && validate.checkCommunity(community) && validate.checkRole(role)/* && checkPassword(password)*/) {
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

    public void setDefaultPic() {
        firebase.getStorageRef().child("profilePics/defaultPic.jpg").getDownloadUrl().addOnSuccessListener(defaultPicUri -> {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(defaultPicUri)
                    .build();
            firebase.getUser().updateProfile(profileChangeRequest);
        });
    }
}