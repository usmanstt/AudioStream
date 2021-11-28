package com.example.audiostream;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiostream.PlayMusic.MyApplicationContext;
import com.example.audiostream.podcast.PlayPodcastActivity;
import com.example.jean.jcplayer.model.JcAudio;

import java.text.BreakIterator;
import java.util.ArrayList;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {

    Context context;

    ArrayList<uploadPodcasts> podcastlist;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();

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
        jcAudios.add(JcAudio.createFromURL(uploadPodcasts.getName(),uploadPodcasts.getUrl()));

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Clicked",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, PlayPodcastActivity.class);
                intent.putParcelableArrayListExtra("ArrayJc",jcAudios);
                intent.putExtra("ITEM_POS",holder.getAdapterPosition());
                intent.putExtra("ITEM_LIKES",uploadPodcasts.getLike());
                intent.putExtra("ITEM_URL",uploadPodcasts.getUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/


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
