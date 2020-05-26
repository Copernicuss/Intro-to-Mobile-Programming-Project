package com.example.calenderproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    Button btn1,btn2,btn3,btn4,btn5, btn6, btn7;
    String CHANNEL_ID = "10001" ;
    String CHANNEL_NAME = "MY NOTIFICATİONS" ;
    TextView tvMain;
    private String chosenRingtone;
    LinearLayout lL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("mode","light");
        myEdit.apply();
        myEdit.commit();

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("mode", "light");

        if(s1.equals("light")){
            setTheme(R.style.AppTheme);
            setContentView(R.layout.activity_main);
            lL = (LinearLayout)findViewById(R.id.linearLayout);
            lL.setBackgroundResource(R.color.blue);

        }else if (s1.equals("dark")){
            setTheme(R.style.ThemeOverlay_AppCompat_Dark);
            setContentView(R.layout.activity_main);
        }


        btn1 = (Button)findViewById(R.id.bttn1);
        //btn2 = (Button)findViewById(R.id.bttn2);
        btn3 = (Button)findViewById(R.id.bttn3);
       // btn4 = (Button)findViewById(R.id.bttn4);
        btn5 = (Button)findViewById(R.id.bttn5);
        //btn6 = findViewById(R.id.setbutton1);
        //btn7 = findViewById(R.id.setbutton4);
        //tvMain = (TextView)findViewById(R.id.textViewMain1);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventListsTwo.class);
                startActivity(intent);
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

    }


}
