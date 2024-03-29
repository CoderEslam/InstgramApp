package com.doubleclick.instant_gram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText ForgotEmail;
    private Button ForgotButton;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Initialising Firebase Instance....
        mAuth = FirebaseAuth.getInstance();
        //Defining Views
        ForgotEmail = findViewById(R.id.forgot_email);
        ForgotButton = findViewById(R.id.forgot_button);

        ForgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserEmail =ForgotEmail.getText().toString().trim();

                //Hiding Keyboard when user entered forgot button.
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                progressDialog = new ProgressDialog(ForgotPassword.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Wait a second");
                progressDialog.show();
                //Checking whether the Email Field is not Empty!
                if(!TextUtils.isEmpty(UserEmail))
                {
                    mAuth.sendPasswordResetEmail(UserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPassword.this,"Success",Toast.LENGTH_SHORT).show();

                                progressDialog.dismiss();

                                //Setting Dialog Box...
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ForgotPassword.this, R.style.AlertDialogCustom));
                                builder.setMessage("Reset Link has been Sent to your given EmailId");

                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Sending User to Login Activity...
                                        Intent loginIntent = new Intent(ForgotPassword.this,LoginActivity.class);
                                        startActivity(loginIntent);
                                        dialog.cancel();
                                    }
                                });

                                Dialog dialog = builder.create();
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                                dialog.show();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(ForgotPassword.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }

                    });



                }
            }
        });
    }
}