package com.example.audiostream.hometrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audiostream.R;
import com.example.audiostream.uploadMusic;
import com.example.audiostream.uploadPodcasts;
import com.example.jean.jcplayer.JcPlayerManager;
import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.zip.Inflater;

public class HomePlayActivity extends AppCompatActivity {

    DatabaseReference mReference, mReferencePodcast;
    JcPlayerView trackPlayer;
    TextView countlike;
    JcPlayerManager manager;
    uploadPodcasts model1=new uploadPodcasts();
    uploadMusic model = new uploadMusic();
    DatabaseReference databaseReference;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    ArrayList<uploadMusic> mMusicList = new ArrayList<>();
    ArrayList<uploadPodcasts> mPodList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    View view;

    int itempos, mLike;
    String songTrack;
    String songName;
    int URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_play);
        countlike=findViewById(R.id.countlike);
        mReference = FirebaseDatabase.getInstance().getReference().child("users").child("music");
        mReferencePodcast = FirebaseDatabase.getInstance().getReference().child("users").child("podcasts");
        jcAudios = getIntent().getParcelableArrayListExtra("Array");

        trackPlayer = findViewById(R.id.trackPlayer);
        //jcAudios=getIntent().getParcelableArrayListExtra("Array");
        itempos = getIntent().getIntExtra("ITEM_POS", 0);
        mLike = getIntent().getIntExtra("ITEM_LIKES", 0);
        URL = getIntent().getIntExtra("ITEM_URL", 0);
        String type = getIntent().getStringExtra("TRACK");
        if(type.equals("TRACK")) {
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            model = ds.getValue(uploadMusic.class);
                            mMusicList.add(model);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        else
        {
            mReferencePodcast.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            model1 = ds.getValue(uploadPodcasts.class);
                            mPodList.add(model1);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

        trackPlayer.initPlaylist(jcAudios, null);
        trackPlayer.setVisibility(View.VISIBLE);
        trackPlayer.createNotification();
        trackPlayer.playAudio(jcAudios.get(itempos));



        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int pID =trackPlayer.getCurrentAudio().getPosition();
                if(type.equals("TRACK"))
                {
                    countlike.setText(mMusicList.get(pID).getLikes()+"");
                }
                else
                {
                    countlike.setText(mPodList.get(pID).getLike()+"");
                }

            }
        },3000);
/*
        if(type.equals("TRACK")){
            for (int i = 0; i < mMusicList.size(); i++) {
                jcAudios.add(JcAudio.createFromURL(mMusicList.get(i).getName(),mMusicList.get(i).getUrl()));
                if (URL.equals(mMusicList.get(i).getUrl())){
                    itempos = i;
                    mLike = mMusicList.get(i).getLikes();
                }
            }
        }
        if(type.equals("PODCAST")){
            for (int i = 0; i < mPodList.size(); i++) {
                jcAudios.add(JcAudio.createFromURL(mPodList.get(i).getName(),mPodList.get(i).getUrl()));
                if (URL.equals(mPodList.get(1).getUrl())){
                    itempos = 1;
                    mLike = mPodList.get(1).getLike();
                }
            }
        }*/





       /* trackLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pID = Objects.requireNonNull(trackPlayer.getCurrentAudio()).getPosition();
                if (type.equals("PODCAST")){
                    songTrack = mPodList.get(pID).getPodcast();
                    songName = mPodList.get(pID).getName();
                    mLike = mPodList.get(pID).getLike();
                    mLike = mLike+1;
                    trackLike.setText(String.valueOf(mLike));
                    mReferencePodcast.child(songTrack).child("like").setValue(mLike).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "You likes " + songName, Toast.LENGTH_LONG).show();
                            }
                        }
                    });*/

              /*  }else {

                    songTrack = mMusicList.get(pID).getTrackId();
                    songName = mMusicList.get(pID).getName();
                    mLike = mMusicList.get(pID).getLikes();
                }
                mLike = mLike+1;
                trackLike.setText(String.valueOf(mLike));
                mReference.child(songTrack).child("likes").setValue(mLike).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "You likes " + songName, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
*/

   /* private void loadData() {

        sharedPreferences = getSharedPreferences("CityList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("courses", null);
        Type type = new TypeToken<ArrayList<uploadPodcasts>>() {}.getType();
        mMusicList = gson.fromJson(json, type);
        if (mMusicList == null) {
            mMusicList = new ArrayList<>();
        }

        Gson gsonPod = new Gson();
        String jsonPod = sharedPreferences.getString("mPodList", null);
        Type typePod = new TypeToken<ArrayList<uploadPodcasts>>() {}.getType();
        mPodList = gsonPod.fromJson(jsonPod, typePod);
        if (mPodList == null) {
            mPodList = new ArrayList<>();
        }
        sharedPreferences.edit().clear();
    }*/
    }
}