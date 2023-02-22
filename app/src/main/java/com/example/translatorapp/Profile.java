package com.example.translatorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class Profile extends AppCompatActivity {

    private Button logOutBtn;
    private Button uploadBtn;
    private ImageView profilePhoto;
    private Uri imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        logOutBtn = findViewById(R.id.logOut);

        //Change Action Bar
        ChangeActionBar("Settings");

        //Navigation View
        BottomNavigationView nav = findViewById(R.id.ProfileNavigationView);

        //Profile Photo
        profilePhoto = findViewById(R.id.profileImg);

        //Upload Photo Button
        uploadBtn = findViewById(R.id.uploadImg);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImg();
            }
        });

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.ContactsNav) {
                    startActivity(new Intent(Profile.this, Contacts.class));
                    finish();
                }
                else if (item.getItemId()==R.id.MessagesNav) {
                    startActivity(new Intent(Profile.this, AllMessages.class));
                    finish();
                }
                return false;
            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sign Out
                FirebaseAuth.getInstance().signOut();

                //Changes Screen and make sure User can't go back
                startActivity(new Intent(Profile.this, Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,1);
            }
        });
    }

    private void UploadImg() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString()).putFile(imagePath)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    //Download Url
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                updateProfilePicture(task.getResult().toString());
                            }
                        }
                    });
                    Toast.makeText(Profile.this, "Image has been Uploaded.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Profile.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = 100.0 + snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
    }

    private void updateProfilePicture(String url) {

        FirebaseDatabase.getInstance().getReference("user/" +
                FirebaseAuth.getInstance().getCurrentUser().getUid() + "/profilePhoto").setValue(url);
    }

    void ChangeActionBar(String title){
        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#2699FB"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            imagePath = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        profilePhoto.setImageBitmap(bitmap);

    }
}