package com.eventapp.pitch.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import static com.eventapp.pitch.Utils.pitch.MANIFEST_FILE_PATH;
import static com.eventapp.pitch.Utils.pitch.PACKAGE_DIR;

/**
 * Created by prasang on 14/6/16.
 */
public class BasicDataActivity_4 extends Activity{

    TextView hiThere, pleaseFillName, next;
    Button bt_datePick, bt_timePick;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dateEmpty()) {
                    emptyDateAlert();
                }
                else if (timeEmpty()) {
                    emptyTimeAlert();
                }
                else {
                    saveDateTimeData();
                    pitch.currentProjectIndex=saveProject();
                    Intent i = new Intent(BasicDataActivity_4.this, InputFromUser.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }
            }
        });

        bt_datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BasicDataActivity_4.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bt_timePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(BasicDataActivity_4.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour > 12) {
                            selectedHour = selectedHour - 12;
                            bt_timePick.setText(selectedHour + " : " + selectedMinute + "  PM");
                        }
                        else
                            bt_timePick.setText(selectedHour + " : " + selectedMinute + "  AM");
                    }
                }, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), false).show();
            }
        });

    }

    void init() {
        setContentView(R.layout.basicdata_4);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.OPEN_CLOSED_STATUS, "yes");

        Typeface MontReg = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Regular.otf");
        Typeface MontBold = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Bold.otf");
        //Typeface MontHair = Typeface.createFromAsset(getApplication().getAssets(), "Montserrat-Hairline.otf");

        hiThere = (TextView)findViewById(R.id.tv_layName_hiThere);
        pleaseFillName = (TextView)findViewById(R.id.tv_layName_pleaseFillName);
        next = (TextView)findViewById(R.id.tv_layName_next);
        bt_datePick = (Button)findViewById(R.id.bt_basic4_datePick);
        bt_timePick = (Button)findViewById(R.id.bt_basic4_timePick);
        myCalendar = Calendar.getInstance();

        hiThere.setTypeface(MontReg);
        pleaseFillName.setTypeface(MontBold);
        next.setTypeface(MontBold);
        bt_datePick.setTypeface(MontBold);
        bt_timePick.setTypeface(MontBold);
    }

    boolean dateEmpty() {
        if (bt_datePick.getText().toString().equals("DATE")) {
            return true;
        }
        else {
            return false;
        }
    }

    boolean timeEmpty() {
        if (bt_timePick.getText().toString().equals("TIME")) {
            return true;
        }
        else {
            return false;
        }
    }

    void emptyDateAlert() {
        Toast.makeText(BasicDataActivity_4.this, "Please select event's date!", Toast.LENGTH_SHORT).show();
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
    }

    void emptyTimeAlert() {
        Toast.makeText(BasicDataActivity_4.this, "Please select event's time", Toast.LENGTH_SHORT).show();
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
    }

    void saveDateTimeData() {
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.DATE, bt_datePick.getText().toString());
        SharedPreferenceMethods.setString(this, SharedPreferenceMethods.TIME, bt_timePick.getText().toString());
        Toast.makeText(BasicDataActivity_4.this, "Saving data.."
                + SharedPreferenceMethods.getString(this, SharedPreferenceMethods.DATE)
                + SharedPreferenceMethods.getString(this, SharedPreferenceMethods.TIME), Toast.LENGTH_SHORT).show();
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
        bt_datePick.setText(sdf.format(myCalendar.getTime()));
    }

    int saveProject(){
        Context context = getApplicationContext();
        String name = SharedPreferenceMethods.getString(context,SharedPreferenceMethods.EVENT_NAME);
        Calendar calendar = Calendar.getInstance();
        String dateCreated = calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+1+" "+calendar.get(Calendar.YEAR);
        int templateId = pitch.currentTemplateIndex;
        project newProject = new project(name,templateId,dateCreated);
        template information = new template();
        information.setEventName(name);
        information.setOrganizersName(SharedPreferenceMethods.getString(context,SharedPreferenceMethods.AUTHOR_NAME));
        information.setDescription(SharedPreferenceMethods.getString(context,SharedPreferenceMethods.EVENT_DESCRIPTION));
        information.setTime(SharedPreferenceMethods.getString(context,SharedPreferenceMethods.TIME));
        information.setDate(SharedPreferenceMethods.getString(context,SharedPreferenceMethods.DATE));
        newProject.saveData(information);
        pitch.recents.add(newProject);
        SharedPreferenceMethods.saveRecentsAsync(context);
        return pitch.recents.size()-1;
    }
}
