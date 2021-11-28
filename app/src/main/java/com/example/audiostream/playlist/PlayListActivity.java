package com.example.audiostream.playlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.audiostream.PlayMusic.MyApplicationContext;
import com.example.audiostream.R;
import com.example.audiostream.uploadMusic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlayListActivity extends AppCompatActivity {

    EditText etCreateList;
    Button createBtnList;
    TextView createNewListTv;
    private PlayListAdapter playListAdapter;
    private RecyclerView createPlaylist;
    private ArrayList<CreateListModel> createLit = new ArrayList<>();
    private DatabaseReference mReference;
    private String idPlaylist="0" ;
    private CreateListModel createListModel;

    uploadMusic playlistMusic = new uploadMusic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference().child("users").child("profile")
                .child(currentUser.getUid()).child("myPlaylist");
        MyApplicationContext.playlistMusic = playlistMusic;
        createNewListTv = findViewById(R.id.createNewListTv);
        createPlaylist = findViewById(R.id.createPlaylist);


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                createLit.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CreateListModel model = ds.getValue(CreateListModel.class);
                    assert model != null;
                    createLit.add(model);
                    idPlaylist = "playlist" + createLit.size();

                    Log.d("1122", "onDataChange: "+idPlaylist);
                    createListModel = new CreateListModel(model.getListName(), idPlaylist);
                }
                playListAdapter = new PlayListAdapter(createLit, getApplicationContext());
                LinearLayoutManager manager = new LinearLayoutManager(PlayListActivity.this);
                createPlaylist.setLayoutManager(manager);
                createPlaylist.setAdapter(playListAdapter);
                playListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        createNewListTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(PlayListActivity.this);
                dialog.setContentView(R.layout.dialog_create_list);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                etCreateList = (EditText) dialog.findViewById(R.id.etCreateList);
                createBtnList = (Button) dialog.findViewById(R.id.createBtnList);
                dialog.show();
                dialog.setCancelable(false);

                if (etCreateList.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter List name", Toast.LENGTH_LONG).show();

                } else {
                    createBtnList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = etCreateList.getText().toString().trim();
                            //idPlaylist = idPlaylist;
                            CreateListModel model = new CreateListModel(name,idPlaylist);
                            //createLit.add(model);
                            mReference.child(idPlaylist).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Added to PlayList", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

    }
}