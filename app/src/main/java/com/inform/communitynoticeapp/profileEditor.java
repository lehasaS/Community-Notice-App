package com.inform.communitynoticeapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class profileEditor extends AppCompatActivity implements View.OnClickListener {

    private EditText dispNameET, communityET;
    private ImageView profilePicIV;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private validateInput validate;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editor);
        validate = new validateInput(this);

        profilePicIV = findViewById(R.id.profile_pic_IV);
        dispNameET = findViewById(R.id.new_username_ET);
        communityET = findViewById(R.id.new_community_ET);
        Button uploadPicBtn = findViewById(R.id.upload_pic_Btn);
        TextView updateEmail = findViewById(R.id.updateEmail_TV);
        TextView updatePassword = findViewById(R.id.updatePassword_TV);
        Button saveBtn = findViewById(R.id.editProfile_Btn);

        showProfilePic();
        dispNameET.setText(firebase.getDisplayName());
        //communityET.setText(firebase.getCommunity());

        updateEmail.setOnClickListener(this);
        updatePassword.setOnClickListener(this);
        saveBtn.setOnClickListener(view -> handleSaveBtnClick());
        uploadPicBtn.setOnClickListener(view -> handlePicBtnClick());

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.updateEmail_TV:
                Intent updateEmail = new Intent(profileEditor.this, updateEmail.class);
                startActivity(updateEmail);

            case R.id.updatePassword_TV:
                Intent updatePassword = new Intent(profileEditor.this, updatePassword.class);
                startActivity(updatePassword);
                break;
        }

    }

    private void handleSaveBtnClick() {
        String dispName = dispNameET.getText().toString();
        String community = communityET.getText().toString();
        if (validate.checkDispName(dispName) && validate.checkCommunity(community)) {
            firebase.updateDispName(dispName);
            firebase.updateCommunity(community);
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        }
        Intent profile = new Intent(profileEditor.this, profile.class);
        startActivity(profile);
    }

    private void handlePicBtnClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent, 1);
        uploadPicActivityResultLauncher.launch(intent);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            Uri imageUri = data.getData();
            profilePicIV.setImageURI(imageUri);
            Toast.makeText(this, "Image URI: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
            firebase.updateDisplayPicture(imageUri);
        }
    }*/

    ActivityResultLauncher<Intent> uploadPicActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri imageUri = data.getData();
                        //firebase.updateDisplayPicture(imageUri);
                        setProfilePic(imageUri);
                        showProfilePic();
                    }
                }
            });

    /*public void openSomeActivityForResult() {
        Intent intent = new Intent(this, SomeActivity.class);
        uploadPicActivityResultLauncher.launch(intent);
    }*/

    public void setProfilePic(Uri photoUri) {
        //Toast.makeText(this, "Photo URI: " + photoUri.toString(), Toast.LENGTH_SHORT).show();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image...");
        progressDialog.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference pictureRef = firebase.getStorageRef().child("profilePics/" + randomKey);
        pictureRef.putFile(photoUri).addOnProgressListener(snapshot -> {
            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
            progressDialog.setMessage("Upload is " + (int) progressPercent + "% complete.");
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(this, "Upload failed. Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            pictureRef.getDownloadUrl().addOnSuccessListener(pictureUri -> {
                //Toast.makeText(this, "Picture URI: " + pictureUri.toString(), Toast.LENGTH_SHORT).show();
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(pictureUri)
                        .build();
                firebase.getUser().updateProfile(profileChangeRequest);
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
            progressDialog.dismiss();
            Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
        });
        //Toast.makeText(this, "Picture reference: " + pictureRef.toString(), Toast.LENGTH_SHORT).show();

    }
    public void showProfilePic() {
        String photoUrl = firebase.getDisplayPicture().toString();

        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference photoRef = storage.getReferenceFromUrl(photoUrl);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            profilePicIV.setImageBitmap(bmp);
        }).addOnFailureListener(exception ->
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show());
    }
}