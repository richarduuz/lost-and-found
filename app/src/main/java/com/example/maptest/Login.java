package com.example.maptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText passsword;
    private Button login;
    private TextView register;
    private TextView forget;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
//        Intent input_intent = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.username);
        passsword = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forget = findViewById(R.id.forget_textView);



        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }




        register = findViewById(R.id.textView);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_email = email.getText().toString().trim();
                String user_password = passsword.getText().toString().trim();
                userLogin(user_email, user_password);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin(String email, String password){
        if (TextUtils.isEmpty(email)){
            Toast.makeText(Login.this, "Please enter email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(Login.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Log.d("forget", email + "+" + password);
                            // Go to the home page in 3 seconds
                            Timer timer = new Timer();
                            TimerTask task1 = new TimerTask() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    Login.this.finish();
                                }
                            };
                            timer.schedule(task1, 3000);
                        }
                        else{
                            Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
