package com.example.audiostream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class viewPodcasts extends AppCompatActivity {

    DatabaseReference databaseReference;
    PodcastAdapter podcastAdapter;
    ArrayList<uploadPodcasts> uploadPodcasts;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    Button backbutton;
    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    ListView listView;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_podcasts);

        fauth = FirebaseAuth.getInstance();
        fuser = fauth.getCurrentUser();

        backbutton = findViewById(R.id.backbtn);
        jcPlayerView = findViewById(R.id.jcplayer);
        listView = findViewById(R.id.list_podcasts);
        progressBar = (ProgressBar)findViewById(R.id.pBar);

        progressBar.setVisibility(View.VISIBLE);


        viewAllPodcasts();


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewPodcasts.this, homepage.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification();
            }
        });


        String uid = fuser.getUid().toString();

        uploadPodcasts = new ArrayList<>();

    }

    private void viewAllPodcasts() {

        String uid = fuser.getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("podcasts");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    uploadPodcasts uploadPodcast = ds.getValue(com.example.audiostream.uploadPodcasts.class);
                    uploadPodcasts.add(uploadPodcast);
                    jcAudios.add(JcAudio.createFromURL(uploadPodcast.getName(), uploadPodcast.getUrl()));
                }

                String[] uploads = new String[uploadPodcasts.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadPodcasts.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);

                jcPlayerView.initPlaylist(jcAudios, null);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Error Occured: " + error.toString());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

        });
    }
}