package com.example.audiostream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiostream.PlayMusic.PlayMusicActivity;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>  {

    Context context;

    ArrayList<uploadMusic> musiclist;
    OnMusicListener mOnMusicListener;

    public MusicAdapter(Context context, ArrayList<uploadMusic> musiclist, OnMusicListener onMusicListener) {
        this.context = context;
        this.musiclist = musiclist;
        this.mOnMusicListener = onMusicListener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.musictracks, parent, false);

        return new MusicViewHolder(v, mOnMusicListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, @SuppressLint("RecyclerView") int position) {

        uploadMusic uploadMusic = musiclist.get(position);
        holder.trackname.setText(uploadMusic.getName());
    }

    @Override
    public int getItemCount() {
        return musiclist.size();
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView trackname;
        OnMusicListener onMusicListener;

        public MusicViewHolder(@NonNull View itemView, OnMusicListener onMusicListener) {
            super(itemView);

            trackname = itemView.findViewById(R.id.track);
            this.onMusicListener = onMusicListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           onMusicListener.OnMusicClick(getAdapterPosition());
        }
    }

    public  interface OnMusicListener{
        void OnMusicClick(int position);
    }


}
