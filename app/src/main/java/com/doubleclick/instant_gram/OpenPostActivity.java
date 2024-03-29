package com.doubleclick.instant_gram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class OpenPostActivity extends AppCompatActivity {

    private ImageView image_clicked;
    TextView description_clicked;
    //private Button edit_clicked,delete_clicked;
    private String PostKey, Current_User_Id, Database_User_Id, description, postimage;
    private DatabaseReference ClickPostRef;
    private FirebaseAuth mAuth;
    private ProgressBar post_progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //MAKING IT FULL SCREEN......
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //SHOWING LAYOUT...
        setContentView(R.layout.activity_open_post);

        //Initialising Variables.....
        mAuth = FirebaseAuth.getInstance();
        Current_User_Id = mAuth.getCurrentUser().getUid();
        PostKey = getIntent().getExtras().getString("PostKey");
        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);

        image_clicked = findViewById(R.id.image_clicked);
        description_clicked = findViewById(R.id.description_clicked);
        //edit_clicked = findViewById(R.id.edit_clicked);
        //delete_clicked = findViewById(R.id.delete_clicked);

        //edit_clicked.setVisibility(View.INVISIBLE);
        //delete_clicked.setVisibility(View.INVISIBLE);

        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    postimage = dataSnapshot.child("postimage").getValue().toString();
                    Database_User_Id = dataSnapshot.child("uid").getValue().toString();
                    description = dataSnapshot.child("description").getValue().toString();

                    //Adding Image from Picasso and calling callback Listener who hides the progressbar when image is loaded....
                    Picasso.get().load(postimage).into(image_clicked, new Callback() {
                        @Override
                        public void onSuccess() {
                            post_progress_bar = findViewById(R.id.open_post_progress);
                            post_progress_bar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                    description_clicked.setText(description);
                    description_clicked.setTextSize(14);

                    if (Current_User_Id.equals(Database_User_Id)) {
                        //edit_clicked.setVisibility(View.VISIBLE);
                        //delete_clicked.setVisibility(View.VISIBLE);

                        if (!postimage.equals("none")) {

                            //  edit_clicked.setVisibility(View.VISIBLE);
                            //delete_clicked.setVisibility(View.VISIBLE);

                        }

                        if (postimage.equals("none")) {
                            image_clicked.setVisibility(View.INVISIBLE);
                            description_clicked.setText(description);
                            description_clicked.setTextSize(25);

                        }

                    }
                } else {
                    Toast.makeText(OpenPostActivity.this, "Nothing to Delete", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* delete_clicked.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                ClickPostRef.removeValue();
                SendUserToMainActivity();
            }
        });
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void SendUserToMainActivity() {

        Intent homeIntent = new Intent(OpenPostActivity.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }

}