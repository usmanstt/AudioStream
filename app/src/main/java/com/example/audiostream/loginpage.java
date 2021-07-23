package com.example.audiostream;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginpage extends AppCompatActivity {
    TextView signupText,googleLoginText;
    EditText emaillogin, passwordlogin;
    Button loginbtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        signupText = findViewById(R.id.clickablesignuptext);
        googleLoginText = findViewById(R.id.clickablegmailtext);
        emaillogin = findViewById(R.id.loginemail);
        passwordlogin = findViewById(R.id.loginpassword);
        loginbtn = findViewById(R.id.btnlogin);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), homepage.class));
            finish();
        }

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), signuppage.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pEmail = emaillogin.getText().toString().trim();
                String pPassword = passwordlogin.getText().toString().trim();

                String regex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(pEmail);


                //validating email
                if(TextUtils.isEmpty(pEmail)){
                    emaillogin.setError("Email Required");
                }
                else if(!matcher.matches()){
                    emaillogin.setError("Enter a valid Email");
                    return;
                }

                //validating password
                if(TextUtils.isEmpty(pPassword)){
                    passwordlogin.setError("Password Required");
                    return;
                }
                if (pPassword.length() < 6){
                    passwordlogin.setError("Password length must be greater than 6");
                }

                //user authentication
                firebaseAuth.signInWithEmailAndPassword(pEmail, pPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(loginpage.this, "User Logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), homepage.class));
                        }
                        else{
                            Toast.makeText(loginpage.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });




    }
}