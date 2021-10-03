package com.inform.communitynoticeapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class posts extends AppCompatActivity implements View.OnClickListener {

    private EditText typeET;
    private final dataBaseFirebase firebase = dataBaseFirebase.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private ImageView profilePicIV, postPicIV;
    private Uri postPicUri = null;
    private LinearLayout linearLayout;
    private TextInputLayout communityTI;
    private AutoCompleteTextView textView;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        TextView displayNameTV = findViewById(R.id.displayName_TV);
        displayNameTV.setText(firebase.getUser().getDisplayName());
        communityTI = findViewById(R.id.commGroup_TI);
        typeET=findViewById(R.id.type_ET);
        Button postBtn = findViewById(R.id.post_Btn);
        ImageView takePhoto = findViewById(R.id.takePhoto_IV);
        ImageView galleryPhoto = findViewById(R.id.addPhoto_IV);
        ImageView removePhotoIV = findViewById(R.id.removePhoto_IV);
        ImageView category = findViewById(R.id.categoryIV);
        profilePicIV = findViewById(R.id.displayPicture_IV);
        postPicIV = findViewById(R.id.picPreview_IV);
        linearLayout= findViewById(R.id.tagsLL);
        textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteCommunity);

        showProfilePic();
        takePhoto.setOnClickListener(this);
        galleryPhoto.setOnClickListener(this);
        removePhotoIV.setOnClickListener(this);
        category.setOnClickListener(this);

        postBtn.setOnClickListener(view -> checkRole());

        readCommunities();

    }

    private void checkRole(){
        firebase.getUserDetailsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails details = snapshot.getValue(userDetails.class);
                assert details != null;
                if(details.getRole().equals("Community Member")){
                    //tell user you cant post here
                    //askUserToPost();
                    postToMessageBoard();
                }
                else{
                    askWhereToPost();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(posts.this, "Some error occurred: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.takePhoto_IV:
                handleTakingPhoto();
                break;
            case R.id.addPhoto_IV:
                handleAddingPhoto();
                break;
            case R.id.removePhoto_IV:
                handleRemovingPhoto();
                break;
            case R.id.categoryIV:
                Intent choose= new Intent(posts.this, hashtag.class);
                chooseHashtags.launch(choose);
                break;
        }
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @SuppressWarnings("unchecked")
    ActivityResultLauncher<Intent> chooseHashtags = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK) {
                    Map<String, Object> hashtags = (HashMap<String, Object>) Objects.requireNonNull(result.getData()).getSerializableExtra("extra");
                    for(Map.Entry<String,Object> entry:hashtags.entrySet()){
                        List<String> tags = (List<String>) entry.getValue();
                        for(String hashTags: tags){
                            TextView textView = new TextView(this);
                            textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
                            textView.setText("#"+hashTags);
                            linearLayout.addView(textView);
                        }
                    }
                }
            });


    public void showProfilePic() {
        String photoUrl = firebase.getDisplayPicture().toString();

        StorageReference photoRef = storage.getReferenceFromUrl(photoUrl);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            profilePicIV.setImageBitmap(bmp);
        }).addOnFailureListener(exception ->
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show());
    }

    private void askWhereToPost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Post To?");
        builder.setPositiveButton("Message Board", (dialog, id) -> postToMessageBoard());
        builder.setNegativeButton("Notice Board", (dialog, id) -> postToNoticeBoard());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void postToMessageBoard(String pictureURI) {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM 'at' HH:mm");
        String dateNow = dateFormat.format(date);
        String text = typeET.getText().toString();
        ArrayList<String> tags = getHashtags();

        firebase.addPostToMessageBoardNode(text, dateNow, pictureURI, tags, Objects.requireNonNull(communityTI.getEditText()).getText().toString()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(posts.this, "Post Submitted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(posts.this, "Some error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
            }
            typeET.setText("");
        });
        tags.clear();
        goToMessageBoard();
    }

    private void goToMessageBoard() {
        Intent goToMessageBoard = new Intent(posts.this, messageBoard.class);
        startActivity(goToMessageBoard);
    }

    private void postToMessageBoard() {
        if (postPicUri!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading image...");
            progressDialog.show();

            final String randomKey = UUID.randomUUID().toString();
            StorageReference pictureRef = firebase.getStorageRef().child("images/" + randomKey);
            pictureRef.putFile(postPicUri).addOnProgressListener(snapshot -> {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Upload is " + (int) progressPercent + "% complete.");
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(this, "Upload failed. Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                pictureRef.getDownloadUrl().addOnSuccessListener(pictureUri -> {
                    progressDialog.dismiss();
                    postToMessageBoard(pictureUri.toString());
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            });
        } else {
            postToMessageBoard("");
        }
    }

    private ArrayList<String> getHashtags(){
        ArrayList<String> hash = new ArrayList<>();
        int childCount = linearLayout.getChildCount();
        if(childCount!=0){
            for(int i=0; i<childCount;i++){
                TextView view = (TextView) linearLayout.getChildAt(i);
                hash.add(view.getText().toString().substring(1));
            }
        }
        return hash;
    }

    private void postToNoticeBoard(String pictureURI) {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM 'at' HH:mm");
        String dateNow = dateFormat.format(date);
        String text = typeET.getText().toString();
        ArrayList<String> tags = getHashtags();

        firebase.addPostToNoticeBoardNode(text, dateNow, pictureURI, tags, Objects.requireNonNull(communityTI.getEditText()).getText().toString()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(posts.this, "Post Submitted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(posts.this, "Some error occurred: "+task.getException(), Toast.LENGTH_SHORT).show();
            }
            typeET.setText("");
        });
        tags.clear();
       goToNoticeBoard();
    }

    private void goToNoticeBoard() {
        Intent goToNoticeBoard = new Intent(posts.this, noticeBoard.class);
        startActivity(goToNoticeBoard);
    }

    private void postToNoticeBoard() {
        if (postPicUri!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading image...");
            progressDialog.show();

            final String randomKey = UUID.randomUUID().toString();
            StorageReference pictureRef = firebase.getStorageRef().child("images/" + randomKey);
            pictureRef.putFile(postPicUri).addOnProgressListener(snapshot -> {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Upload is " + (int) progressPercent + "% complete.");
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(this, "Upload failed. Error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {
                pictureRef.getDownloadUrl().addOnSuccessListener(pictureUri -> {
                    progressDialog.dismiss();
                    postToNoticeBoard(pictureUri.toString());
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            });
        } else {
            postToNoticeBoard("");
        }
    }

    private void handleTakingPhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.CAMERA
            }, 100);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> takePicActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                        Uri imageUri = getImageUri(bitmap);
                        postPicIV.setImageURI(imageUri);
                        postPicUri = imageUri;
                        //postPicIV.getLayoutParams().height = 30;
                    }
                }
            });

    //source: https://colinyeoh.wordpress.com/2012/05/18/android-getting-image-uri-from-bitmap/
    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void handleAddingPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        uploadPicActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> uploadPicActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    Uri imageUri = data.getData();
                    postPicIV.setImageURI(imageUri);
                    postPicUri = imageUri;
                }
            });

    private void handleRemovingPhoto() {
        postPicIV.setImageResource(R.drawable.ic_baseline_image_24);
        postPicUri = null;
    }

    private void readCommunities() {
        firebase.getUserCommunities().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> communitiesList = new ArrayList<String>();
                String[] communitiesArray;
                Community aCommunity;
                for (DataSnapshot content : snapshot.getChildren()) {
                    aCommunity = content.getValue(Community.class);
                    assert aCommunity != null;
                    communitiesList.add(aCommunity.getName());
                }

                communitiesArray = new String[communitiesList.size()];
                communitiesArray = communitiesList.toArray(communitiesArray);
                ArrayAdapter<String> communities = new ArrayAdapter<>(posts.this, android.R.layout.simple_dropdown_item_1line, communitiesArray);
                textView.setAdapter(communities);
                Objects.requireNonNull(communityTI.getEditText()).setText(communitiesArray[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(posts.this, "Error occurred: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
