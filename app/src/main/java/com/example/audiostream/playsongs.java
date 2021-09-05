package com.example.audiostream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class playsongs extends AppCompatActivity {

    TextView songName;
    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    DatabaseReference databaseReference;
    ArrayList<uploadMusic> uploadMusics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playsongs);

        songName = findViewById(R.id.songname);
        jcPlayerView = (JcPlayerView) findViewById(R.id.jcplayer);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        int songPosition;
        String song = (String) b.get("song name");
        songName.setText(song);
        String url = (String) b.get("song url");
        songPosition = (Integer) b.get("song position");

        jcAudios.add(JcAudio.createFromURL(song, url));
        jcPlayerView.initPlaylist(jcAudios, null);
        jcPlayerView.playAudio(jcAudios.get(songPosition));

    }
}