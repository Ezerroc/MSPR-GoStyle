package com.example.codescannergostyle;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.codescannergostyle.database.AppDatabase;
import com.example.codescannergostyle.util.Coupon;

import org.json.JSONException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SplashScreen extends AppCompatActivity {

    private int timer = 2000;
    private List<Coupon> coupons;




    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    final Calendar myCalendar = Calendar. getInstance () ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //getSupportActionBar().hide();
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.main));
        coupons = new ArrayList<>();
        createNotificationChannel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            verifyDB();
        }catch (Exception e){
            Toast.makeText(this,e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void verifyDB() throws JSONException, ParseException {
        //Create or Open Database and check if user is connected

        AppDatabase db = AppDatabase.getInstance(SplashScreen.this);

        int size = db.couponsDAO().getAll().size();


        if (size != 0) {
            for (Coupon coupon : db.couponsDAO().getAll()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date strDate = sdf.parse(coupon.getDate_to());
                if (new Date().after(strDate)) {
                    db.couponsDAO().delete(coupon);
                }else{
                    coupons.add(coupon);
                    Notification notif = getNotification(coupon);
                    scheduleNotification(notif, strDate);
                }
            }

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startHome();
            }
        }, timer);
    }

    private void startHome(){
        Intent intent = new Intent(this, NavActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("coupons", (Serializable) coupons);
        intent.putExtras(bundle);
        finish();
        startActivity(intent);
    }


    public void scheduleNotification (Notification notification , Date date) {
        Intent notificationIntent = new Intent( this, NotificationPublisher. class ) ;
        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(NotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;

        int h1 = 3600 * 1000;
        int h6 = h1 *6;
        int h12 = h1 * 12;
        int h24 = h1 *24;

        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , date.getTime()-1000, pendingIntent);

        /*alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , date.getTime()-h1, pendingIntent) ;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , date.getTime()-h6, pendingIntent) ;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , date.getTime()-h12, pendingIntent) ;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , date.getTime()-h24, pendingIntent) ;*/
    }
    public Notification getNotification (Coupon coupon) {
        Intent intent = new Intent(this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String content = "Le coupon "+coupon.getCode()+" arrive bientôt à expiration, alors profitez en !";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Un coupon arrive bientôt à expiration")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return builder.build() ;
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
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}
