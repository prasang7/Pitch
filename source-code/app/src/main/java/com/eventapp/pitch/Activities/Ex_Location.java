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
public class Ex_Location extends Activity{

    TextView text1, text2, save;
    EditText et_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emptyString()) {
                    emptyAlert();
                } else {
                    saveUsername();
                    Intent i = new Intent(Ex_Location.this, InputFromUser.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });
    }

    void init() {
        setContentView(R.layout.ex_location);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Typeface MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");

        text1 = (TextView)findViewById(R.id.tv_exLocation_text1);
        text2 = (TextView)findViewById(R.id.tv_exLocation_text2);
        save = (TextView)findViewById(R.id.tv_exLocation_save);
        et_answer = (EditText)findViewById(R.id.et_exlocation_answer);

        text1.setTypeface(MontReg);
        text2.setTypeface(MontBold);
        save.setTypeface(MontBold);
        et_answer.setTypeface(MontBold);
    }

    boolean emptyString() {
        String tempname = et_answer.getText().toString();
        if (tempname.equals("")) {
            return true;
        }
        else {
            return false;
        }
    }

    void emptyAlert() {
        Toast.makeText(Ex_Location.this, "Venue address can't be left empty. Right?", Toast.LENGTH_LONG).show();
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
    }

    void saveUsername() {
        SharedPreferenceMethods.setString(Ex_Location.this,SharedPreferenceMethods.LOCATION,et_answer.getText().toString());
    }

}
