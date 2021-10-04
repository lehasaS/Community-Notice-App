package com.inform.communitynoticeapp;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class dataBaseFirebase implements Cloneable, Serializable {

    private final FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private static final dataBaseFirebase instance = new dataBaseFirebase();
    private final StorageReference storageRef;
    private dataBaseFirebase(){
        if(instance!=null){
            throw new IllegalStateException("Firebase database instance is already created");
        }
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public static dataBaseFirebase getInstance(){
        return instance;
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        return user;
    }

    public StorageReference getStorageRef() {
        return storageRef;
    }


    public Uri getDisplayPic() {
        return getUser().getPhotoUrl();
    }

    private DatabaseReference getRootRef(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public void addCommunitiesToFirebase(ArrayList<String> communities){
        this.getRootRef().child("Communities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.hasChildren())){
                    saveCommunitiesInFirebase(communities);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveCommunitiesInFirebase(@NonNull ArrayList<String> communities){
        DatabaseReference communityRef = this.getRootRef().child("Communities").getRef();
        DatabaseReference newRef = communityRef.push();

        for(String com: communities){
            newRef.setValue(com);
            newRef = communityRef.push();
        }
    }

    public void saveNameInFirebase(userDetails userCurrent){
        DatabaseReference nameRef = this.getRootRef().child("Users").child(Objects.requireNonNull(userAuth.getUid()));
        nameRef.setValue(userCurrent);
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

    public void updateDisplayPicture(Uri photoUri){
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(photoUri).build();
        this.getUser().updateProfile(profileChangeRequest);
    }

    public Uri getDisplayPicture() {
        return getUser().getPhotoUrl();
    }

    public void updateCommunity(String community) {
        getRootRef().child("Users").child(getUser().getUid()).child("community").setValue(community);
    }

    public Query readPostForNoticeBoard(String community){
        return this.getRootRef().child("Posts").child("NoticeBoard").orderByChild("community").equalTo(community);
    }

    public Query readPostForMessageBoard(String community){
        return this.getRootRef().child("Posts").child("MessageBoard").orderByChild("community").equalTo(community);
    }

    public DatabaseReference readBookmarks(){
        return this.getRootRef().child("Users").child(this.getUser().getUid()).child("Bookmarks").getRef();
    }

    public Task<Void> addPostToNoticeBoardNode(String text, String dateNow, String imageUri, ArrayList<String> hashtags, String community){
        DatabaseReference postRef = this.getRootRef().child("Posts").child("NoticeBoard").getRef();
        DatabaseReference newRef = postRef.push();
        createPost post = new createPost(this.getUser().getDisplayName(), text, dateNow, imageUri, newRef.getKey(), hashtags, community);
        return newRef.setValue(post);
    }

    public Task<Void> addPostToMessageBoardNode(String text, String dateNow, String imageUri, ArrayList<String> hashtags, String community){
        DatabaseReference postRef = this.getRootRef().child("Posts").child("MessageBoard").getRef();
        DatabaseReference newRef = postRef.push();
        createPost post = new createPost(this.getUser().getDisplayName(), text, dateNow, imageUri, newRef.getKey(), hashtags, community);
        return newRef.setValue(post);
    }

    public Task<Void> addPostToBookmarks(@NonNull createPost post){
        DatabaseReference bookmarkRef = this.getRootRef().child("Users").child(this.getUser().getUid()).child("Bookmarks").getRef();
        DatabaseReference newRef = bookmarkRef.push();
        return newRef.setValue(post);
    }

    public Query removeBookmark(String postID){
        return this.getRootRef().child("Users").child(this.getUser().getUid()).child("Bookmarks").orderByChild("postID").equalTo(postID);
    }

    public void uploadPicture(Uri imgUri) {
        final String randomKey = UUID.randomUUID().toString();
        StorageReference pictureRef = storageRef.child("images/" + randomKey);
        pictureRef.putFile(imgUri);
    }

    public DatabaseReference readRequests(){
        return this.getRootRef().child("Requests").getRef();
    }

    public Task<Void> addRequest(String reason, String dateNow){
        getRootRef().child("Users").child(this.getUser().getUid()).child("requestStatus").setValue("Pending");
        DatabaseReference requestRef = this.getRootRef().child("Requests").getRef();
        DatabaseReference newRef = requestRef.push();
        request newRequest = new request(this.getUser().getUid(), this.getUser().getDisplayName(), this.getUser().getEmail(), reason, dateNow, newRef.getKey());
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

}
