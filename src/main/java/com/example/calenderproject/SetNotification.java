package com.example.calenderproject;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class SetNotification extends AppCompatActivity {
    String CHANNEL_ID = "10001" ;
    String CHANNEL_NAME = "MY NOTIFICATİONS" ;
    Random rand = new Random();
    Button stButton1,stButton2,stButton3;
    EditText stEditText1;
    TextView stTextView1,stTextView2,stTextView3;
    //Event event;
    int yearNofif = 0;
    int monthNofif = 0;
    int dayNotif = 0;
    int hourNotif = 12;
    int minuteNotif = 0;
    MyDatabase db = new MyDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("mode", "light");

        if(s1.equals("light")){
            setTheme(R.style.AppTheme);
            setContentView(R.layout.setnotif_layout);
            LinearLayout lL = (LinearLayout)findViewById(R.id.LinearLayoutxx3);
            lL.setBackgroundResource(R.color.blue);

        }else if (s1.equals("dark")){
            setTheme(R.style.ThemeOverlay_AppCompat_Dark);
            setContentView(R.layout.setnotif_layout);
        }

        stButton1 = (Button)findViewById(R.id.setnotifButton1);
        stButton2 = (Button)findViewById(R.id.setnotifButton2);
        stButton3 = (Button)findViewById(R.id.setnotifButton3);
        stTextView1 = (TextView)findViewById(R.id.setnotifText2);
        stTextView2 = (TextView)findViewById(R.id.setnotifText3);
        stTextView3 = (TextView)findViewById(R.id.setnotifText4);

        MyDatabase db = new MyDatabase(this);
        final String eventName = getIntent().getStringExtra("event_object");
        final String eventInfo = db.getEventInfoDatabase(eventName);
        final String eventRepeat = db.getEventRepeatDatabase(eventName);

        String tmpStr = "\t\t\t\t       Event Name : " + eventName;
        stTextView1.setTextSize(20);
        stTextView1.setText(tmpStr);

        stButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar tmp_cal = Calendar.getInstance();
                yearNofif = tmp_cal.get(Calendar.YEAR);
                monthNofif = tmp_cal.get(Calendar.MONTH);
                dayNotif = tmp_cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(SetNotification.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yil, int ay, int gun) {
                        ay += 1;
                        String s1 = gun + "/" + ay + "/" + yil;
                        stTextView2.setText(s1);
                        dayNotif = gun;
                        monthNofif = ay;
                        yearNofif = yil;
                    }
                },yearNofif,monthNofif,dayNotif);
                datePicker.show();
            }
        });


        stButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetNotification.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String s2 = hourOfDay + " : " + minute;
                        stTextView3.setText(s2);
                        hourNotif = hourOfDay;
                        minuteNotif = minute;
                    }
                }, hourNotif,minuteNotif,true);
                timePickerDialog.show();
            }
        });


        stButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmWithNotification(getNotification(eventName, eventInfo),eventName, dayNotif, monthNofif, yearNofif, hourNotif, minuteNotif, eventRepeat);
                finish();
            }
        });

    }

    private void setAlarmWithNotification(Notification notification, String eventName, int a, int b, int c, int d, int e, String eventRepeat){
        int notificationID = rand.nextInt(9999)+90000;
        //int notificationID = 991;
        Intent intent = new Intent(this, Notifications.class);
        intent.putExtra("notification_ID", notificationID);
        intent.putExtra("notif", notification);
        intent.setAction("SomeAction");
        PendingIntent pending = PendingIntent.getBroadcast(this, notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.DATE,a);
        cal.set(Calendar.MONTH,b-1);
        cal.set(Calendar.YEAR,c);
        cal.set(Calendar.HOUR_OF_DAY, d);
        cal.set(Calendar.MINUTE, e);
        cal.set(Calendar.SECOND,0);
        //cal.add(Calendar.SECOND, 10);

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast_container));
        TextView text = (TextView)layout.findViewById(R.id.text);
        text.setText("notification with alarm has been created");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);

        AlarmManager alarm_Manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (eventRepeat.equals("None")) {
            alarm_Manager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pending);
            //Toast.makeText(SetNotification.this, "alarm has been set !", Toast.LENGTH_SHORT).show();
            toast.show();
        }else if (eventRepeat.equals("Daily")){
            alarm_Manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pending);
            //Toast.makeText(SetNotification.this, "daily alarm has been set !", Toast.LENGTH_SHORT).show();
            toast.show();
        }
        else if (eventRepeat.equals("Weekly")){
                alarm_Manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 7*AlarmManager.INTERVAL_DAY, pending);
                //Toast.makeText(SetNotification.this, "weekly alarm has been set !", Toast.LENGTH_SHORT).show();
                toast.show();
        }
        else if (eventRepeat.equals("Monthly")){
            alarm_Manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*AlarmManager.INTERVAL_DAY, pending);
            //Toast.makeText(SetNotification.this, "monthly alarm has been set !", Toast.LENGTH_SHORT).show();
            toast.show();
        }else if (eventRepeat.equals("Yearly")){
            alarm_Manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 365*AlarmManager.INTERVAL_DAY, pending);
            //Toast.makeText(SetNotification.this, "yearly alarm has been set !", Toast.LENGTH_SHORT).show();
            toast.show();
        }else{
            Toast.makeText(SetNotification.this, "alarm could not be set !", Toast.LENGTH_SHORT).show();
            return;
        }

        MyDatabase db = new MyDatabase(this);
        db.updateEventNotifsInDatabase(eventName, 1, notificationID );
    }

    private Notification getNotification(String eventName, String eventInfo) {
        return new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(eventName)
                .setContentText(eventInfo)
                .setSmallIcon(R.drawable.presence_online)
                .setAutoCancel(true)
                .build();
    }
}
