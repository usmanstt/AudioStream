package com.example.audiostream.playlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiostream.PlayMusic.MyApplicationContext;
import com.example.audiostream.R;
import com.example.audiostream.uploadMusic;
import com.example.audiostream.utils.Constants;
import com.example.audiostream.utils.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.GridviewHolder>{

    private ArrayList<CreateListModel> modelArrayList;
    private Context context;
    PreferenceManager preferenceManager;

    public PlayListAdapter(ArrayList<CreateListModel> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public GridviewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_create_list,parent,false);
        return new GridviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PlayListAdapter.GridviewHolder holder, @SuppressLint("RecyclerView") int position) {

        CreateListModel model = modelArrayList.get(position);
        holder.textView.setText(model.getListName());
        preferenceManager = new PreferenceManager(context);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("users").child("profile")
                .child(currentUser.getUid()).child("myPlaylistDetails");

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String songName = preferenceManager.getString(Constants.KEY_SongName);
                String songUrl = preferenceManager.getString(Constants.KEY_SongUrl);
                String songLike = preferenceManager.getString(Constants.KEY_SongLike);
                String songTrack = preferenceManager.getString(Constants.KEY_SongTrack);
                Log.d("112233", "onLongClick: "+songName+" "+songUrl+" "+songLike+" "+songTrack+" ");
                uploadMusic modelMusic = new uploadMusic(songName,songUrl,Integer.valueOf(songLike),Integer.valueOf(songTrack));
               mReference.child(model.getListName()).child(songTrack).setValue(modelMusic).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(context,"Song added to "+model.getListName(),Toast.LENGTH_LONG).show();
                       }
                   }
               });
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayPlayListActivity.class);
                intent.putExtra("PLAY_LIST",model.getListName());
                intent.putExtra("List_URL",model.getListId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class GridviewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public GridviewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.createListTv);
        }
    }
}
