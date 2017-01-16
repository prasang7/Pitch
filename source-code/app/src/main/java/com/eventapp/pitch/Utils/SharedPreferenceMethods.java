package com.eventapp.pitch.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.eventapp.pitch.project;
import com.google.gson.Gson;

import java.util.List;

public class SharedPreferenceMethods
{
    public static Context appContext;
    private static String PREFERENCE="pitch_eventapp_SharedPreference";
    public static final String SHARED_PREFERENCE_NAME="pitch_eventApp_SharedPreference";
    public static String UTILITY_PREFS_NAME="utilityPrefs";

    // Variables
    public static final String EVENT_NAME = "app_name";
    public static final String AUTHOR_NAME = "author_name";
    public static final String EVENT_DESCRIPTION = "event_description";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String LOCATION = "location";
    public static final String TYPE_OF_EVENT = "type_of_event";
    public static final String OPEN_CLOSED_STATUS = "openClosedStatus";
    public static final String TARGET_AUDIENCE = "targetAudience";
    public static final String EVENT_DURATION = "eventDuration";
    public static final String WEBSITE = "website";
    public static final String ORG_CONTACT = "contact";
    public static final String ORG_EMAIL = "email";
    public static final String SPONSOR_DETAIL= "sponsor_detail";
    public static final String COLOR = "color";
    public static final String RECENTS ="recents";
    public static final String isAssetsCopied = "assetsCopied";


    public static SharedPreferences.Editor getEditor(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        return editor;
    }
    public static SharedPreferences getSharedPreference(Context context)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedpreferences;
    }

    public static boolean getBoolean(Context context , String name)
    {
        SharedPreferences sharedPreferences=getSharedPreference(context);
        return sharedPreferences.getBoolean(name,false);
    }
    public static void setBoolean(Context context , String name , boolean value)
    {
        SharedPreferences.Editor editor=getEditor(context);
        editor.putBoolean(name,value);
        editor.commit();
    }
    public static String getString(Context context , String name)
    {
        SharedPreferences sharedPreferences=getSharedPreference(context);
        return sharedPreferences.getString(name, "");
    }
    public static void setString(Context context , String name , String value)
    {
        SharedPreferences.Editor editor=getEditor(context);
        editor.putString(name, value);
        editor.commit();
    }

    // for username string preferences
    public static void setDoubleSharedPreference(Context context, String name,double value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(name, (float) value);
        editor.commit();
    }

    public static Double getDoubleSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE, 0);
        return (double)settings.getFloat (name, 0.0f);
    }

    public static void saveRecentsAsync(Context context){
        setString(context,RECENTS,(new Gson()).toJson(pitch.recents));
    }

}
