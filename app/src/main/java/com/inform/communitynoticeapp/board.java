package com.inform.communitynoticeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class board extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<createPost> createPostArrayList;
    private postAdapter postAdapter;
    private Context context;
    private EditText typeET;
    private Button postBtn;

    public board(){
        super(R.layout.activity_board);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set posts selected
        bottomNavigationView.setSelectedItemId(R.id.nav_board);

        //perform itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_board:
                        return true;

                    case R.id.nav_post:
                        startActivity(new Intent(getApplicationContext(),posts.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        typeET=findViewById(R.id.type_ET);
        postBtn=findViewById(R.id.post_Btn);
        context=this;
        createPostArrayList = new ArrayList<>();

        postBtn.setOnClickListener(view -> {
            addPost();
        });

        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        readPost();
    }

    private void readPost() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference postRef = firebaseDatabase.getReference().child("Users").child(currentUser.getUid()).child("Posts");

        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Datasnapshot contains {PostID1: {user, post}}
                createPost post;
                for(DataSnapshot content: snapshot.getChildren()){
                    post = content.getValue(createPost.class);
                    //Toast.makeText(noticeBoard.this, "Post: "+ post.getPost(), Toast.LENGTH_SHORT).show();
                    Log.i("my post", post.getPost());

                    createPostArrayList.add(post);
                }
                postAdapter = new postAdapter(createPostArrayList, context);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addPost() {
        String text = typeET.getText().toString();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = firebaseDatabase.getReference();
        DatabaseReference postRef = rootRef.child("Users").child(currentUser.getUid()).child("Posts");
        DatabaseReference newRef = postRef.push();

        createPost post = new createPost(newRef.child(currentUser.getUid()).getKey(), text);
        newRef.setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(board.this, "Post Submitted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(board.this, "Some error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}