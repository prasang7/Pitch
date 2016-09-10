package com.eventapp.pitch.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eventapp.pitch.R;
import com.eventapp.pitch.Utils.SharedPreferenceMethods;

/**
 * Created by prasang on 14/6/16.
 */
public class BasicDataActivity_2 extends Activity{

    TextView hiThere, pleaseFillName, next;
    EditText et_eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setOrganizerName();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emptyString()) {
                    emptyAlert();
                } else {
                    saveEventName();
                    Intent i = new Intent(BasicDataActivity_2.this, BasicDataActivity_3.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
    }

    void init() {
        setContentView(R.layout.basicdata_2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Typeface MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Bold.otf");
        Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");

        hiThere = (TextView)findViewById(R.id.tv_layName_hiThere);
        pleaseFillName = (TextView)findViewById(R.id.tv_layName_pleaseFillName);
        next = (TextView)findViewById(R.id.tv_layName_next);
        et_eventName = (EditText)findViewById(R.id.et_basic2_eventName);

        hiThere.setTypeface(MontReg);
        pleaseFillName.setTypeface(MontBold);
        next.setTypeface(MontBold);
        et_eventName.setTypeface(MontBold);
    }

    boolean emptyString() {
        String tempname = et_eventName.getText().toString();
        if (tempname.equals("")) {
            return true;
        }
        else {
            return false;
        }
    }

    void emptyAlert() {
        Toast.makeText(BasicDataActivity_2.this, "Event's name can't be left empty. Right?", Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
    }

    void saveEventName() {
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.EVENT_NAME, et_eventName.getText().toString());
    }

    void setOrganizerName() {
        hiThere.setText("HEY " + SharedPreferenceMethods.getString(this, SharedPreferenceMethods.AUTHOR_NAME) +",");
    }

}
