package com.inform.communitynoticeapp;

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

public class dataBaseFirebase  {

    private final FirebaseAuth userAuth = FirebaseAuth.getInstance();
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

    public boolean checkIfEmailIsVerified(){
        AtomicBoolean val = new AtomicBoolean(false);
        Task<Void> userTask = Objects.requireNonNull(userAuth.getCurrentUser()).reload();
        userTask.addOnSuccessListener(unused -> {
            val.set(userAuth.getCurrentUser().isEmailVerified());
        });
        return val.get();
    }

    public void addCommunitiesToFirebase(ArrayList<String> communities){
        FirebaseDatabase.getInstance().getReference().child("Communities").addListenerForSingleValueEvent(new ValueEventListener() {
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
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();
        DatabaseReference communityRef = rootRef.child("Communities").getRef();
        DatabaseReference newRef = communityRef.push();

        for(String com: communities){
            newRef.setValue(com);
            newRef = communityRef.push();
        }
    }

    public void saveNameInFirebase(String role, userDetails userCurrent){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();
        DatabaseReference nameRef = rootRef.child("Users").child(role).child(Objects.requireNonNull(userAuth.getUid()));
        nameRef.setValue(userCurrent);
    }

    public Task<AuthResult> signUpUser(String email, String password){
        return  userAuth.createUserWithEmailAndPassword(email, password);
    }

    public void ChangeUserName(String username){
        FirebaseUser currentUser = userAuth.getCurrentUser();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
        assert currentUser != null;
        currentUser.updateProfile(profileChangeRequest);
    }

    public Task<Void> sendVerificationEmail() {
       return Objects.requireNonNull(userAuth.getCurrentUser()).sendEmailVerification();
    }

    public Task<AuthResult> signInUser(String email, String password){
        return userAuth.signInWithEmailAndPassword(email, password);
    }

    public ArrayList<createPost> readPostsFromFirebase(){
        ArrayList<createPost> createPostArrayList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference postRef = firebaseDatabase.getReference().child("Posts").getRef();
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Datasnapshot contains {PostID1: {user, post}}
                createPost post;
                for(DataSnapshot content: snapshot.getChildren()){
                    post = content.getValue(createPost.class);
                    Objects.requireNonNull(post).setPost(post.getPost());
                    createPostArrayList.add(0,post);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return createPostArrayList;
    }

    public Task<Void> updateEmail(String email){
        return userAuth.getCurrentUser().updateEmail(email);
    }

    public Task<Void> updatePassword(String password){
        return userAuth.getCurrentUser().updatePassword(password);
    }

    public Task<Void> addPostsToFirebase(String text, String dateNow){
        FirebaseUser currentUser = userAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();
        DatabaseReference postRef = rootRef.child("Posts").getRef();
        DatabaseReference newRef = postRef.push();
        assert currentUser != null;
        createPost post = new createPost(currentUser.getDisplayName(), text, dateNow);
        return newRef.setValue(post);
    }
}
