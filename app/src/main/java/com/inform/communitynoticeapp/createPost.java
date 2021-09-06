package com.inform.communitynoticeapp;

public class createPost {

    private String post;
    private String user;
    private String dateTime;

    public createPost() {
    }

    public createPost(String user, String post, String dateTime){
        this.user=user;
        this.post=post;
        this.dateTime=dateTime;
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
}
