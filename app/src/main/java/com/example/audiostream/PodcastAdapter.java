package com.example.audiostream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {

    Context context;

    ArrayList<uploadPodcasts> podcastlist;

    public PodcastAdapter(Context context, ArrayList<uploadPodcasts> podcastlist) {
        this.context = context;
        this.podcastlist = podcastlist;
    }

    @NonNull
    @Override
    public PodcastAdapter.PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.podcast, parent, false);

        return new PodcastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastAdapter.PodcastViewHolder holder, int position) {

        uploadPodcasts uploadPodcasts = podcastlist.get(position);
        holder.podcastname.setText(uploadPodcasts.getName());

    }

    @Override
    public int getItemCount() {
        return podcastlist.size();

    }
    public static class PodcastViewHolder extends RecyclerView.ViewHolder{

        TextView podcastname;

        public PodcastViewHolder(@NonNull View itemView) {
            super(itemView);

            podcastname = itemView.findViewById(R.id.podcast);
        }
    }
}
