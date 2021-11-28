package com.example.audiostream.podcast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audiostream.PlayMusic.MyApplicationContext;
import com.example.audiostream.R;
import com.example.audiostream.uploadMusic;
import com.example.audiostream.uploadPodcasts;
import com.example.audiostream.utils.Constants;
import com.example.audiostream.utils.PreferenceManager;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class PlayPodcastActivity extends AppCompatActivity {

    TextView podcastLike;
    ImageView podcastLikeImg;
    JcPlayerView podcastPlayer;
    uploadPodcasts model=new uploadPodcasts();
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    ArrayList<uploadPodcasts>mMusicList = new ArrayList<>();
    DatabaseReference databaseReference,databaserefernceUser;
    SharedPreferences sharedPreferences;
    PreferenceManager manager;
    int pID;
    private String like;
    String URL,songTrack,songName,songUrl;
    int mLike,itempos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_podcast);

        URL = getIntent().getStringExtra("ITEM_URL");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("podcasts");
        databaserefernceUser=FirebaseDatabase.getInstance().getReference("users").child("profile").child(currentUser.getUid()).child("podcasts");
        podcastLike = findViewById(R.id.podcastLike);
        podcastLikeImg = findViewById(R.id.podcastLikeImg);
        podcastPlayer = findViewById(R.id.podcastPlayer);

        loadData();
        jcAudios=getIntent().getParcelableArrayListExtra("Array");
        itempos = getIntent().getIntExtra("SONG_POS",0);
        mLike = getIntent().getIntExtra("SONG_LIKE",0);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        model=ds.getValue(uploadPodcasts.class);
                        mMusicList.add(model);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        podcastLike.setText(String.valueOf(mLike));
        podcastPlayer.initPlaylist(jcAudios,null);
        podcastPlayer.setVisibility(View.VISIBLE);
        podcastPlayer.createNotification();
        podcastPlayer.playAudio(jcAudios.get(itempos));


        podcastLikeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pID = Objects.requireNonNull(podcastPlayer.getCurrentAudio()).getPosition();
                songName = mMusicList.get(pID).getName();
                mLike = mMusicList.get(pID).getLike();
                mLike = mLike+1;
                podcastLike.setText(String.valueOf(mLike));
                viewAllMusic(mLike,songTrack,songName);
            }
        });
    }

    private void viewAllMusic(int like,String url,String songName) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                pID = Objects.requireNonNull(podcastPlayer.getCurrentAudio()).getPosition();
                int count=mMusicList.get(pID).getCount();

                databaseReference.child(count + "").child("like").setValue(like).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            uploadPodcasts model = new uploadPodcasts(songName, url, like,count);
                            databaserefernceUser.child(count + "").setValue(model);
                            Toast.makeText(getApplicationContext(), "You likes " + songName, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void loadData() {

        sharedPreferences = getSharedPreferences("CityList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("courses", null);
        Type type = new TypeToken<ArrayList<uploadPodcasts>>() {}.getType();
        mMusicList = gson.fromJson(json, type);
        if (mMusicList == null) {
            mMusicList = new ArrayList<>();
        }
        sharedPreferences.edit().clear();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.edit().clear().commit();
    }
}