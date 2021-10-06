package com.inform.communitynoticeapp;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class FirebaseConnector implements Cloneable, Serializable {

    private final FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private static final FirebaseConnector instance = new FirebaseConnector();

    private FirebaseConnector(){
        if(instance!=null){
            throw new IllegalStateException("Firebase database instance is already created");
        }
    }

    public static FirebaseConnector getInstance(){
        return instance;
    }

    public FirebaseStorage getFBStorage() {
        return FirebaseStorage.getInstance();
    }

    private Object readResolve() throws ObjectStreamException {
        return instance;
    }

    private  Object writeReplace() throws  ObjectStreamException{
        return instance;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException{
        throw new CloneNotSupportedException();
    }

    public FirebaseAuth getUserAuth(){
        return userAuth;
    }

    public FirebaseUser getUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public StorageReference getStorageRef() {
        return FirebaseStorage.getInstance().getReference();
    }

    private DatabaseReference getRootRef(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public Task<Void> addCommunityToFirebase(String community){
        DatabaseReference communityRef = this.getRootRef().child("Communities").getRef();
        DatabaseReference newRef = communityRef.push();
        Community com = new Community(community);
        return newRef.setValue(com);
    }

    public DatabaseReference readCommunities(){
        return this.getRootRef().child("Communities").getRef();
    }

    public void saveNameInFirebase(UserDetails userCurrent, String community){
        DatabaseReference nameRef = this.getRootRef().child("Users").child(Objects.requireNonNull(userAuth.getUid()));
        nameRef.setValue(userCurrent);
        this.joinCommunity(community);
    }

    public Task<AuthResult> signInUser(String email, String password){
        return userAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signUpUser(String email, String password){
        return  userAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<Void> sendVerificationEmail() {
        return Objects.requireNonNull(userAuth.getCurrentUser()).sendEmailVerification();
    }

    public boolean checkIfEmailIsVerified(){
        return Objects.requireNonNull(userAuth.getCurrentUser()).isEmailVerified();
    }

    public void updateDispName(String dispName){
        FirebaseUser currentUser = userAuth.getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(dispName).build();
        assert currentUser != null;
        currentUser.updateProfile(profileChangeRequest);
    }

    public String getDisplayName() {
        return getUser().getDisplayName();
    }


    public DatabaseReference getUserDetailsRef(){
        return getRootRef().child("Users").child(getUser().getUid()).getRef();
    }

    public Task<Void> updateEmail(String email){
        return Objects.requireNonNull(userAuth.getCurrentUser()).updateEmail(email);
    }

    public Task<Void> updatePassword(String password){
        return Objects.requireNonNull(userAuth.getCurrentUser()).updatePassword(password);
    }

    public Uri getDisplayPicture() {
        return getUser().getPhotoUrl();
    }


    public Query readPostForNoticeBoard(String community){
        return this.getRootRef().child("Posts").child("NoticeBoard").orderByChild("community").equalTo(community);
    }

    public Query readPostForMessageBoard(String community){
        return this.getRootRef().child("Posts").child("MessageBoard").orderByChild("community").equalTo(community);
    }

    public Query readBookmarks(){
        return this.getRootRef().child("Bookmarks").child(this.getUser().getUid()).orderByChild("dateTime").getRef();
    }

    public Task<Void> addPostToNoticeBoardNode(String text, String dateNow, String imageUri, ArrayList<String> hashtags, String community){
        DatabaseReference postRef = this.getRootRef().child("Posts").child("NoticeBoard").getRef();
        DatabaseReference newRef = postRef.push();
        Post post = new Post(this.getUser().getDisplayName(), text, dateNow, imageUri, newRef.getKey(), hashtags, community);
        return newRef.setValue(post);
    }

    public Task<Void> addPostToMessageBoardNode(String text, String dateNow, String imageUri, ArrayList<String> hashtags, String community){
        DatabaseReference postRef = this.getRootRef().child("Posts").child("MessageBoard").getRef();
        DatabaseReference newRef = postRef.push();
        Post post = new Post(this.getUser().getDisplayName(), text, dateNow, imageUri, newRef.getKey(), hashtags, community);
        return newRef.setValue(post);
    }

    public Task<Void> addPostToBookmarks(@NonNull Post post){
        DatabaseReference bookmarkRef = this.getRootRef().child("Bookmarks").child(this.getUser().getUid()).getRef();
        DatabaseReference newRef = bookmarkRef.push();
        return newRef.setValue(post);
    }

    public Query removeBookmark(String postID){
        return this.getRootRef().child("Bookmarks").child(this.getUser().getUid()).orderByChild("postID").equalTo(postID);
    }

    public DatabaseReference readRequests(){
        return this.getRootRef().child("Requests").getRef();
    }

    public Task<Void> addRequest(String reason, String dateNow){
        getRootRef().child("Users").child(this.getUser().getUid()).child("requestStatus").setValue("Pending");
        DatabaseReference requestRef = this.getRootRef().child("Requests").getRef();
        DatabaseReference newRef = requestRef.push();
        Request newRequest = new Request(this.getUser().getUid(), this.getUser().getDisplayName(), this.getUser().getEmail(), reason, dateNow, newRef.getKey());
        return newRef.setValue(newRequest);
    }

    public void acceptRequest(String requestID, String userID) {
        getRootRef().child("Users").child(userID).child("role").setValue("Service provider");
        getRootRef().child("Requests").child(requestID).child("status").setValue("Accepted");
        getRootRef().child("Users").child(userID).child("requestStatus").setValue("Accepted");
    }

    public void declineRequest(String requestID, String userID) {
        getRootRef().child("Requests").child(requestID).child("status").setValue("Declined");
        getRootRef().child("Users").child(userID).child("requestStatus").setValue("Declined");
    }

    public void joinCommunity(String community) {
        DatabaseReference communityRef = this.getRootRef().child("Users").child(Objects.requireNonNull(userAuth.getUid())).child("Communities").getRef();
        DatabaseReference newRef = communityRef.push();
        Community com = new Community(community);
        newRef.setValue(com);
    }

    public void leaveCommunity(String communityID) {
        this.getRootRef().child("Users").child(Objects.requireNonNull(userAuth.getUid())).child("Communities").child(communityID).removeValue();
    }

    public DatabaseReference getUserCommunities(){
        return this.getRootRef().child("Users").child(Objects.requireNonNull(userAuth.getUid())).child("Communities").getRef();
    }

}
