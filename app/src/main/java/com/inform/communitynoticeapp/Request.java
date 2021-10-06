package com.inform.communitynoticeapp;


public class Request {

    private String requestID;
    private String userID;
    private String displayName;
    private String emailAddress;
    private String reason;
    private String dateTime;
    private String status;

    public Request() {}

    public Request(String userID, String displayName, String emailAddress, String reason, String dateTime, String requestID) {
        this.userID = userID;
        this.displayName = displayName;
        this.emailAddress = emailAddress;
        this.reason = reason;
        this.dateTime = dateTime;
        this.status = "Pending";
        this.requestID = requestID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
}
