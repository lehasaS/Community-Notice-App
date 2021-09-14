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
import com.google.firebase.database.ValueEventListener;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class dataBaseFirebase {

    private final FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private final ArrayList<createPost> createPostArrayList = new ArrayList<>();
    private static final dataBaseFirebase instance = new dataBaseFirebase();

    private dataBaseFirebase(){
        if(instance!=null){
            throw new IllegalStateException("Firebase database instance is already created");
        }
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
    public Object clone() throws CloneNotSupportedException{
        throw new CloneNotSupportedException();
    }

    public FirebaseUser getUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        return user;
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

    public void saveNameInFirebase(String role, userDetails userCurrent){
        DatabaseReference nameRef = this.getRootRef().child("Users").child(role).child(Objects.requireNonNull(userAuth.getUid()));
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

    public void updateUsername(String username){
        FirebaseUser currentUser = userAuth.getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
        assert currentUser != null;
        currentUser.updateProfile(profileChangeRequest);
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

    public DatabaseReference readPostsFromFirebase(){
        return this.getRootRef().child("Posts").getRef();
    }

    public Task<Void> addPostsToFirebase(String text, String dateNow){
        DatabaseReference postRef = this.getRootRef().child("Posts").getRef();
        DatabaseReference newRef = postRef.push();
        createPost post = new createPost(this.getUser().getDisplayName(), text, dateNow);
        return newRef.setValue(post);
    }

}
