package com.example.audiostream.podcast;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiostream.Chat.UserModel;
import com.example.audiostream.PodcastAdapter;
import com.example.audiostream.R;
import com.example.audiostream.uploadPodcasts;
import com.example.audiostream.utils.Constants;
import com.example.audiostream.utils.PreferenceManager;
import com.example.jean.jcplayer.model.JcAudio;

import java.util.ArrayList;

public class ViewPodAdapter extends RecyclerView.Adapter<ViewPodAdapter.PodcastViewHolder> {

    Context context;

    ArrayList<uploadPodcasts> podcastlist;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();

    public ViewPodAdapter(Context context, ArrayList<uploadPodcasts> podcastlist) {
        this.context = context;
        this.podcastlist = podcastlist;
    }

    @NonNull
    @Override
    public ViewPodAdapter.PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.sample_display_list, parent, false);

        return new ViewPodAdapter.PodcastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPodAdapter.PodcastViewHolder holder, @SuppressLint("RecyclerView") int position) {

        uploadPodcasts uploadPodcasts = podcastlist.get(position);
        holder.podcastname.setText(uploadPodcasts.getName());
        jcAudios.add(JcAudio.createFromURL(uploadPodcasts.getName(),uploadPodcasts.getUrl()));
        PreferenceManager manager = new PreferenceManager(context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Clicked "+holder.getAdapterPosition(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, PlayPodcastActivity.class);
                intent.putParcelableArrayListExtra("Array",jcAudios);
                intent.putExtra("SONG_POS",holder.getAdapterPosition());
                intent.putExtra("SONG_LIKE",uploadPodcasts.getLike());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return podcastlist.size();

    }
    public static class PodcastViewHolder extends RecyclerView.ViewHolder{

        TextView podcastname;
        public PodcastViewHolder(@NonNull View itemView) {
            super(itemView);
            podcastname = itemView.findViewById(R.id.displayItemListTv);
        }
    }
    public void filterlist(ArrayList<uploadPodcasts> filtereddata) {
        podcastlist=filtereddata;
        notifyDataSetChanged();
    }
}
