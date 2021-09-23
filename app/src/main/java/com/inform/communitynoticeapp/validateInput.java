package com.inform.communitynoticeapp;

import android.content.Context;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class validateInput {

    private TextInputLayout displayNameTI;
    private TextInputLayout passwordAgainTI;
    private TextInputLayout communityTI;
    private final Context context;
    private final TextInputLayout emailTI;
    private TextInputLayout passwordTI;
    private TextInputLayout emailAgainTI;
    private final ArrayList<String> communities=new ArrayList<>(Arrays.asList("Mowbray", "Cape Town", "Rondebosch", "Claremont"));

    public validateInput(Context context, TextInputLayout emailTI, TextInputLayout emailAgainTI){
        this.context = context;
        this.emailTI =emailTI;
        this.emailAgainTI =emailAgainTI;
    }


    public validateInput(Context context,TextInputLayout emailTI, TextInputLayout passwordTI, TextInputLayout passwordAgainTI, TextInputLayout displayNameTI, TextInputLayout communityTI){
        this.context=context;
        this.displayNameTI=displayNameTI;
        this.emailTI = emailTI;
        this.passwordTI =passwordTI;
        this.passwordAgainTI=passwordAgainTI;
        this.communityTI=communityTI;
    }

    public CharSequence checkDisplayName(String displayName) {
        CharSequence val="valid";
        if(displayName.equals("")){
            displayNameTI.setEndIconActivated(true);
            displayNameTI.setError("Enter display name");
            val=displayNameTI.getError();

        }
        return val;
    }

    public CharSequence checkEmailValid(String email){
        if(email.length()==0){
            emailTI.setEndIconActivated(true);
            emailTI.setError("Please enter your email");
            return emailTI.getError();
        }else if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            emailTI.setEndIconActivated(true);
            emailTI.setError("Email is invalid");
            return emailTI.getError();
        }
        else {
            return "valid";
        }
    }

    public CharSequence checkEmailValid(String email, String emailAgain){
        if(email.length()==0){
            emailTI.setEndIconActivated(true);
            emailTI.setError("Please enter your email");
            return emailTI.getError();
        }else if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            emailTI.setEndIconActivated(true);
            emailTI.setError("Email is invalid");
            return emailTI.getError();
        }else if(!(email.equals(emailAgain))){
            emailTI.setEndIconActivated(true);
            emailAgainTI.setEndIconActivated(true);
            emailAgainTI.setError("Passwords don't match");
            return emailAgainTI.getError();
        }
        else {
            return "valid";
        }
    }

    public CharSequence checkPasswordValid(@NonNull String password, @NonNull String passwordAgain){
        if(password.length()==0){
            passwordTI.setEndIconActivated(true);
            passwordTI.setError("Please enter a password");
            return passwordTI.getError();
        }else if(password.length()<6){
            passwordTI.setEndIconActivated(true);
            passwordTI.setError("Password must be 6 characters");
            return passwordTI.getError();
        } else if(!(passwordAgain.equals(password))){
            passwordTI.setEndIconActivated(true);
            passwordAgainTI.setEndIconActivated(true);
            passwordAgainTI.setError("Passwords don't match");
            return passwordAgainTI.getError();
        } else {
            return "valid";
        }
    }

    public CharSequence checkEnteredPasswordValid(@NonNull String password){
        if(password.length()==0){
            passwordTI.setEndIconActivated(true);
            passwordTI.setError("Please enter a password");
            return passwordTI.getError();
        }else if(password.length()<6){
            passwordTI.setEndIconActivated(true);
            passwordTI.setError("Password must be 6 characters");
            return passwordTI.getError();
        } else {
            return "valid";
        }
    }

    public CharSequence checkCommunity(@NonNull String community){
        if(community.equals("")){
            communityTI.setEndIconActivated(true);
            communityTI.setError("Enter your community name");
            return communityTI.getError();
        } else if(!(communities.contains(community))){
            communityTI.setEndIconActivated(true);
            communityTI.setError("This community group does not exist");
            return communityTI.getError();
        }
        return "valid";
    }

    public Context getContext() {
        return context;
    }
}

