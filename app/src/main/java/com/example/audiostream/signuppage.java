package com.example.audiostream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
//import com.google.firebase.iid.FirebaseInstanceIdc;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signuppage extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText username, email,password;
    Button signupbtn;
    FirebaseAuth firebaseAuth;
    TextView login_redirect;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid;
    InstallationTokenResult devicetoken;
    String s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        login_redirect = findViewById(R.id.clickablelogintext);
        signupbtn = findViewById(R.id.btnsignup);
        username = findViewById(R.id.signupusername);
        email = findViewById(R.id.signupemail);
        password = findViewById(R.id.signuppassword);
        firebaseAuth = FirebaseAuth.getInstance();
//        devicetoken=FirebaseInstanceId.getInstance().getToken();

        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                if(task.isComplete()){
                   devicetoken = task.getResult();
                   s = devicetoken.getToken();
                }
            }
        });

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), homepage.class));
            finish();
        }



        login_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), loginpage.class));
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pEmail = email.getText().toString().trim();
                final String pUsername = username.getText().toString().trim();
                String pPassword = password.getText().toString().trim();

                String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(pEmail);


                //validating email
                if (TextUtils.isEmpty(pEmail)) {
                    email.setError("Email Required");
                } else if (!matcher.matches()) {
                    email.setError("Enter a valid Email");
                    return;
                }

                //validating password
                if (TextUtils.isEmpty(pPassword)) {
                    password.setError("Password Required");
                    return;
                }
                if (pPassword.length() < 6) {
                    password.setError("Password length must be greater than 6");
                }
                

                firebaseAuth.createUserWithEmailAndPassword(pEmail, pPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        uid = firebaseAuth.getCurrentUser().getUid();

                        //storing user information
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //Storing user's data, using hashmap in realtime database
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", pEmail);
                            hashMap.put("username", pUsername);
                            hashMap.put("uid", uid);
                            hashMap.put("password", pPassword);
                            hashMap.put("devicetoken",s);
                            hashMap.put("image", "");

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference("users").child("profile");
                            databaseReference.child(uid).setValue(hashMap);
                            startActivity(new Intent(getApplicationContext(), homepage.class));
                        }
                        else{
                            Toast.makeText(signuppage.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });






                    }
                });
            }
        }


