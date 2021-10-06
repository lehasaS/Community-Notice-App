package com.inform.communitynoticeapp;

import java.util.ArrayList;

public class UserDetails {
    private String email, dispName, community, role, requestStatus;
    private ArrayList<String> communities;

    public UserDetails(){

    }
    public UserDetails(String dispName, String email, String community, String role){
        this.dispName=dispName;
        this.email=email;
        this.community=community;
        this.role=role;
        this.requestStatus="None";
    }

    public UserDetails(String dispName, String email, String role){
        this.dispName=dispName;
        this.email=email;
        this.role=role;
        this.requestStatus="None";
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

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
