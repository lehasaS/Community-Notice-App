package com.inform.communitynoticeapp;

public class userDetails {
    private String email, username;

    public userDetails(){

    }
    public  userDetails(String usernamme, String email){
        this.username=usernamme;
        this.email=email;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(){
        this.email=email;
    }

    public String getUsername(){return username;}

    public void setUsername(){
        this.username=username;
    }
}
