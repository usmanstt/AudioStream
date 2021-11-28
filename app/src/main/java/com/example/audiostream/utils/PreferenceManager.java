package com.example.audiostream.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class PreferenceManager {

    private final SharedPreferences preferences;

    public PreferenceManager(Context context) {
        preferences= context.getSharedPreferences(Constants.kEY_REFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void putBoolean(String key,Boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public Boolean getBoolean(String key){
        return preferences.getBoolean(key,false);
    }

    public void putString(String key,String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getString(String key){
        return  preferences.getString(key,"");
    }


    public void putInt(String key,int value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public int getInt(String key){
        return  preferences.getInt(key,0);
    }

    public void clear(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }




}
