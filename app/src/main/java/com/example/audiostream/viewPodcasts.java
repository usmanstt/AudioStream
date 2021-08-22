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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewPodcasts extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    PodcastAdapter podcastAdapter;
    ArrayList<uploadPodcasts> uploadPodcasts;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    RecyclerView.LayoutManager layoutManager;
    Button backbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_podcasts);

        fauth = FirebaseAuth.getInstance();
        fuser =fauth.getCurrentUser();

        backbutton = findViewById(R.id.backbtn);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewPodcasts.this, homepage.class));
            }
        });


        recyclerView = findViewById(R.id.podcastRecycler);

        String uid = fuser.getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("podcasts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploadPodcasts = new ArrayList<>();
        podcastAdapter = new PodcastAdapter(this, uploadPodcasts);

        layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(podcastAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    uploadPodcasts upodcast = dataSnapshot.getValue(uploadPodcasts.class);
                    uploadPodcasts.add(upodcast);
                }

                podcastAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}