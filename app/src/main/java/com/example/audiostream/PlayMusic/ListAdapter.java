package com.example.audiostream.PlayMusic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiostream.R;
import com.example.audiostream.uploadMusic;
import com.example.jean.jcplayer.model.JcAudio;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.GridviewHolder>{

    private ArrayList<uploadMusic> modelArrayList;
    private Context context;
    ArrayList<JcAudio> jcAudioArrayList;

    public ListAdapter(ArrayList<uploadMusic> modelArrayList, Context context,ArrayList<JcAudio> jcAudioArrayList) {
        this.modelArrayList = modelArrayList;
        this.context = context;
        this.jcAudioArrayList=jcAudioArrayList;

    }

    @NonNull
    @NotNull
    @Override
    public GridviewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_song,parent,false);
        return new GridviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListAdapter.GridviewHolder holder, @SuppressLint("RecyclerView") int position) {

        uploadMusic model = modelArrayList.get(position);
        jcAudioArrayList.add(JcAudio.createFromURL(model.getName(),model.getUrl()));
        holder.textView.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayMusicActivity.class);
                intent.putParcelableArrayListExtra("Array",jcAudioArrayList);
                intent.putExtra("ITEM_POS",holder.getAdapterPosition());
                intent.putExtra("ITEM_LIKES",model.getLikes());
                intent.putExtra("ITEM_URL",model.getUrl());
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
            textView = itemView.findViewById(R.id.sample_Item);
        }
    }
    public void filterlist(ArrayList<uploadMusic> filtereddata) {
        modelArrayList=filtereddata;
        notifyDataSetChanged();
    }
}
