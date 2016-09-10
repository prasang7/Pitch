package com.eventapp.pitch.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eventapp.pitch.R;
import com.eventapp.pitch.Utils.SharedPreferenceMethods;

/**
 * Created by prasang on 14/6/16.
 */
public class Ex_TypeOfEvent extends Activity{

    Button saveChoice;
    TextView cardHeading;
    RadioButton conference, seminar, meeting, themeParty, productLaunch, birthdayParty, wedding, weddingAnniver, other;
    String choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        saveChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choice.equals("")) {
                    emptyStringAlert();
                }
                else {
                    saveTypeOfEventData();
                    Intent i = new Intent(Ex_TypeOfEvent.this, InputFromUser.class);
                    startActivity(i);
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.cb_exType_conference:
                if (checked)
                    choice = "Conference";
                    break;
            case R.id.cb_exType_seminar:
                if (checked)
                    choice = "Seminar";
                    break;
            case R.id.cb_exType_meeting:
                if (checked)
                    choice = "Meeting";
                    break;
            case R.id.cb_exType_themeParty:
                if (checked)
                    choice = "Theme Party";
                    break;
            case R.id.cb_exType_productLaunch:
                if (checked)
                    choice = "Product Launch";
                    break;
            case R.id.cb_exType_birthday:
                if (checked)
                    choice = "Birthday Party";
                    break;
            case R.id.cb_exType_wedding:
                if (checked)
                    choice = "Wedding";
                    break;
            case R.id.cb_exType_weddingAnniversary:
                if (checked)
                    choice = "Wedding Anniversary";
                    break;
            case R.id.cb_exType_other:
                if (checked)
                    choice = "Other";
                    break;
        }
    }

    void saveTypeOfEventData() {
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.TYPE_OF_EVENT, choice);
    }

    void emptyStringAlert() {
        Toast.makeText(Ex_TypeOfEvent.this, "Please select at least one option!", Toast.LENGTH_SHORT).show();
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
    }

    void init() {
        setContentView(R.layout.ex_typeofevent);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        choice = "";

        cardHeading = (TextView)findViewById(R.id.tv_exTyprOfEvent_heading);
        saveChoice = (Button)findViewById(R.id.bt_extypeOfEvent_save);

        conference = (RadioButton)findViewById(R.id.cb_exType_conference);
        seminar = (RadioButton)findViewById(R.id.cb_exType_seminar);
        meeting = (RadioButton)findViewById(R.id.cb_exType_meeting);
        themeParty = (RadioButton)findViewById(R.id.cb_exType_themeParty);
        productLaunch = (RadioButton)findViewById(R.id.cb_exType_productLaunch);
        birthdayParty = (RadioButton)findViewById(R.id.cb_exType_birthday);
        wedding = (RadioButton)findViewById(R.id.cb_exType_wedding);
        weddingAnniver = (RadioButton)findViewById(R.id.cb_exType_weddingAnniversary);
        other = (RadioButton)findViewById(R.id.cb_exType_other);

        Typeface MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");
        cardHeading.setTypeface(MontBold);
        conference.setTypeface(MontReg);
        seminar.setTypeface(MontReg);
        meeting.setTypeface(MontReg);
        themeParty.setTypeface(MontReg);
        productLaunch.setTypeface(MontReg);
        birthdayParty.setTypeface(MontReg);
        wedding.setTypeface(MontReg);
        weddingAnniver.setTypeface(MontReg);
        other.setTypeface(MontReg);
        saveChoice.setTypeface(MontReg);
    }


}
