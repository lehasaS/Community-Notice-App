package com.inform.communitynoticeapp;

public class createPost {

    private String post;
    private String user;

    public createPost() {
    }

    public createPost(String user, String post){
        this.user=user;
        this.post=post;
    }

    public String getUser() {
        return user;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
