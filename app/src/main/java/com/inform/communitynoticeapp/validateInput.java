package com.inform.communitynoticeapp;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

public class validateInput {

    private Context context;

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

    public boolean checkPasswordValid(String password){
        if(password.length()==0){
            Toast.makeText(context,"Please enter your password", Toast.LENGTH_SHORT ).show();
            return false;
        }else if(password.length()<6){
            Toast.makeText(context, "Please enter password that has at least 6 charters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
}
