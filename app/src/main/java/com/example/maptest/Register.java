package com.example.maptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;

import java.util.Timer;
import java.util.TimerTask;

public class Register extends AppCompatActivity {


    private EditText email;
    private EditText password;
    private Button register;
    private FirebaseAuth firebaseAuth;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_register);
        password = findViewById(R.id.email_register);
        register = findViewById(R.id.register);
        login =  findViewById(R.id.textView1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = email.getText().toString().trim();
                String userPassword = email.getText().toString().trim();

                userRegister(userEmail, userPassword);


            }
        });

    }

    private void userRegister(String email, String password){
        if (TextUtils.isEmpty(email)){
            Toast.makeText(Register.this, "Please enter email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(Register.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Register.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                // Go to the login page in 3 seconds
                                Timer timer = new Timer();
                                TimerTask task1 = new TimerTask() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(Register.this, Login.class);
                                        startActivity(intent);
                                        Register.this.finish();
                                    }
                                };
                                timer.schedule(task1, 3000);

                            }
                            else{
                                Toast.makeText(Register.this, "Register Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
