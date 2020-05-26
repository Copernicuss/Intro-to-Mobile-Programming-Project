package com.example.calenderproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button b1,b2,b3,b4,bLoc;
    CalendarView calendar;
    TextView textviewcalender;
    EditText textinput,textinput2;
    int flag;
    Random rand = new Random();
    String CHANNEL_ID = "10001" ;
    String CHANNEL_NAME = "MY NOTIFICATİONS" ;

    static boolean switchNotif = false;
    LayoutInflater inflater;

    MyDatabase db = new MyDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("mode", "light");

        if(s1.equals("light")){
            setTheme(R.style.AppTheme);
            setContentView(R.layout.calendarxml);
            LinearLayout lL = (LinearLayout)findViewById(R.id.LinearLayoutx5);
            //lL.setBackgroundResource(R.color.blue);

        }else if (s1.equals("dark")){
            setTheme(R.style.ThemeOverlay_AppCompat_Dark);
            setContentView(R.layout.calendarxml);
        }

        calendar = (CalendarView) findViewById(R.id.calendarView);
        textinput = (EditText) findViewById(R.id.editTextInput);
        textinput2 = (EditText) findViewById(R.id.editTextInput2);
        b1 = (Button) findViewById(R.id.button1);
        b3 = (Button) findViewById(R.id.button3);
        bLoc = (Button)findViewById(R.id.bLoc);
        flag = 1;
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(CalendarActivity.this);
        final String[] modes = {"None", "Daily", "Weekly","Monthly","Yearly"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(
                    @NonNull CalendarView view,
                    final int year,
                    final int month,
                    final int dayOfMonth)           //primary key kontrolü olmalı !!!!
            {
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String eventName = textinput.getText().toString();
                        if (!eventName.equals("")){
                            MyDatabase db = new MyDatabase(CalendarActivity.this);
                            if (db.isExist(eventName))    { flag = 0; }
                            if (flag == 1){
                                String str_spinner = modes[spinner.getSelectedItemPosition()];
                                if (str_spinner == null || str_spinner.equals("")){
                                    str_spinner = "None";
                                }
                                String eventInfo = textinput2.getText().toString();
                                if (eventInfo == null){ eventInfo = "";}
                                db.addToDatabase(eventName, eventInfo,dayOfMonth, month+1, year, str_spinner, 0,0 );

                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast_container));
                                TextView text = (TextView)layout.findViewById(R.id.text);
                                text.setText("event has been created");
                                Toast toast = new Toast(getApplicationContext());
                                toast.setGravity(Gravity.BOTTOM, 0, 400);
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();

                                b3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(CalendarActivity.this, SetNotification.class);
                                        intent.putExtra("event_object",eventName);
                                        //intent.putExtra("event_object", indexOfNewEvent);
                                        startActivity(intent);
                                    }
                                });

                                bLoc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent2 = new Intent(CalendarActivity.this, ActivityMap.class);
                                        intent2.putExtra("map_name", eventName);
                                        startActivity(intent2);
                                    }
                                });
                            }
                            flag = 1;
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String item = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2)
        {
            Double l1 = data.getDoubleExtra("l1", 0);
            Double l2 = data.getDoubleExtra("l1", 0);
        }
    }
    */
}
