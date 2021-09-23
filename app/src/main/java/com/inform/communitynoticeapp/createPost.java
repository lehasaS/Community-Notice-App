package com.inform.communitynoticeapp;

import com.google.firebase.database.Exclude;

public class createPost {

    private String post;
    private String user;
    private String dateTime;
    private String imageUri;

    public createPost() {
    }

    public createPost(String user, String post, String dateTime){
        this.user=user;
        this.post=post;
        this.dateTime=dateTime;
        this.imageUri = "";
    }

    public createPost(String user, String post, String dateTime, String imageUri){
        this.user=user;
        this.post=post;
        this.dateTime=dateTime;
        this.imageUri = imageUri;
    }

    public String getUser() {
        return user;
    }

    public String getPost() {
        return post;
    }

    public String getDateTime(){return  dateTime;}

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Exclude
    public boolean setSelected(boolean b) {
        return b;
    }

    @Exclude
    public boolean isSelected() {
        return true;
    }
}
