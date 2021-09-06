package com.inform.communitynoticeapp;

public class userDetails {
    private String email, username, community;

    public userDetails(){

    }
    public  userDetails(String username, String email, String community){
        this.username=username;
        this.email=email;
        this.community=community;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
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
