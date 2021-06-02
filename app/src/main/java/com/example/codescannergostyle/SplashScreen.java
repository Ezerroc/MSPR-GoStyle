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





}
