package com.eventapp.pitch.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.eventapp.pitch.R;
import com.eventapp.pitch.Utils.SharedPreferenceMethods;
import com.eventapp.pitch.Utils.pitch;
import com.eventapp.pitch.project;
import com.eventapp.pitch.template;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by prasang on 14/6/16.
 */
public class InputFromUser extends Activity{

    Calendar myCalendar;
    FloatingActionButton fab_preview;
    ImageView iv_settings, iv_save;
    Button bt_dateSet, bt_timeSet;
    CardView cv_locationOfEvent, cv_description, cv_typeOfEvent, cv_orgContact, cv_orgEmail,
            cv_targetAudience, cv_eventDuration, cv_website, cv_sponsors;
    TextView tv_eventName, tv_locationOfEvent, tv_briefDescription, tv_typeOfEvent, tv_organizerContact,
            tv_organizerEmail, tv_questionOpenClosed,tv_export_status,
            tv_targetAudience, tv_eventDuration, tv_sponsors, tv_website, tv_selectColor, tv_orgName;
    ImageView gt_location, gt_description, gt_typeOfEvent, gt_openClosedStatus, gt_targetAudience, gt_eventDuration,
                gt_website, gt_orgContact, gt_orgEmail, gt_sponsor;
    RelativeLayout rl_color_blue, rl_color_purple, rl_color_red, rl_color_green;
    Button bt_reset;
    RadioButton openClosed_yes, openClosed_no;

    project currentProject;
    RelativeLayout loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        setBasicData();

        setGreenTicks();

        fab_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(InputFromUser.this, "Generating preview...", Toast.LENGTH_SHORT).show();
                saveData();
                preview();
            }
        });

        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputFromUser.this, SettingsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new exportDataAsync().execute();
            }
        });


        bt_dateSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InputFromUser.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt_timeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(InputFromUser.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour > 12) {
                            selectedHour = selectedHour - 12;
                            bt_timeSet.setText(selectedHour + " : " + selectedMinute + "  PM");
                            SharedPreferenceMethods.setString(InputFromUser.this, SharedPreferenceMethods.TIME, bt_timeSet.getText().toString());
                        }
                        else {
                            bt_timeSet.setText(selectedHour + " : " + selectedMinute + "  AM");
                            SharedPreferenceMethods.setString(InputFromUser.this, SharedPreferenceMethods.TIME, bt_timeSet.getText().toString());
                        }
                    }
                }, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), false).show();


            }
        });


        cv_locationOfEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputFromUser.this, Ex_Location.class);
                startActivity(i);
            }
        });

        cv_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputFromUser.this, Ex_Description.class);
                startActivity(i);
            }
        });

        cv_typeOfEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputFromUser.this, Ex_TypeOfEvent.class);
                startActivity(i);
            }
        });

        cv_orgContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputFromUser.this, Ex_OrgContact.class);
                startActivity(i);
            }
        });

        cv_orgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputFromUser.this, Ex_OrgEmail.class);
                startActivity(i);            }
        });



        cv_targetAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputFromUser.this, Ex_TargetAudience.class);
                startActivity(i);
            }
        });

        cv_eventDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputFromUser.this, Ex_EventDuration.class);
                startActivity(i);
            }
        });

        rl_color_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_selectColor.setText("SELECT COLOR SCHEME:  BLUE");
                SharedPreferenceMethods.setString(InputFromUser.this, SharedPreferenceMethods.COLOR, "Blue");
            }
        });

        rl_color_purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_selectColor.setText("SELECT COLOR SCHEME:  PURPLE");
                SharedPreferenceMethods.setString(InputFromUser.this, SharedPreferenceMethods.COLOR, "Purple");
            }
        });

        rl_color_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_selectColor.setText("SELECT COLOR SCHEME:  RED");
                SharedPreferenceMethods.setString(InputFromUser.this, SharedPreferenceMethods.COLOR, "Red");
            }
        });

        rl_color_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_selectColor.setText("SELECT COLOR SCHEME:  GREEN");
                SharedPreferenceMethods.setString(InputFromUser.this, SharedPreferenceMethods.COLOR, "Green");
            }
        });

        cv_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputFromUser.this, Ex_Website.class);
                startActivity(i);
            }
        });

        cv_sponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InputFromUser.this, Ex_Sponsors.class);
                startActivity(i);
            }
        });

        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
                setGreenTicks();
            }
        });

        openClosed_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceMethods.setString(InputFromUser.this, SharedPreferenceMethods.OPEN_CLOSED_STATUS, "yes");
            }
        });

        openClosed_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceMethods.setString(InputFromUser.this, SharedPreferenceMethods.OPEN_CLOSED_STATUS, "no");
            }
        });

    }

    void preview() {
        Intent i = new Intent(InputFromUser.this, PreviewActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }


    template saveData() {
        Context context = getApplicationContext();
        template information = currentProject.getInformation();
        information.setAddress(SharedPreferenceMethods.getString(context, SharedPreferenceMethods.LOCATION));
        information.setDuration(SharedPreferenceMethods.getString(context, SharedPreferenceMethods.EVENT_DURATION));
        information.setDescription(SharedPreferenceMethods.getString(context, SharedPreferenceMethods.EVENT_DESCRIPTION));
        information.setOrgContact(SharedPreferenceMethods.getString(context, SharedPreferenceMethods.ORG_CONTACT));
        information.setSponsor(SharedPreferenceMethods.getString(context, SharedPreferenceMethods.SPONSOR_DETAIL));
        information.setOrgEmail(SharedPreferenceMethods.getString(context, SharedPreferenceMethods.ORG_EMAIL));
        information.setTargetAudience(SharedPreferenceMethods.getString(context, SharedPreferenceMethods.TARGET_AUDIENCE));
        information.setEventType(SharedPreferenceMethods.getString(context, SharedPreferenceMethods.TYPE_OF_EVENT));
        information.setWebsiteURL(SharedPreferenceMethods.getString(context, SharedPreferenceMethods.WEBSITE));
        information.setOpenClosed(SharedPreferenceMethods.getString(context, SharedPreferenceMethods.OPEN_CLOSED_STATUS));
        pitch.recents.get(projectIndex).saveData(information);
        SharedPreferenceMethods.saveRecentsAsync(context);
        return information;
    }


    class exportDataAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingView.setVisibility(View.VISIBLE);
            fab_preview.setVisibility(View.GONE);
            tv_export_status.setText("Exporting...");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadingView.setVisibility(View.GONE);
            fab_preview.setVisibility(View.VISIBLE);
            tv_export_status.setText("Exported to output folder");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            pitch.extractToAccumulator();
            /*(new Runnable(){
                @Override
                public void run() {
                    tv_export_status.setText("Extracted to accumulator");
                }
            }).run();*/
            template information=saveData();

            pitch.manipulate(information);
            /*(new Runnable(){
                @Override
                public void run() {
                    tv_export_status.setText("Manipulation Complete");
                }
            }).run();*/
            pitch.zipAndSign();
            /*(new Runnable(){
                @Override
                public void run() {
                    tv_export_status.setText("Zipped And Signed");
                }
            }).run();*/
            return null;
        }
    }

    void init() {
        setContentView(R.layout.activity_input);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myCalendar = Calendar.getInstance();

        fab_preview = (FloatingActionButton) findViewById(R.id.fab);

        iv_settings = (ImageView)findViewById(R.id.iv_settingsGear);
        iv_save = (ImageView)findViewById(R.id.iv_save);
        tv_eventName = (TextView)findViewById(R.id.tv_input_eventName);
        tv_orgName = (TextView)findViewById(R.id.tv_input_organizerName);
        bt_dateSet = (Button)findViewById(R.id.bt_input_dateSet);
        bt_timeSet = (Button)findViewById(R.id.bt_input_timeSet);

        cv_locationOfEvent = (CardView)findViewById(R.id.cv_location);
        tv_locationOfEvent = (TextView)findViewById(R.id.tv_input_locationOfEvent);

        cv_description = (CardView)findViewById(R.id.cv_description);
        cv_typeOfEvent = (CardView)findViewById(R.id.cv_typeOfEvent);
        cv_orgContact = (CardView)findViewById(R.id.cv_orgContact);
        cv_orgEmail = (CardView)findViewById(R.id.cv_orgEmail);
        tv_briefDescription = (TextView)findViewById(R.id.tv_input_briefDescription);
        tv_typeOfEvent = (TextView)findViewById(R.id.tv_input_typeOfEvent);
        tv_organizerContact = (TextView)findViewById(R.id.tv_input_organizerContact);
        tv_organizerEmail = (TextView)findViewById(R.id.tv_input_organizerEmail);

        tv_questionOpenClosed = (TextView)findViewById(R.id.tv_input_questionOpenOrClosed);
        openClosed_yes = (RadioButton)findViewById(R.id.rb_openClosed_yes);
        openClosed_no = (RadioButton)findViewById(R.id.rb_openClosed_no);

        cv_targetAudience = (CardView)findViewById(R.id.cv_targetAudience);
        cv_eventDuration = (CardView)findViewById(R.id.cv_eventDuration);
        tv_targetAudience = (TextView)findViewById(R.id.tv_input_targetAudience);
        tv_eventDuration = (TextView)findViewById(R.id.tv_input_eventDuration);

        rl_color_blue = (RelativeLayout)findViewById(R.id.rl_input_colorPallete_blue);
        rl_color_purple = (RelativeLayout)findViewById(R.id.rl_input_colorPallete_purple);
        rl_color_red = (RelativeLayout)findViewById(R.id.rl_input_colorPallete_red);
        rl_color_green = (RelativeLayout)findViewById(R.id.rl_input_colorPallete_green);
        tv_selectColor = (TextView)findViewById(R.id.tv_input_selectColor);

        cv_website = (CardView)findViewById(R.id.cv_website);
        cv_sponsors = (CardView)findViewById(R.id.cv_sponsors);
        tv_website = (TextView)findViewById(R.id.tv_input_website);
        tv_sponsors = (TextView)findViewById(R.id.tv_input_sponsors);

        gt_location = (ImageView)findViewById(R.id.iv_doneImage_location);
        gt_description = (ImageView)findViewById(R.id.iv_doneImage_description);
        gt_typeOfEvent = (ImageView)findViewById(R.id.iv_doneImage_thypeOfEvent);
        gt_openClosedStatus = (ImageView)findViewById(R.id.iv_doneImage_openOrClose);
        gt_targetAudience = (ImageView)findViewById(R.id.iv_doneImage_targetAudience);
        gt_eventDuration = (ImageView)findViewById(R.id.iv_doneImage_duration);
        gt_website = (ImageView)findViewById(R.id.iv_doneImage_website);
        gt_orgContact = (ImageView)findViewById(R.id.iv_doneImage_orgContact);
        gt_orgEmail = (ImageView)findViewById(R.id.iv_doneImage_orgEmail);
        gt_sponsor = (ImageView)findViewById(R.id.iv_doneImage_sponsors);

        bt_reset = (Button)findViewById(R.id.bt_input_reset);

        loadingView =(RelativeLayout)findViewById(R.id.loading_view);
        tv_export_status= (TextView)findViewById(R.id.tv_export_status);

        Typeface MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");
        tv_eventName.setTypeface(MontBold);
        bt_dateSet.setTypeface(MontBold);
        bt_timeSet.setTypeface(MontBold);
        tv_locationOfEvent.setTypeface(MontBold);
        tv_briefDescription.setTypeface(MontBold);
        tv_typeOfEvent.setTypeface(MontBold);
        tv_organizerContact.setTypeface(MontBold);
        tv_organizerEmail.setTypeface(MontBold);
        tv_questionOpenClosed.setTypeface(MontBold);
        tv_targetAudience.setTypeface(MontBold);
        tv_eventDuration.setTypeface(MontBold);
        tv_sponsors.setTypeface(MontBold);
        tv_website.setTypeface(MontBold);
        tv_selectColor.setTypeface(MontBold);
        tv_orgName.setTypeface(MontReg);
        bt_reset.setTypeface(MontBold);
        openClosed_yes.setTypeface(MontReg);
        openClosed_no.setTypeface(MontReg);

        currentProject = pitch.recents.get(pitch.currentProjectIndex);
    }
    int projectIndex;
    void setBasicData() {
        tv_eventName.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.EVENT_NAME));
        tv_orgName.setText("- " + SharedPreferenceMethods.getString(this, SharedPreferenceMethods.AUTHOR_NAME));

        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.OPEN_CLOSED_STATUS).equals("yes")) {
            openClosed_yes.setChecked(true);
            openClosed_no.setChecked(false);
        }
        else if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.OPEN_CLOSED_STATUS).equals("no")) {
            openClosed_no.setChecked(true);
            openClosed_yes.setChecked(false);
        }

        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.DATE).equals("")) {
            bt_dateSet.setText("DATE");
        }
        else {
            bt_dateSet.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.DATE));
        }

        if (SharedPreferenceMethods.getString(this, SharedPreferenceMethods.TIME).equals("")) {
            bt_timeSet.setText("TIME");
        }
        else {
            bt_timeSet.setText(SharedPreferenceMethods.getString(this, SharedPreferenceMethods.TIME));
        }
    }

    void setGreenTicks() {
        if (!SharedPreferenceMethods.getString(this, SharedPreferenceMethods.LOCATION).equals("")) {
            gt_location.setVisibility(View.VISIBLE);
        }
        else {
            gt_location.setVisibility(View.INVISIBLE);
        }

        if (!SharedPreferenceMethods.getString(this, SharedPreferenceMethods.EVENT_DESCRIPTION).equals("")) {
            gt_description.setVisibility(View.VISIBLE);
        }
        else {
            gt_description.setVisibility(View.INVISIBLE);
        }

        if (!SharedPreferenceMethods.getString(this, SharedPreferenceMethods.TYPE_OF_EVENT).equals("")) {
            gt_typeOfEvent.setVisibility(View.VISIBLE);
        }
        else {
            gt_typeOfEvent.setVisibility(View.INVISIBLE);
        }

        if (!SharedPreferenceMethods.getString(this, SharedPreferenceMethods.OPEN_CLOSED_STATUS).equals("")) {
            gt_openClosedStatus.setVisibility(View.VISIBLE);
        }
        else {
            gt_openClosedStatus.setVisibility(View.INVISIBLE);
        }

        if (!SharedPreferenceMethods.getString(this, SharedPreferenceMethods.TARGET_AUDIENCE).equals("")) {
            gt_targetAudience.setVisibility(View.VISIBLE);
        }
        else {
            gt_targetAudience.setVisibility(View.INVISIBLE);
        }

        if (!SharedPreferenceMethods.getString(this, SharedPreferenceMethods.EVENT_DURATION).equals("")) {
            gt_eventDuration.setVisibility(View.VISIBLE);
        }
        else {
            gt_eventDuration.setVisibility(View.INVISIBLE);
        }

        if (!SharedPreferenceMethods.getString(this, SharedPreferenceMethods.WEBSITE).equals("")) {
            gt_website.setVisibility(View.VISIBLE);
        }
        else {
            gt_website.setVisibility(View.INVISIBLE);
        }

        if (!SharedPreferenceMethods.getString(this, SharedPreferenceMethods.ORG_CONTACT).equals("")) {
            gt_orgContact.setVisibility(View.VISIBLE);
        }
        else {
            gt_orgContact.setVisibility(View.INVISIBLE);
        }

        if (!SharedPreferenceMethods.getString(this, SharedPreferenceMethods.ORG_EMAIL).equals("")) {
            gt_orgEmail.setVisibility(View.VISIBLE);
        }
        else {
            gt_orgEmail.setVisibility(View.INVISIBLE);
        }

        if (!SharedPreferenceMethods.getString(this, SharedPreferenceMethods.SPONSOR_DETAIL).equals("")) {
            gt_sponsor.setVisibility(View.VISIBLE);
        }
        else {
            gt_sponsor.setVisibility(View.INVISIBLE);
        }
    }

    void resetData() {
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.EVENT_DESCRIPTION, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.DATE, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.TIME, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.LOCATION, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.TYPE_OF_EVENT, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.OPEN_CLOSED_STATUS, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.TARGET_AUDIENCE, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.EVENT_DURATION, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.WEBSITE, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.ORG_CONTACT, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.ORG_EMAIL, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.SPONSOR_DETAIL, "");
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.COLOR, "Blue");
        bt_dateSet.setText("DATE");
        bt_timeSet.setText("TIME");
        tv_selectColor.setText("SELECT COLOR SCHEME: BLUE (DEFAULT)");
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }
    };
    private void updateDateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        bt_dateSet.setText(sdf.format(myCalendar.getTime()));
        SharedPreferenceMethods.setString(InputFromUser.this, SharedPreferenceMethods.DATE, bt_dateSet.getText().toString());
    }
}
