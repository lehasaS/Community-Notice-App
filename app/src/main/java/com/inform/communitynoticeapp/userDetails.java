package com.inform.communitynoticeapp;

public class userDetails {
    private String password, email, username;

    public userDetails(){

    }
    public  userDetails(String usernamme, String email, String password){
        this.username=usernamme;
        this.email=email;
        this.password=password;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(){
        this.email=email;
    }

    public String getUsername(){
        return password;
    }

    public void setUsername(){
        this.username=username;
    }
}
