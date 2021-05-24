package com.example.codescannergostyle;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import com.example.codescannergostyle.util.Coupon;

import java.util.Date;

import static com.example.codescannergostyle.SplashScreen.NOTIFICATION_CHANNEL_ID;

public class CouponNotification extends Notification {

    private Context context;

    public CouponNotification(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "gostyle-notif";
            String description = "Desc du channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void scheduleNotification (Notification notification , Date date) {
        Intent notificationIntent = new Intent( context, NotificationPublisher. class ) ;
        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( context, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;

        int m10 = 600 * 1000;
        int h1 = 3600 * 1000;
        int h6 = h1 *6;
        int h12 = h1 * 12;
        int h24 = h1 *24;

        //TestAlarmNotification

        alarmManager.set(AlarmManager.RTC_WAKEUP , date.getTime()-m10, pendingIntent);

        alarmManager.set(AlarmManager. RTC_WAKEUP , date.getTime()-h1, pendingIntent) ;
        alarmManager.set(AlarmManager. RTC_WAKEUP , date.getTime()-h6, pendingIntent) ;
        alarmManager.set(AlarmManager. RTC_WAKEUP , date.getTime()-h12, pendingIntent) ;
        alarmManager.set(AlarmManager. RTC_WAKEUP , date.getTime()-h24, pendingIntent) ;
    }
    public Notification getNotification (Coupon coupon) {
        Intent intent = new Intent(context, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        String content = "Le coupon "+coupon.getCode()+" arrive bientôt à expiration, alors profitez en !";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Un coupon arrive bientôt à expiration")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return builder.build() ;
    }
}
