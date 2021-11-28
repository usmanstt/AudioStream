package com.example.audiostream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.audiostream.Chat.AllUserActivity;
import com.example.audiostream.Chat.UserAdapter;
import com.example.audiostream.Chat.UserModel;
import com.example.audiostream.podcast.ViewPodAdapter;
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
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class viewPodcasts extends AppCompatActivity {

    DatabaseReference databaseReference;
    ViewPodAdapter podcastAdapter;
    ArrayList<uploadPodcasts> uploadPodcasts;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    Button backbutton;
    EditText search;

    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    SharedPreferences sharedPreferences;
    RecyclerView recyclerPodcast;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_podcasts);

        fauth = FirebaseAuth.getInstance();
        fuser = fauth.getCurrentUser();
        search=findViewById(R.id.search);
        backbutton = findViewById(R.id.backbtn);
        //listView = findViewById(R.id.list_podcasts);
        recyclerPodcast = findViewById(R.id.recyclerPodcast);
        progressBar = (ProgressBar)findViewById(R.id.pBar);

        progressBar.setVisibility(View.VISIBLE);
        viewAllPodcasts();
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewPodcasts.this, homepage.class));
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

      /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification();
            }
        });*/

        uploadPodcasts = new ArrayList<>();

    }

    private void viewAllPodcasts() {

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("podcasts");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadPodcasts.clear();
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    uploadPodcasts uploadPodcast = ds.getValue(com.example.audiostream.uploadPodcasts.class);
                    uploadPodcasts.add(uploadPodcast);


                }
                addList();
                podcastAdapter = new ViewPodAdapter(getApplicationContext(),uploadPodcasts);
                recyclerPodcast.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true));
                recyclerPodcast.setAdapter(podcastAdapter);

              /*  String[] uploads = new String[uploadPodcasts.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadPodcasts.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);

                jcPlayerView.initPlaylist(jcAudios, null);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);*/

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

    public void addList()
    {
        sharedPreferences = getSharedPreferences("CityList", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(uploadPodcasts);
        editor.putString("courses", json);
        editor.apply();

    }
    private void  filter (String text)
    {
        ArrayList<uploadPodcasts> filtereddata=new ArrayList<>();
        ViewPodAdapter adapter= new ViewPodAdapter(this, uploadPodcasts);
        recyclerPodcast.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                uploadPodcasts.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    uploadPodcasts helper=dataSnapshot.getValue(uploadPodcasts.class);
                    if(helper.getName().toLowerCase().contains(text.toLowerCase()))
                    {
                        filtereddata.add(helper);
                    }
                    adapter.filterlist(filtereddata);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}