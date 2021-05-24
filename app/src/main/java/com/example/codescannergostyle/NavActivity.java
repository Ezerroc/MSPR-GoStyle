package com.example.codescannergostyle;

import android.content.Intent;
import android.os.Bundle;

import com.example.codescannergostyle.database.AppDatabase;
import com.example.codescannergostyle.util.Coupon;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class NavActivity extends AppCompatActivity {

    private ArrayList<Coupon> coupons;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        //getSupportActionBar().hide();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        coupons = (ArrayList<Coupon>) bundle.getSerializable("coupons");
        db = AppDatabase.getInstance(NavActivity.this);



        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_scanner)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }

    public void refreshCoupons(){
        setCoupons(new ArrayList<Coupon>(db.couponsDAO().getAll()));

    }
}