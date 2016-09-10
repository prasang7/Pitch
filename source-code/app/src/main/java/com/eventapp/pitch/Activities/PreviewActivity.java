package com.eventapp.pitch.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eventapp.pitch.R;
import com.eventapp.pitch.Utils.SharedPreferenceMethods;

/**
 * Created by prasang on 14/6/16.
 */
public class PreviewActivity extends Activity {

    TextView tv_eventName, tv_eventType, tv_dateTime, tv_location, tv_headingDescription, tv_description,
                tv_openClosedStatus, tv_headingTargetAudience, tv_targetAudience, tv_headingDuration,
                tv_duration, tv_website, tv_sponsors, tv_headingOrg, tv_organizers, tv_headingContactDetails;
    Button bt_locateOnMap, bt_email, bt_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        setData();

        bt_locateOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PreviewActivity.this, "This will open google maps in child app.", Toast.LENGTH_LONG).show();
            }
        });

        bt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PreviewActivity.this, "This will open email box in child app.", Toast.LENGTH_LONG).show();
            }
        });

        bt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PreviewActivity.this, "This will call in child app.", Toast.LENGTH_LONG).show();
            }
        });
    }

    void init() {
        setContentView(R.layout.activity_preview);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tv_eventName = (TextView)findViewById(R.id.tv_preview_eventName);
        tv_eventType = (TextView)findViewById(R.id.tv_preview_eventType);
        tv_dateTime = (TextView)findViewById(R.id.tv_preview_dateTime);
        tv_website = (TextView)findViewById(R.id.tv_preview_website);

        tv_location = (TextView)findViewById(R.id.tv_preview_location);
        bt_locateOnMap = (Button)findViewById(R.id.bt_preview_locateOnMap);

        tv_headingDescription = (TextView)findViewById(R.id.tv_preview_descriptionHeadingText);
        tv_description = (TextView)findViewById(R.id.tv_preview_description);
        tv_openClosedStatus = (TextView)findViewById(R.id.tv_preview_openCloseStatus);

        tv_headingTargetAudience = (TextView)findViewById(R.id.tv_preview_headingTargetAudience);
        tv_targetAudience = (TextView)findViewById(R.id.tv_preview_targetAudience);
        tv_headingDuration = (TextView)findViewById(R.id.tv_preview_headingEventDuration);
        tv_duration = (TextView)findViewById(R.id.tv_preview_eventDuration);

        tv_headingOrg = (TextView)findViewById(R.id.tv_preview_headingOrganizers);
        tv_organizers = (TextView)findViewById(R.id.tv_preview_organizers);
        tv_headingContactDetails = (TextView)findViewById(R.id.tv_preview_headingContactDetails);
        bt_email = (Button)findViewById(R.id.bt_preview_email);
        bt_phone = (Button)findViewById(R.id.bt_preview_phone);

        tv_sponsors = (TextView)findViewById(R.id.tv_preview_sponsors);

        Typeface MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");

        tv_eventName.setTypeface(MontBold);
        tv_eventType.setTypeface(MontBold);
        tv_dateTime.setTypeface(MontBold);
        tv_website.setTypeface(MontReg);
        tv_location.setTypeface(MontBold);
        tv_headingDescription.setTypeface(MontBold);
        tv_description.setTypeface(MontReg);
        tv_openClosedStatus.setTypeface(MontBold);
        bt_locateOnMap.setTypeface(MontBold);
        tv_headingTargetAudience.setTypeface(MontBold);
        tv_targetAudience.setTypeface(MontReg);
        tv_headingDuration.setTypeface(MontBold);
        tv_duration.setTypeface(MontReg);
        tv_sponsors.setTypeface(MontBold);
        tv_headingOrg.setTypeface(MontBold);
        tv_organizers.setTypeface(MontReg);
        tv_headingContactDetails.setTypeface(MontBold);
        bt_email.setTypeface(MontBold);
        bt_phone.setTypeface(MontBold);
    }

    void setData() {
        tv_eventName.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.EVENT_NAME));
        tv_eventType.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.TYPE_OF_EVENT));
        tv_dateTime.setText("Event starts at " +
                SharedPreferenceMethods.getString(this, SharedPreferenceMethods.TIME) +
                " on " + SharedPreferenceMethods.getString(this, SharedPreferenceMethods.DATE));
        tv_website.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.WEBSITE));

        //location
        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.LOCATION).equals("")) {
            tv_location.setText("Location/Address not filled! Venue address will appear here.");
        }
        else {
            tv_location.setText("Venue: " + SharedPreferenceMethods.getString(this, SharedPreferenceMethods.LOCATION));
        }

        //description
        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.EVENT_DESCRIPTION).equals("")) {
            tv_description.setText("Looks like you haven't filled event description. Event description when filled will come here.");
        }
        else {
            tv_description.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.EVENT_DESCRIPTION));
        }

        // open closed status
        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.OPEN_CLOSED_STATUS).equals("yes")) {
            tv_openClosedStatus.setText("Yes, its an OPEN TO ALL event.");
        }
        else if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.OPEN_CLOSED_STATUS).equals("no")) {
            tv_openClosedStatus.setText("No, its a CLOSED event, for limited audience.");
        }


        // target audience
        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.TARGET_AUDIENCE).equals("")) {
            tv_targetAudience.setText("- Target audience not selected -");
        }
        else {
            tv_targetAudience.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.TARGET_AUDIENCE));
        }

        // event duration
        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.EVENT_DURATION).equals("")) {
            tv_duration.setText("- Event duration not set -");
        }
        else {
            tv_duration.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.EVENT_DURATION) + " hrs");
        }

        // Org name
        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.AUTHOR_NAME).equals("")) {
            tv_organizers.setText("- Event Organizer's name not set -");
        }
        else {
            tv_organizers.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.AUTHOR_NAME));
        }

        // Org email
        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.ORG_EMAIL).equals("")) {
            bt_email.setText("Organizer's email not available");
        }
        else {
            bt_email.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.ORG_EMAIL));
        }

        // Org phone
        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.ORG_CONTACT).equals("")) {
            bt_phone.setText("Organizer's contact not available");
        }
        else {
            bt_phone.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.ORG_CONTACT));
        }

        // Sponsors
        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.SPONSOR_DETAIL).equals("")) {
            tv_sponsors.setText("Sponsor's details not available");
        }
        else {
            tv_sponsors.setText("Sponsors for our event are:\n" + SharedPreferenceMethods.getString(this, SharedPreferenceMethods.SPONSOR_DETAIL));
        }
    }
}
