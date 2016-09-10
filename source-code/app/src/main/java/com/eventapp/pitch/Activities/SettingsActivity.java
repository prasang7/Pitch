package com.eventapp.pitch.Activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eventapp.pitch.R;

/**
 * Created by prasang on 14/6/16.
 */
public class SettingsActivity extends Activity{

    TextView tv_settingPanel, tv_requestApp, tv_contactUs, tv_rateUs, tv_about, tv_share;
    CardView cv_requestApp, cv_contactUs, cv_rateUs, cv_about, cv_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        cv_requestApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "req", Toast.LENGTH_SHORT).show();
            }
        });

        cv_contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "contact", Toast.LENGTH_SHORT).show();
            }
        });

        cv_rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "rate", Toast.LENGTH_SHORT).show();
            }
        });

        cv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "about", Toast.LENGTH_SHORT).show();
            }
        });

        cv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "share", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void init() {
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cv_requestApp = (CardView)findViewById(R.id.cv_setting_requestApp);
        cv_contactUs = (CardView)findViewById(R.id.cv_setting_contactUs);
        cv_rateUs = (CardView)findViewById(R.id.cv_setting_rateUs);
        cv_about = (CardView)findViewById(R.id.cv_setting_about);
        cv_share = (CardView)findViewById(R.id.cv_setting_share);

        tv_settingPanel = (TextView)findViewById(R.id.tv_settings_settingPanel);
        tv_requestApp = (TextView)findViewById(R.id.tv_settings_requestCustomApp);
        tv_contactUs = (TextView)findViewById(R.id.tv_settings_contactUs);
        tv_rateUs = (TextView)findViewById(R.id.tv_settings_rateUs);
        tv_about = (TextView)findViewById(R.id.tv_settings_about);
        tv_share = (TextView)findViewById(R.id.tv_settings_share);

        Typeface MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");

        tv_settingPanel.setTypeface(MontBold);
        tv_requestApp.setTypeface(MontBold);
        tv_contactUs.setTypeface(MontBold);
        tv_rateUs.setTypeface(MontBold);
        tv_about.setTypeface(MontBold);
        tv_share.setTypeface(MontBold);
    }
}
