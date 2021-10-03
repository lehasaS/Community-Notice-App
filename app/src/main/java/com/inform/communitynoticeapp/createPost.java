package com.inform.communitynoticeapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Map;

public class createPost implements Comparable<createPost> {

    private String post;
    private String user;
    private String dateTime;
    private String imageUri;
    private String postID;
    private ArrayList<String> hashtags;
    private String community;

    public createPost(){

    }

    public createPost(String user, String post, String dateTime, String imageUri, String postID, ArrayList<String> hashtags, String community){
        this.user=user;
        this.post=post;
        this.dateTime=dateTime;
        this.imageUri = imageUri;
        this.postID=postID;
        this.hashtags=hashtags;
        this.community=community;
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

    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    public String getCommunity() {
        return community;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setHashtags(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    public void setCommunity(String community) {
        this.community = community;
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

    @Override
    public int compareTo(createPost post) {
        if (this.postID.equals(post.getPostID())) {
            return 0;
        } else if (this.postID.compareTo(post.getPostID())>0) {
            return -1;
        } else {
            return 1;
        }
    }
}
