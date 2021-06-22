package com.srinidhi.lgb;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Prefconfig {

    private static final String MY_PREFERENCE = "com.example.lgb";
    private static final String Address_key = "journey_id";
    private static final String Address_key1 = "attendance_id";
    private static final String Address_key2 = "ticket_id";
    public static  void savejourneyid(Context context,int id)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Address_key,id);
        editor.apply();
    }

    public static  void saveattendanceid(Context context,int id)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Address_key1,id);
        editor.apply();
    }
    public static int loadjourneyid(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE,Context.MODE_PRIVATE);
        return pref.getInt(Address_key,1);
    }
    public static int loadattendanceid(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE,Context.MODE_PRIVATE);
        return pref.getInt(Address_key1,1);
    }

    public static  void saveticketid(Context context,int id)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(Address_key2,id);
        editor.apply();
    }
    public static int loadticketid(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE,Context.MODE_PRIVATE);
        return pref.getInt(Address_key2,1);
    }
    public static void removeDataFromPref(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(MY_PREFERENCE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Address_key);
        editor.clear();
        editor.apply();

    }
}
