package com.inform.communitynoticeapp;

import static com.inform.communitynoticeapp.R.id.nav_bookmarks;
import static com.inform.communitynoticeapp.R.id.nav_messageBoard;
import static com.inform.communitynoticeapp.R.id.nav_noticeBoard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class profile extends AppCompatActivity implements View.OnClickListener {

    private final dataBaseFirebase firebase=dataBaseFirebase.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context context;
    ImageView profilePicture;


    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView roleTV = findViewById(R.id.roleTV);
        profilePicture = findViewById(R.id.displayPicture_IV);
        TextView community = findViewById(R.id.communityTV);
        TextView welcomeMessageTV = findViewById(R.id.welcomeMessage_TV);
        TextView displayName = findViewById(R.id.usernameTV);
        Button logoutBtn = findViewById(R.id.logout_Btn);
        Button mangeRequests = findViewById(R.id.manage_request_btn);
        Button editProfile = findViewById(R.id.editProfile_Btn2);
        logoutBtn.setOnClickListener(this);
        mangeRequests.setOnClickListener(this);
        editProfile.setOnClickListener(this);
        displayName.setText(firebase.getUser().getDisplayName());
        welcomeMessageTV.setText(getString(R.string.Greeting)+ firebase.getUser().getDisplayName()+"!");
        context=this;

        firebase.getUserDetailsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails details = snapshot.getValue(userDetails.class);
                assert details != null;
                roleTV.setText(details.getRole());
                community.setText(details.getCommunity());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showProfilePic();

        //initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set posts selected
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        //perform itemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch (item.getItemId())
            {
                case nav_noticeBoard:
                    startActivity(new Intent(getApplicationContext(),noticeBoard.class));
                    overridePendingTransition(0,0);
                    return true;

                case nav_messageBoard:
                    startActivity(new Intent(getApplicationContext(),messageBoard.class));
                    overridePendingTransition(0,0);
                    return true;

                case nav_bookmarks:
                    startActivity(new Intent(getApplicationContext(),bookmarks.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.nav_profile:
                    return true;
            }
            return false;
        });

    }


    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.logout_Btn:
                showLogoutDialog();
                break;
            case R.id.editProfile_Btn2:
                Intent editProfile = new Intent(profile.this, profileEditor.class);
                startActivity(editProfile);
                break;
            case R.id.manage_request_btn:
                Intent manage_Request = new Intent(profile.this, manageRequest.class);
                startActivity(manage_Request);
                break;

        }
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            firebase.getUserAuth().signOut();
            ((Activity) context).finish();
            Intent login = new Intent(profile.this, LogIn.class);
            startActivity(login);

        });
        builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showProfilePic() {
        String photoUrl = firebase.getDisplayPicture().toString();

        StorageReference photoRef = storage.getReferenceFromUrl(photoUrl);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            profilePicture.setImageBitmap(bmp);
        }).addOnFailureListener(exception ->
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show());
    }
}