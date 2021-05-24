package com.example.codescannergostyle.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.codescannergostyle.util.Coupon;

import java.util.List;

@Dao
public interface CouponsDAO {
    @Query("SELECT * FROM coupon")
    List<Coupon> getAll();

    @Query("SELECT * FROM coupon WHERE ID = :ID")
    Coupon getById(int ID);


    @Insert
    void insertAll(Coupon... coupon);

    @Delete
    void delete(Coupon coupon);
}
