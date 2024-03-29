package com.doubleclick.instant_gram;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private Button setup;
    private EditText userName,fullName,countryName;
    private CircleImageView circleImage;
    FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private String CurrentUserId;
    private ProgressDialog loadingBar;
    final static int GalleryPic=1;
    private StorageReference UserProfileImageRef;
    private ImageButton AddPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        CurrentUserId = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(CurrentUserId);
        userName = findViewById(R.id.username);
        fullName = findViewById(R.id.fullname);
        countryName = findViewById(R.id.country);
        circleImage = findViewById(R.id.circleimage);
        AddPost = findViewById(R.id.add_photo);
        setup = findViewById(R.id.setup);
        loadingBar =new ProgressDialog(this);

        setup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                String username = userName.getText().toString().trim();
                String fullname = fullName.getText().toString().trim();
                String countryname = countryName.getText().toString().trim();

                if(TextUtils.isEmpty(username))
                {
                    Toast.makeText(SetupActivity.this,"Provide Username for it",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(fullname))
                {
                    Toast.makeText(SetupActivity.this,"Provide FullName for it",Toast.LENGTH_SHORT);
                }
                else if(TextUtils.isEmpty(countryname))
                {
                    Toast.makeText(SetupActivity.this,"Provide Country you're Living",Toast.LENGTH_SHORT);
                }
                else
                {
                    loadingBar.setTitle("Saving Information");
                    loadingBar.setMessage("Please wait until we save your Information to the Database");
                    loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    HashMap userMap =new HashMap();
                    userMap.put("username",username);
                    userMap.put("fullname",fullname);
                    userMap.put("country",countryname);
                    userMap.put("status","Hey there, I am using Big-Inch");
                    userMap.put("gender","none");
                    userMap.put("dob","none");
                    userMap.put("realtionshipstatus","none");

                    //ADDING TEMPORARY PROFILE TO THE USER ACCOUNT.....
                    //userMap.put("profileImage","https://firebasestorage.googleapis.com/v0/b/instantgram-5c343.appspot.com/o/Profile%20Images%2Fprofile.png?alt=media&token=360aa709-b18d-443e-96ce-e0704bf97e62");

                    //ADDING DATA TO THE CURRENT USER ...
                    UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {   SendUserToMainActivity();
                                loadingBar.dismiss();
                                Toast.makeText(SetupActivity.this,"Your Account is Created Successfully",Toast.LENGTH_LONG);

                            }
                            else {
                                String message = task.getException().toString();
                                loadingBar.dismiss();
                                Toast.makeText(SetupActivity.this,message,Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });

        //This click will redirect to Gallery where you can find Images only...
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPic); //User will pick the Picture...
            }
        });

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("profileImage")) {
                        String image = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.get()
                                .load(image)
                                .placeholder(R.drawable.profile)
                                .into(circleImage);
                    }
                }
                else
                {
                    Toast.makeText(SetupActivity.this,"First Select the Image from Gallery",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //This click will redirect to Gallery where you can find Images only...
        AddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPic); //User will pick the Picture...
            }
        });

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("profileImage")) {
                        String image = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.get()
                                .load(image)
                                .placeholder(R.drawable.profile)
                                .into(circleImage);
                    }
                }
                else
                {
                    Toast.makeText(SetupActivity.this,"First Select the Image from Gallery",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //This method onActivityResult checks whether the image is right.....
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        //this checks the checked imaged,so there should be no null image...
        if(requestCode== GalleryPic && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();             //here we getting the image.....in ImageUri

            //When the user Select the image he will be redirected to the Image Cropping Activity...
            CropImage.activity(ImageUri)
                    .setAspectRatio(1,1)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .start(this);
        }
        //THIS CHECKS WHETHER WE SELECT THE CROP OPTION...
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            //HERE WE GETTING CROPPED IMAGE...
            CropImage.ActivityResult result= CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK) //IF CROPPING SUCCESSFULL...
            {
                loadingBar.setTitle("Saving Information");
                loadingBar.setMessage("Please wait until we update your Profile Image");
                loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                //THIS OBJECTS CONTAINS THE CROPPED IMAGE....
                Uri resultUri =result.getUri();

                //WE STORING THE STORAGE REFERENCE AS A USERID.JPG.....
                StorageReference filePath = UserProfileImageRef.child(CurrentUserId + ".jpg");

                StorageTask task = filePath.putFile(resultUri);
                task.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task task) throws Exception {
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SetupActivity.this,"Profile Image Stored to Database Successfully",Toast.LENGTH_SHORT).show();
                            Uri uri = task.getResult();
                            String downloadUrl = uri.toString();
                            //FINALLY STORING THE IMAGE IN DATABASE IN FIREBASE STORAGE...
//                            final String downloadUrl = filePath.getDownloadUrl().toString();// task.getResult().getDownloadUrl().toString();
                            UsersRef.child("profileImage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        //REDIRECTING TO SETUP ACTIVITY FROM CROPPING ACTIVITY....
                                        Intent selfIntent = new Intent(SetupActivity.this,SetupActivity.class);
                                        startActivity(selfIntent);
                                        Toast.makeText(SetupActivity.this, "Profile Image is Stored Successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else{
                                        String message =task.getException().getMessage();
                                        Toast.makeText(SetupActivity.this, "Error Occurred :" +message, Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                        }else {

                        }
                    }
                });

                /*//HERE STORING THE FILE IN filePath OBJECT..
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {

                        }
                    }
                });*/

            }

            else{
                //IF THE IMAGE IS UNABLE TO CROP...
                Toast.makeText(SetupActivity.this, "Error Occurred : Image can't be Cropped,Try Again." , Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }

        }
        //else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
    }



    private void SendUserToMainActivity() {
        Intent homeIntent = new Intent(SetupActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();

    }
}