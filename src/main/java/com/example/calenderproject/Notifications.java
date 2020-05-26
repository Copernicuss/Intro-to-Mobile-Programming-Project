package com.example.calenderproject;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import static android.app.NotificationManager.IMPORTANCE_DEFAULT;


public class Notifications extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    public static final String NOTIFICATION_NAME = "NOTIFICATİON_CHANNEL" ;

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra("notif");
        int notificationId = intent.getIntExtra("notification_ID", 0);
        manager.notify(notificationId, notification);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null)
        {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        final Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();


        new CountDownTimer(8000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                ringtone.stop();
            }
        }.start();

    }

}
