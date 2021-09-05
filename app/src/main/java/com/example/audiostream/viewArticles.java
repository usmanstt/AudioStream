package com.example.audiostream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class viewArticles extends AppCompatActivity {
    ListView listView;
    DatabaseReference databaseReference;
    List<uploadArticles> uArticles;
    StorageReference storageReference;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    Button backbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_articles);

        listView = findViewById(R.id.list_items);
        backbutton = findViewById(R.id.backbtn);


        uArticles = new ArrayList<>();

        fauth = FirebaseAuth.getInstance();
        fuser = fauth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        viewAllArticles();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                uploadArticles uploadArticles = uArticles.get(position);
                Intent intent = new Intent();
                intent.setData(Uri.parse(uploadArticles.getUrl()));
                startActivity(intent);
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), homepage.class));
            }
        });

    }

    private void viewAllArticles() {
        String uid = fuser.getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("articles");
        StorageReference articleFiles = storageReference.child("users/articles");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    uploadArticles uploadArticles = postSnapshot.getValue(com.example.audiostream.uploadArticles.class);
                    uArticles.add(uploadArticles);


                }
                String[] uploads = new String[uArticles.size()];

                for(int i=0; i<uploads.length; i++){
                    uploads[i] = uArticles.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,uploads);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}