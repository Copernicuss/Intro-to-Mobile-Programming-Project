package com.example.calenderproject;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class UpdateEvent extends AppCompatActivity {
    int indexE;
    EditText edT1,edT2;
    TextView tv1;
    Button btu1,btu2,btu3,btu4,btu5,btu6,btu7,btu8;
    Event tmp_e;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("mode", "light");

        if(s1.equals("light")){
            setTheme(R.style.AppTheme);
            setContentView(R.layout.updating_layout);
            LinearLayout lL = (LinearLayout)findViewById(R.id.LinearLayoutxx2);
            lL.setBackgroundResource(R.color.blue);

        }else if (s1.equals("dark")){
            setTheme(R.style.ThemeOverlay_AppCompat_Dark);
            setContentView(R.layout.updating_layout);
        }

        edT1 = (EditText)findViewById(R.id.editTextU1);
        //edT2 = (EditText)findViewById(R.id.editTextU2);
        tv1 = (TextView)findViewById(R.id.textViewU1);
        btu1 = (Button)findViewById(R.id.buttonU1);
        btu2 = (Button)findViewById(R.id.buttonU2);
        btu3 = (Button)findViewById(R.id.buttonU3);
        btu4 = (Button)findViewById(R.id.buttonU4);
        btu5 = (Button)findViewById(R.id.buttonU5);
        btu6 = (Button)findViewById(R.id.buttonU11);
        btu7 = findViewById(R.id.buttonU7);
        btu8 = findViewById(R.id.buttonU51);

        final MyDatabase db = new MyDatabase(this);
        final String tmp_eventName = getIntent().getStringExtra("key");
        final String tmp_eventInfo = db.getEventInfoDatabase(tmp_eventName);

            String tmpString = "\t\t\t\t\tEvent Name : " + tmp_eventName;
            if(db.isNotificationDatabase(tmp_eventName) == 1){tmpString += "\n\t\t\t\t\tNotification : Yes";} else{tmpString += "\n\t\t\t\t\tNotification : No";}
            if(db.checkLocationDatabase(tmp_eventName) == 1){tmpString += "   Location : Yes";} else{tmpString += "   Location : No";}
            tv1.setTextSize(20);
            tv1.setText(tmpString);

             edT1.setText(tmp_eventInfo);
            //edT2.setText(tmp_eventName);

            btu1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TextView textView = new TextView(UpdateEvent.this);
                    textView.setText("Are you sure that you want to update this event?");
                    textView.setPadding(20, 30, 20, 30);
                    textView.setTextSize(15F);
                    textView.setBackgroundColor(Color.GRAY);
                    textView.setTextColor(Color.WHITE);


                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEvent.this);
                    //builder.setTitle("Pay Attention");
                    //builder.setMessage("Are you sure that you want to update this event?");
                    builder.setView(textView);
                    builder.setNegativeButton("No", null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String s1 = edT1.getText().toString();
                            db.updateEventInDatabase(tmp_eventName, s1);

                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast_container));
                            TextView text = (TextView)layout.findViewById(R.id.text);
                            text.setText("event has been updated");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 0);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                            finish();

                        }
                    });
                    builder.show();
                }
            });

            btu2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TextView textView = new TextView(UpdateEvent.this);
                    textView.setText("Are you sure that you want to delete this event?");
                    textView.setPadding(20, 30, 20, 30);
                    textView.setTextSize(15F);
                    textView.setBackgroundColor(Color.GRAY);
                    textView.setTextColor(Color.WHITE);

                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEvent.this);
                    //builder.setTitle("Pay Attention");
                    //builder.setMessage("Are you sure that you want to delete this event?");
                    builder.setView(textView);
                    builder.setNegativeButton("No", null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int x = db.getNotID(tmp_eventName);
                            if (db.isNotificationDatabase(tmp_eventName) == 1) {
                                Intent intent = new Intent(UpdateEvent.this, Notifications.class);
                                intent.setAction("SomeAction");
                                intent.putExtra("notification_ID", x);
                                //intent.putExtra("notif", notification);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateEvent.this, x, intent, PendingIntent.FLAG_NO_CREATE);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                if (pendingIntent != null) {
                                    alarmManager.cancel(pendingIntent);
                                }
                            }
                            db.deleteFromDatabase(tmp_eventName);
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast_container));
                            TextView text = (TextView)layout.findViewById(R.id.text);
                            text.setText("event has been deleted");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.BOTTOM, 0, 0);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                            finish();
                        }
                    });
                    builder.show();

                }
            });

            btu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (db.isNotificationDatabase(tmp_eventName) == 0){
                Intent intent = new Intent(UpdateEvent.this, SetNotification.class);
                intent.putExtra("event_object", tmp_eventName);
                startActivity(intent);
                //  }
            }
            });

            btu6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (db.checkLocationDatabase(tmp_eventName) == 1){
                        TextView textView = new TextView(UpdateEvent.this);
                        textView.setText("This event already has a location. Do you want to change it?");
                        textView.setPadding(20, 30, 20, 30);
                        textView.setTextSize(15F);
                        textView.setBackgroundColor(Color.GRAY);
                        textView.setTextColor(Color.WHITE);

                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEvent.this);
                        builder.setView(textView);
                        builder.setNegativeButton("No", null);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(UpdateEvent.this, ActivityMap.class);
                                intent.putExtra("map_name", tmp_eventName);
                                startActivity(intent);
                            }
                        });
                        builder.show();

                    }else {
                        Intent intent = new Intent(UpdateEvent.this, ActivityMap.class);
                        intent.putExtra("map_name", tmp_eventName);
                        startActivity(intent);
                    }
                }
            });

            btu4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float arr[] = new  float[2];
                    //double arr[] = new  double[2];
                    arr = db.getLatituteLongitude(tmp_eventName, arr);
                    Intent it = new Intent(Intent.ACTION_SEND);
                    //it.putExtra(Intent.EXTRA_EMAIL, new String[]{receiver.getText().toString()});
                    //it.putExtra(Intent.EXTRA_SUBJECT,topictext.getText().toString());
                    String stringText = "Event Name : " + tmp_eventName +"\nEvent Information : " + tmp_eventInfo+"\nDate: "+db.getDateAsString(tmp_eventName);
                    //it.putExtra(Intent.EXTRA_TEXT,stringText);
                    it.putExtra(Intent.EXTRA_SUBJECT,"Event ! ");
                    if (db.checkLocationDatabase(tmp_eventName) == 1){
                        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", arr[0],arr[1]);
                        stringText += "\n" + uri;
                    }else{stringText += "\nNo Location"; }
                    it.setType("message/rfc822");
                    it.putExtra(Intent.EXTRA_TEXT, stringText);
                    startActivity(Intent.createChooser(it,"Choose Mail App"));
                }
            });


            btu5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float arr[] = new  float[2];
                    //double arr[] = new  double[2];
                    arr = db.getLatituteLongitude(tmp_eventName, arr);
                    String stringText = "Event Name : " + tmp_eventName +"\nEvent Information : " + tmp_eventInfo+"\nDate: "+db.getDateAsString(tmp_eventName);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    //sendIntent.putExtra(Intent.EXTRA_TEXT, stringText);
                    sendIntent.setType("text/plain");
                    if (db.checkLocationDatabase(tmp_eventName) == 1){
                        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", arr[0],arr[1]);
                        stringText += "\n" + uri;
                    }else{stringText += "\nNo Location"; }
                    sendIntent.putExtra(Intent.EXTRA_TEXT, stringText);
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
            });

            btu7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (db.checkLocationDatabase(tmp_eventName) == 1){
                        Intent intent3 = new Intent(UpdateEvent.this, LocationActivityTwo.class);
                        float arr[] = new  float[2];
                        //double arr[] = new  double[2];
                        arr = db.getLatituteLongitude(tmp_eventName, arr);
                        intent3.putExtra("l1", arr[0]);
                        intent3.putExtra("l2", arr[1]);
                        //intent3.putExtra("l1", arr[0]);
                        //intent3.putExtra("l2", arr[1]);
                        intent3.putExtra("map_n", tmp_eventName);
                        startActivity(intent3);
                    }

                    else{
                        final AlertDialog.Builder mesaj = new AlertDialog.Builder(UpdateEvent.this);
                        mesaj.setTitle("Warning");
                        mesaj.setMessage("This event has no location");
                        mesaj.show();
                    }
                }
            });

            btu8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (db.isNotificationDatabase(tmp_eventName) == 1) {
                        int x = db.getNotID(tmp_eventName);
                        Intent intent = new Intent(UpdateEvent.this, Notifications.class);
                        intent.setAction("SomeAction");
                        intent.putExtra("notification_ID", x);
                        //intent.putExtra("notif", notification);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateEvent.this, x, intent, PendingIntent.FLAG_NO_CREATE);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        if (pendingIntent != null) {
                            alarmManager.cancel(pendingIntent);
                        }
                        db.updateEventNotifsInDatabase(tmp_eventName, 0, 0);
                        Toast.makeText(UpdateEvent.this, "The notification has been deleted.", Toast.LENGTH_SHORT).show();
                    }else{
                        final AlertDialog.Builder mesaj = new AlertDialog.Builder(UpdateEvent.this);
                        mesaj.setTitle("Warning");
                        mesaj.setMessage("This event has no notification");
                        mesaj.show();
                    }
                }
            });
    }

}
