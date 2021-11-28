package com.example.audiostream.hometrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiostream.PlayMusic.MyApplicationContext;
import com.example.audiostream.PlayMusic.PlayMusicActivity;
import com.example.audiostream.R;
import com.example.audiostream.homepage;
import com.example.audiostream.uploadMusic;
import com.example.jean.jcplayer.model.JcAudio;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.GridviewHolder>{

    private ArrayList<uploadMusic> modelArrayList;
    private Context context;
    ArrayList<JcAudio> jcAudioArrayList;

    public TrackListAdapter(ArrayList<uploadMusic> modelArrayList, Context context,ArrayList<JcAudio> jcAudioArrayList) {
        this.modelArrayList = modelArrayList;
        this.context = context;
        this.jcAudioArrayList = jcAudioArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public GridviewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_track_item,parent,false);
        return new GridviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TrackListAdapter.GridviewHolder holder, @SuppressLint("RecyclerView") int position) {

        uploadMusic model = modelArrayList.get(position);
        jcAudioArrayList.add(JcAudio.createFromURL(model.getName(),model.getUrl()));
        holder.textView.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomePlayActivity.class);
                intent.putParcelableArrayListExtra("Array",jcAudioArrayList);
                intent.putExtra("ITEM_POS",holder.getAdapterPosition());
                intent.putExtra("ITEM_LIKES",model.getLikes());
                intent.putExtra("ITEM_URL",model.getUrl());
                intent.putExtra("TRACK","TRACK");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(modelArrayList.size()!=0) {
            if (modelArrayList.size() > 10) {
                return 10;
            } else {
                return modelArrayList.size();
            }
        }
        return 0;

    }

    public class GridviewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public GridviewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.trackTitleItem);
        }
    }
}
