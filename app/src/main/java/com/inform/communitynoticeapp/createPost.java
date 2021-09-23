package com.inform.communitynoticeapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class createPost {

    private String post;
    private String user;
    private String dateTime;
    private String imageUri;
    private String postID;

    public createPost() {
    }

    public createPost(String user, String post, String dateTime, String imageUri, String postID){
        this.user=user;
        this.post=post;
        this.dateTime=dateTime;
        this.imageUri = imageUri;
        this.postID=postID;
    }

    public String getUser() {
        return user;
    }

    public String getPost() {
        return post;
    }

    public String getDateTime(){return  dateTime;}

    public String getImageUri() {
        return imageUri;
    }

    public String getPostID() {
        return postID;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
