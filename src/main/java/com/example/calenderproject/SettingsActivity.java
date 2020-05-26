package com.example.calenderproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import static android.media.RingtoneManager.TYPE_ALARM;
import static android.media.RingtoneManager.TYPE_ALL;
import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static android.media.RingtoneManager.TYPE_RINGTONE;
import static android.media.RingtoneManager.setActualDefaultRingtoneUri;

public class SettingsActivity extends AppCompatActivity {
    private String chosenRingtone;
    Button settingsbutton1,settingsbutton2,settingsbutton3,settingsbutton4;
    LinearLayout lL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("mode", "light");

        if(s1.equals("light")){
            setTheme(R.style.AppTheme);
            setContentView(R.layout.settingsactivity_layout);
            lL = findViewById(R.id.llx1);
            lL.setBackgroundResource(R.color.blue);

        }else if (s1.equals("dark")){
            setTheme(R.style.ThemeOverlay_AppCompat_Dark);
            setContentView(R.layout.settingsactivity_layout);

        }

        settingsbutton1 = (Button)findViewById(R.id.setbutton1);
        //settingsbutton2 = (Button)findViewById(R.id.setbutton2);
        //settingsbutton3 = (Button)findViewById(R.id.setbutton3);
        settingsbutton4 = (Button)findViewById(R.id.setbutton4);

        settingsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri currentTone=
                        RingtoneManager.getActualDefaultRingtoneUri(SettingsActivity.this,
                                RingtoneManager.TYPE_ALARM);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                startActivityForResult(intent, 999);
            }
        });

        settingsbutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                String s1 = sharedPreferences.getString("mode", "light");
                if (s1.equals("dark")){
                    myEdit.remove("mode");
                    myEdit.putString("mode","light");
                    myEdit.apply();
                    myEdit.commit();
                    recreate();
                }else if(s1.equals("light")){
                    myEdit.remove("mode");
                    myEdit.putString("mode","dark");
                    myEdit.commit();
                    recreate();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 999 && resultCode == RESULT_OK){
            try {
                if (checkSystemWritePermission()) {
                    Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    RingtoneManager.setActualDefaultRingtoneUri(getApplication(), TYPE_ALL, uri);
                }else {
                    Toast.makeText(this, "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
            }

            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            RingtoneManager.setActualDefaultRingtoneUri(getApplication(), TYPE_ALL, uri);
            //txtView.setText("From :" + uri.getPath());
        }
    }

    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.System.canWrite(this))
                return true;
            else
                openAndroidPermissionsMenu();
        }
        return false;
    }

    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            this.startActivity(intent);
        }
    }

    /*
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null)
            {
                this.chosenRingtone = uri.toString();
                setActualDefaultRingtoneUri(getApplicationContext(),TYPE_ALARM, uri);       // ?????
                setActualDefaultRingtoneUri(getApplicationContext(),TYPE_NOTIFICATION, uri);
                setActualDefaultRingtoneUri(getApplicationContext(), TYPE_RINGTONE, uri);
            }
            else
            {
                this.chosenRingtone = null;
            }
        }
    }
     */

}
