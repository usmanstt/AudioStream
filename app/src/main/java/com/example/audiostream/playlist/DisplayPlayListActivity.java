package com.example.audiostream.playlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audiostream.R;
import com.example.audiostream.uploadMusic;
import com.example.jean.jcplayer.model.JcAudio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayPlayListActivity extends AppCompatActivity {

    private DatabaseReference mReference;
    private ArrayList<uploadMusic> createLit = new ArrayList<>();

    TextView tvPlDisl;
    private RecyclerView displayPlaylist;
    DisplayListAdapter adapter;
    String playList;
    String listLink;
    private ArrayList<JcAudio> jcAudioArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_play_list);
        playList = getIntent().getStringExtra("PLAY_LIST");
        listLink = getIntent().getStringExtra("List_URL");

        tvPlDisl = findViewById(R.id.tvPlDisl);
        tvPlDisl.setText(playList);
        displayPlaylist = findViewById(R.id.displayPlaylist);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference().child("users").child("profile")
                .child(currentUser.getUid()).child("myPlaylistDetails").child(playList);


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                createLit.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    uploadMusic model = ds.getValue(uploadMusic.class);
                    assert model != null;
                    createLit.add(model);
                    Toast.makeText(getApplicationContext(),"sixe"+createLit.size(),Toast.LENGTH_LONG).show();
                }

                adapter = new DisplayListAdapter(createLit, getApplicationContext(),jcAudioArrayList);
                LinearLayoutManager manager = new LinearLayoutManager(DisplayPlayListActivity.this);
                displayPlaylist.setLayoutManager(manager);
                displayPlaylist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}