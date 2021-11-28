package com.example.audiostream.PlayMusic;

import com.example.audiostream.uploadMusic;
import com.example.audiostream.uploadPodcasts;
import com.example.jean.jcplayer.model.JcAudio;

import java.util.ArrayList;

public class MyApplicationContext {
   public static ArrayList<uploadMusic> listOfSong=new ArrayList();

   public static ArrayList<JcAudio> jcAudioSong=new ArrayList();


   public static ArrayList<uploadPodcasts> podcastList=new ArrayList();

   public static uploadMusic playlistMusic = new uploadMusic();
}
