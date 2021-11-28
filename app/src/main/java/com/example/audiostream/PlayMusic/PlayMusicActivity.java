package com.example.audiostream.PlayMusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audiostream.R;
import com.example.audiostream.playlist.PlayListActivity;
import com.example.audiostream.uploadMusic;
import com.example.audiostream.utils.Constants;
import com.example.audiostream.utils.PreferenceManager;
import com.example.jean.jcplayer.JcPlayerManagerListener;
import com.example.jean.jcplayer.general.JcStatus;
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
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class PlayMusicActivity extends AppCompatActivity {

    DatabaseReference databaseReference,databaseReferenceUser;
    uploadMusic model=new uploadMusic();
    JcPlayerView jcplayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    ArrayList<uploadMusic>mMusicList = new ArrayList<>();
    TextView mLikes;
    ImageView mThumb;
    String URL,songTrack,songName,songUrl;
    int count;
    PreferenceManager preferenceManager;

    int pID;

    TextView addToPlaylist;

    int itempos,like,songLike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("music");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("users").child("profile")
                .child(currentUser.getUid()).child("music");
        jcAudios=getIntent().getParcelableArrayListExtra("Array");
        Log.d("pos", "onCreate: "+jcAudios);

//        mMusicList=getIntent().getParcelableArrayListExtra("Array_MUSIC");
        mMusicList=MyApplicationContext.listOfSong;
        mThumb = findViewById(R.id.thumbImg);
        mLikes = findViewById(R.id.likeCounter);
        addToPlaylist = findViewById(R.id.addToPlaylist);
        jcplayerView = (JcPlayerView) findViewById(R.id.jcplayerPlay);
        preferenceManager = new PreferenceManager(PlayMusicActivity.this);

        URL=getIntent().getStringExtra("ITEM_URL");
        itempos=getIntent().getIntExtra("ITEM_POS",0);
        like=getIntent().getIntExtra("ITEM_LIKES",0);
        Log.d("11221122", "onCreate: "+itempos);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot ds:snapshot.getChildren())
                    {
                        model=ds.getValue(uploadMusic.class);
                        mMusicList.add(model);
                        //jcAudios.add(JcAudio.createFromURL(model.getName(),model.getUrl()));
                        Log.d("11221122", "onDataChange: "+jcAudios.size());

                        /*for(int i=0;i<mMusicList.size();i++) {

                            if (URL.equals(mMusicList.get(i).getUrl())) {
                                itempos=i;
                                Log.d("11221122", "onDataChange: " + itempos);
                                return;
                            }
                        }*/
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

                mLikes.setText(String.valueOf(like));
                jcplayerView.initPlaylist(jcAudios,null);
                jcplayerView.setVisibility(View.VISIBLE);
                jcplayerView.createNotification();
                jcplayerView.playAudio(jcAudios.get(itempos));



        
        mThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pID = jcplayerView.getCurrentAudio().getPosition();
                Log.d("pos", "onClick: "+pID);
                songName = mMusicList.get(pID).getName();
                songLike = mMusicList.get(pID).getLikes();
                songTrack=mMusicList.get(pID).getUrl();
                songLike = songLike+1;
                count=mMusicList.get(pID).getCount();
                mLikes.setText(String.valueOf(songLike));
                viewAllMusic(songLike,songTrack,songName);
            }
        });


        addToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pID = Objects.requireNonNull(jcplayerView.getCurrentAudio()).getPosition();
                songName = mMusicList.get(pID).getName();
                songLike = mMusicList.get(pID).getLikes();
                songUrl = mMusicList.get(pID).getUrl();
                count=mMusicList.get(pID).getCount();

                preferenceManager.putString(Constants.KEY_SongName,songName);
                preferenceManager.putString(Constants.KEY_SongLike,String.valueOf(songLike));
                preferenceManager.putString(Constants.KEY_SongTrack,count+"");
                preferenceManager.putString(Constants.KEY_SongUrl,songUrl);

                uploadMusic model = new uploadMusic(songName,songUrl,songLike,count);
                MyApplicationContext.playlistMusic = model;
                startActivity(new Intent(getApplicationContext(), PlayListActivity.class));
            }
        });

    }

   /* private void addToPlaylistUser(uploadMusic model,String songTrack) {

        databaseReferenceUser.child(songTrack).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Added to playlist ",Toast.LENGTH_LONG).show();

                }
            }
        });
    }*/

    private void viewAllMusic(int like,String url,String songName) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            databaseReference.child(count+"").child("likes").setValue(like).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        pID = Objects.requireNonNull(jcplayerView.getCurrentAudio()).getPosition();
                                        uploadMusic model = new uploadMusic(songName,url,like,count);
                                        databaseReferenceUser.child(count+"").setValue(model);
                                        Toast.makeText(getApplicationContext(),"You likes "+songName,Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        /*
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    model = ds.getValue(uploadMusic.class);
                    assert model != null;
                    musicList.add(model);
                }
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
        });*/
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        if(hasCapture)
        {
            mLikes.setText(mMusicList.get(jcplayerView.getCurrentAudio().getPosition()).getLikes()+"");
        }
    }
}