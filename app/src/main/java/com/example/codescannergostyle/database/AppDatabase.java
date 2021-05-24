package com.example.codescannergostyle.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.codescannergostyle.util.Coupon;

@Database(entities = {Coupon.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CouponsDAO couponsDAO();

    private static volatile AppDatabase INSTANCE;

    // --- INSTANCE ---
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "coupons.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
