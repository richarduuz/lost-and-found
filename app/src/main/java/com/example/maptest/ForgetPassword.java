package com.example.maptest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;


public class ForgetPassword extends AppCompatActivity {

    private EditText email;
    private EditText old_password;
    private EditText new_password;
    private EditText confirm_password;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
        email = findViewById(R.id.email_forget);
        old_password = findViewById(R.id.old_forget);
        new_password = findViewById(R.id.new_forget);
        confirm_password = findViewById(R.id.confirm_forget);
        reset = findViewById(R.id.reset_forget);
        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userOldPwd = old_password.getText().toString().trim();
                String userNewPwd = new_password.getText().toString().trim();
                String userConfirmPwd = confirm_password.getText().toString().trim();
                forgetPwd(userEmail, userOldPwd, userNewPwd, userConfirmPwd);
            }
        });
    }

    public void forgetPwd (String email, String oldPwd, String newPwd, String confirmPwd){
        if (TextUtils.isEmpty(email)){
            Toast.makeText(ForgetPassword.this, "Please enter email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(oldPwd)){
            Toast.makeText(ForgetPassword.this, "Please enter valid old password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(newPwd)){
            Toast.makeText(ForgetPassword.this, "Please enter valid new password", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmPwd)){
            Toast.makeText(ForgetPassword.this, "Please confirm your new password", Toast.LENGTH_SHORT).show();
        }
        else if (!newPwd.equals(confirmPwd)){
            Toast.makeText(ForgetPassword.this, "The two new passwords are not equal", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, oldPwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = firebaseAuth.getCurrentUser();
                                user.updatePassword(newPwd)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ForgetPassword.this, "User password updated.", Toast.LENGTH_SHORT).show();
                                                firebaseAuth.signOut();
                                                Intent i = new Intent();
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(ForgetPassword.this, "update error ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            } else {
                                Toast.makeText(ForgetPassword.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }

    }

}
