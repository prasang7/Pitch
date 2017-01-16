package com.eventapp.pitch.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.eventapp.pitch.R;
import com.eventapp.pitch.Utils.SharedPreferenceMethods;
import com.eventapp.pitch.Utils.pitch;
import com.eventapp.pitch.project;
import com.eventapp.pitch.template;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.eventapp.pitch.Utils.pitch.*;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readRecents();
        setlayoutView();
        //SharedPreferenceMethods.setBoolean(getApplicationContext(),SharedPreferenceMethods.isAssetsCopied,false);
        copyAssets();
    }

    void setlayoutView(){
        setContentView(R.layout.activity_launcher);
        setStatusBar(getWindow(),getApplicationContext(),R.color.dark_belize);
        layRecents();
        findViewById(R.id.newProject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Launcher.this,templateChooser.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        PACKAGE_DIR = Environment.getExternalStorageDirectory()+"/Android/data/"+getApplicationContext().getPackageName()+"/";
        TEMPLATE_DIR = PACKAGE_DIR + "templates/";
        CONFIG_FILE_PATH= PACKAGE_DIR + "accumulator/assets/config.json";
        MANIFEST_FILE_PATH = PACKAGE_DIR + "accumulator/AndroidManifest.xml";
    }

    void copyAssets(){
        boolean isCopied=SharedPreferenceMethods.getBoolean(this,SharedPreferenceMethods.isAssetsCopied);
        if(!isCopied){
            pitch.copyAssetsAsync(getApplicationContext());
            SharedPreferenceMethods.setBoolean(getApplicationContext(),SharedPreferenceMethods.isAssetsCopied,true);
        }
    }

    void layRecents(){
        if(recents!=null) {
            final LinearLayout recentView = (LinearLayout)findViewById(R.id.sv_recents);
            recentView.removeAllViews();
            for (final project recent : recents) {
                final View recentCardView = getLayoutInflater().inflate(R.layout.layout_recents_card, null);
                ((TextView) recentCardView.findViewById(R.id.project_title)).setText(recent.name);
                ((TextView) recentCardView.findViewById(R.id.project_template)).setText(templateNames[recent.templateId]);
                ((TextView) recentCardView.findViewById(R.id.project_created)).setText(recent.dateCreated);
                recentCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //CODE TO RESUME A PROJECT
                        pitch.currentTemplateIndex=recent.templateId;
                        pitch.currentProjectIndex=recents.indexOf(recent);
                        initializeProject(recent.informationJson);
                        startActivity(new Intent(Launcher.this,InputFromUser.class));
                        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
                    }
                });
                recentCardView.findViewById(R.id.delete_project).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recents.remove(recent);
                        SharedPreferenceMethods.saveRecentsAsync(getApplicationContext());
                        recentView.removeView(recentCardView);
                    }
                });
                recentView.addView(recentCardView);
            }
        }
        else
            recents= new ArrayList<>();
    }

    void readRecents(){
        String recentsJson = SharedPreferenceMethods.getString(getApplicationContext(),"recents");
        if(recentsJson!=null)
            recents = (new Gson()).fromJson(recentsJson,new TypeToken<List<project>>(){}.getType());
        else
            recents=null;
    }

    void initializeProject(String informationJson){
        template information = (new Gson()).fromJson(informationJson,new TypeToken<template>(){}.getType());
        Context context = getApplicationContext();
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.EVENT_NAME,information.getEventName());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.OPEN_CLOSED_STATUS,information.getOpenClosed());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.WEBSITE,information.getWebsiteURL());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.TYPE_OF_EVENT,information.getEventType());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.AUTHOR_NAME,information.getOrganizersName());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.DATE,information.getDate());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.EVENT_DESCRIPTION,information.getDescription());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.EVENT_DURATION,information.getDuration());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.LOCATION,information.getAddress());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.ORG_CONTACT,information.getOrgContact());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.ORG_EMAIL,information.getOrgEmail());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.SPONSOR_DETAIL,information.getSponsor());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.TARGET_AUDIENCE,information.getTargetAudience());
        SharedPreferenceMethods.setString(context,SharedPreferenceMethods.TIME,information.getTime());
    }
}
