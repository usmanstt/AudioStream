package com.example.audiostream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.audiostream.PlayMusic.ListAdapter;
import com.example.audiostream.PlayMusic.PlayMusicActivity;
import com.example.audiostream.podcast.ViewPodAdapter;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class viewMusic extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<uploadMusic> uploadMusics;
    StorageReference storageReference;
    Context context;
    MusicAdapter.OnMusicListener listener;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    RecyclerView.LayoutManager layoutManager;
    Button backbutton;
    //ListView listView;
    String URL="",name="";
    //JcPlayerView jcplayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    ProgressBar progressBar;
    EditText search;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_music);

        backbutton  = findViewById(R.id.backbtn);
        recyclerView = findViewById(R.id.list_music);
        //jcplayerView = (JcPlayerView) findViewById(R.id.jcplayer);
        progressBar = (ProgressBar)findViewById(R.id.pBar);
         search=findViewById(R.id.search);
        uploadMusics = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), homepage.class));
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

        fauth = FirebaseAuth.getInstance();
        fuser =fauth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        viewAllMusic();
        uploadMusics = new ArrayList<>();

    }

    private void viewAllMusic() {

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("music");
        StorageReference musicfiles = storageReference.child("users/music");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploadMusics.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    uploadMusic uploadMusic = ds.getValue(uploadMusic.class);
                    uploadMusics.add(uploadMusic);
                    if(uploadMusic!=null)
                    {
                        try {
                            jcAudios.add(JcAudio.createFromURL(uploadMusic.getName(),uploadMusic.getUrl()));
                        }
                        catch (IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }

                    }
                    Log.d("data", "onDataChange: "+ds.getChildren());

            }
                String[] uploads = new String[uploadMusics.size()];
                for(int i=0; i<uploads.length; i++){
                    uploads[i] = uploadMusics.get(i).getName();

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1,uploads);

                //jcplayerView.initPlaylist(jcAudios, null);
                ListAdapter adapter1 = new ListAdapter(uploadMusics,viewMusic.this,jcAudios);
                LinearLayoutManager manager = new LinearLayoutManager(viewMusic.this);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter1);

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
    private void  filter (String text)
    {
        ArrayList<uploadMusic> filtereddata=new ArrayList<>();
         ListAdapter adapter= new ListAdapter(uploadMusics,this,jcAudios);
        recyclerView.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                uploadMusics.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    uploadMusic helper=dataSnapshot.getValue(uploadMusic.class);
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