package com.example.audiostream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {


    Context context;

    ArrayList<uploadMusic> musiclist;

    public MusicAdapter(Context context, ArrayList<uploadMusic> musiclist) {
        this.context = context;
        this.musiclist = musiclist;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.musictracks, parent, false);

        return new MusicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {

        uploadMusic uploadMusic = musiclist.get(position);
        holder.trackname.setText(uploadMusic.getName());

    }

    @Override
    public int getItemCount() {
        return musiclist.size();
    }

    public static class MusicViewHolder extends RecyclerView.ViewHolder{

        TextView trackname;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);

            trackname = itemView.findViewById(R.id.track);
        }
    }

}
