package com.example.audiostream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewMusic extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MusicAdapter musicAdapter;
    ArrayList<uploadMusic> uploadMusics;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    RecyclerView.LayoutManager layoutManager;
    Button backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_music);

        backbutton  = findViewById(R.id.backbtn);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), homepage.class));
            }
        });

        fauth = FirebaseAuth.getInstance();
        fuser =fauth.getCurrentUser();

        recyclerView = findViewById(R.id.musicRecycler);

        String uid = fuser.getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("music");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploadMusics = new ArrayList<>();
        musicAdapter = new MusicAdapter(this,uploadMusics);

//        LinearLayoutManager horizontalLayout
//                = new LinearLayoutManager(
//                viewMusic.this,
//                LinearLayoutManager.HORIZONTAL,
//                false);
//        recyclerView.setLayoutManager(horizontalLayout);

        layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(musicAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    uploadMusic umusic = dataSnapshot.getValue(uploadMusic.class);
                    uploadMusics.add(umusic);
                }

                musicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}