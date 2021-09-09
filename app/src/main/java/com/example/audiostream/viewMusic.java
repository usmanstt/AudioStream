package com.example.audiostream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class viewMusic extends AppCompatActivity {

//    RecyclerView recyclerView;
    DatabaseReference databaseReference;
//    MusicAdapter musicAdapter;
    ArrayList<uploadMusic> uploadMusics;
    StorageReference storageReference;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    RecyclerView.LayoutManager layoutManager;
    Button backbutton;
    ListView listView;
    JcPlayerView jcplayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_music);

        backbutton  = findViewById(R.id.backbtn);
        listView = findViewById(R.id.list_music);
        jcplayerView = (JcPlayerView) findViewById(R.id.jcplayer);
        progressBar = (ProgressBar)findViewById(R.id.pBar);


        uploadMusics = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);



        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), homepage.class));
            }
        });

        fauth = FirebaseAuth.getInstance();
        fuser =fauth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        viewAllMusic();

//        recyclerView = findViewById(R.id.musicRecycler);

//        String uid = fuser.getUid().toString();
//        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("music");
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploadMusics = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                uploadMusic uploadMusic = uploadMusics.get(position);
//                Intent intent = new Intent(viewMusic.this, playsongs.class);
//                intent.putExtra("song name", uploadMusic.getName());
//                intent.putExtra("song url", uploadMusic.getUrl());
//                intent.putExtra("song position", position);
//                startActivity(intent);
                jcplayerView.playAudio(jcAudios.get(position));
                jcplayerView.setVisibility(View.VISIBLE);
                jcplayerView.createNotification();
            }
        });



//        musicAdapter = new MusicAdapter(this,uploadMusics, this);

//        LinearLayoutManager horizontalLayout
//                = new LinearLayoutManager(
//                viewMusic.this,
//                LinearLayoutManager.HORIZONTAL,
//                false);
//        recyclerView.setLayoutManager(horizontalLayout);

//        layoutManager = new GridLayoutManager(this,3);
//        recyclerView.setLayoutManager(layoutManager);
//
//        recyclerView.setNestedScrollingEnabled(false);
//
//        recyclerView.setAdapter(musicAdapter);

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    uploadMusic umusic = dataSnapshot.getValue(uploadMusic.class);
//                    uploadMusics.add(umusic);
//                }
//
//                musicAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    private void viewAllMusic() {
        String uid = fuser.getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("music");
        StorageReference musicfiles = storageReference.child("users/music");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    uploadMusic uploadMusic = ds.getValue(com.example.audiostream.uploadMusic.class);
                    uploadMusics.add(uploadMusic);
                    jcAudios.add(JcAudio.createFromURL(uploadMusic.getName(),uploadMusic.getUrl()));
            }


                String[] uploads = new String[uploadMusics.size()];

                for(int i=0; i<uploads.length; i++){
                    uploads[i] = uploadMusics.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,uploads);

                jcplayerView.initPlaylist(jcAudios, null);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}