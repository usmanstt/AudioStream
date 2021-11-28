package com.example.audiostream;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.audiostream.hometrack.PodListAdapter;
import com.example.audiostream.hometrack.TrackListAdapter;
import com.example.jean.jcplayer.model.JcAudio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class homepage extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button profile,articles,music,podcast;
    DatabaseReference mReference,mReferencePodcast;
    SharedPreferences sharedPreferences;
    uploadMusic model;
    FirebaseUser user;

    ArrayList<uploadMusic> mMusicList;
    ArrayList<uploadPodcasts> mPodList;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    TrackListAdapter trackListAdapter;
    PodListAdapter podcastAdapter;
    RecyclerView trackRecycler,podCastRecycler;
    ArrayList<JcAudio> jcAudioArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        user=FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference().child("users").child("music");
        mReferencePodcast = FirebaseDatabase.getInstance().getReference().child("users").child("podcasts");
        mMusicList = new ArrayList<>();
        mPodList = new ArrayList<>();

        profile = findViewById(R.id.profilebtn);
        articles = findViewById(R.id.newsbtn);
        music = findViewById(R.id.musicbtn);
        podcast = findViewById(R.id.podcastbtn);
        trackRecycler = findViewById(R.id.trackRecycler);
        podCastRecycler = findViewById(R.id.podCastRecycler);
        bottomListener();
        trackListView();

    }

    private void trackListView() {
        mReferencePodcast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPodList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    uploadPodcasts model = ds.getValue(uploadPodcasts.class);
                    mPodList.add(model);
                }

                Collections.sort(mPodList, new Comparator<uploadPodcasts>() {
                    @Override
                    public int compare(uploadPodcasts o1, uploadPodcasts o2) {
                        return o1.getLike()-o2.getLike();
                    }
                });
                addList();
                podcastAdapter = new PodListAdapter(mPodList,homepage.this,jcAudioArrayList);
                podCastRecycler.setLayoutManager(new LinearLayoutManager(homepage.this, LinearLayoutManager.HORIZONTAL, true));
                podCastRecycler.setAdapter(podcastAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void bottomListener(){
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMusicList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    model = ds.getValue(uploadMusic.class);
                    mMusicList.add(model);
                }
                addList();
                Collections.sort(mMusicList, new Comparator<uploadMusic>() {
                    @Override
                    public int compare(uploadMusic o1, uploadMusic o2) {
                        return o1.getLikes()-o2.getLikes();
                    }
                });
                /*for(int i=0;i<mMusicList.size();i++)
                {
                    jcAudios.add(JcAudio.createFromURL(mMusicList.get(i).getName(),mMusicList.get(i).getUrl()));
                }*/
                trackListAdapter = new TrackListAdapter(mMusicList,homepage.this,jcAudios);
                trackRecycler.setLayoutManager(new LinearLayoutManager(homepage.this, LinearLayoutManager.HORIZONTAL, true));
                trackRecycler.setAdapter(trackListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homepage.this, viewArticles.class));
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homepage.this, viewMusic.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homepage.this, profile.class));
            }
        });
        podcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homepage.this, viewPodcasts.class));
            }
        });
    }


    public void addList()
    {
        sharedPreferences = getSharedPreferences("CityList", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(mMusicList);
        editor.putString("courses", json);

        String jsonpod = gson.toJson(mPodList);
        editor.putString("mPodList", jsonpod);
        editor.apply();

    }
}