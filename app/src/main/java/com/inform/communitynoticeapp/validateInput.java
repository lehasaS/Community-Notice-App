package com.inform.communitynoticeapp;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

public class validateInput {

    private Context context;
    private ArrayList<String> communities=new ArrayList<>(Arrays.asList("Mowbray", "Cape Town", "Rondebosch", "Claremont"));

    public validateInput(Context context){
        this.context=context;
    }

    public boolean checkEmailValid(String email){
        if(email.length()==0){
            Toast.makeText(context,"Please enter your email", Toast.LENGTH_SHORT ).show();
            return false;
        }else if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            Toast.makeText(context,"Email is invalid", Toast.LENGTH_SHORT ).show();
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkPasswordValid(@NonNull String password, @NonNull String passwordAgain){
        if(password.length()==0){
            Toast.makeText(context,"Please enter a password", Toast.LENGTH_SHORT ).show();
            return false;
        }else if(password.length()<6){
            Toast.makeText(context, "Please enter password that has at least 6 charters", Toast.LENGTH_SHORT).show();
            return false;
        } else if(!(passwordAgain.equals(password))){
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean checkEnteredPasswordValid(@NonNull String password){
        if(password.length()==0){
            Toast.makeText(context,"Please enter your password", Toast.LENGTH_SHORT ).show();
            return false;
        }else if(password.length()<6){
            Toast.makeText(context, "Please enter password that has at least 6 charters", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean checkDispName(@NonNull String dispName){
        boolean val=true;
        if(dispName.equals("")){
            Toast.makeText(context, "Enter display name", Toast.LENGTH_SHORT).show();
            val = false;
        }
        return val;
    }

    public boolean checkCommunity(@NonNull String community){
        boolean val=true;
        if(community.equals("")){
            Toast.makeText(context, "Enter your community name ", Toast.LENGTH_SHORT).show();
            val = false;
        } else if(!(communities.contains(community))){
            Toast.makeText(context, "This community group does not exist", Toast.LENGTH_SHORT).show();
            val=false;
        }
        return val;
    }

    public boolean checkRole(@NonNull String role){
        boolean val=true;
        if(role.equals("")){
            Toast.makeText(context, "Select a role", Toast.LENGTH_SHORT).show();
            val=false;
        }
        return val;
    }
}
