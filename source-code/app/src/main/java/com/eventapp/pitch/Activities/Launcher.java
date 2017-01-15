package com.eventapp.pitch.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eventapp.pitch.R;
import com.eventapp.pitch.Utils.SharedPreferenceMethods;
import com.eventapp.pitch.Utils.pitch;
import com.eventapp.pitch.project;
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
    }

    void copyAssets(){
        boolean isCopied=SharedPreferenceMethods.getBoolean(this,SharedPreferenceMethods.isAssetsCopied);
        if(!isCopied){
            pitch.copyAssets(getApplicationContext());
            SharedPreferenceMethods.setBoolean(getApplicationContext(),SharedPreferenceMethods.isAssetsCopied,true);
        }
    }

    void layRecents(){
        if(recents!=null) {
            LinearLayout recentView = (LinearLayout)findViewById(R.id.sv_recents);
            recentView.removeAllViews();
            for (project recent : recents) {
                View recentCardView = getLayoutInflater().inflate(R.layout.layout_recents_card, null);
                ((TextView) recentCardView.findViewById(R.id.project_title)).setText(recent.name);
                ((TextView) recentCardView.findViewById(R.id.project_template)).setText(templateNames[recent.templateId]);
                ((TextView) recentCardView.findViewById(R.id.project_created)).setText(recent.dateCreated);
                recentCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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
}
