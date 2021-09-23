package com.inform.communitynoticeapp;

public class userDetails {
    private String email, dispName, community, role;

    public userDetails(){

    }
    public  userDetails(String dispName, String email, String community, String role){
        this.dispName=dispName;
        this.email=email;
        this.community=community;
        this.role=role;
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

    public String getDispName(){return dispName;}

    public void setDispName(){
        this.dispName=dispName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
