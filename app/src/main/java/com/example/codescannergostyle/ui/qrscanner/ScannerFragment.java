package com.example.codescannergostyle.ui.qrscanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.codescannergostyle.database.*;
import com.example.codescannergostyle.util.*;
import com.example.codescannergostyle.NavActivity;
import com.example.codescannergostyle.R;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScannerFragment extends Fragment {

    private ScannerViewModel dashboardViewModel;
    private static final int RC_PERMISSION = 10;
    private CodeScanner mCodeScanner;
    private boolean mPermissionGranted;
    private String prefix = "http://api.mspr.iswei.fr/coupons/";
    private ProgressBar progressBar;
    private CouponsDAO couponsDAO;
    private AppDatabase db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_scanner, container, false);
        progressBar = root.findViewById(R.id.progress);
        db = AppDatabase.getInstance(getContext());
        couponsDAO = db.couponsDAO();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;/*
                requestPermissions(new String[] {Manifest.permission.CAMERA}, RC_PERMISSION);*/
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }

        mCodeScanner = new CodeScanner(getContext(), root.findViewById(R.id.scanner_view));
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);

                        String url = result.getText();
                        if (url.startsWith(prefix) & !url.equals(prefix)) {

                            getCoupon(url);
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            Toast.makeText(getContext(), "Il semblerait que le QRCode ne soit pas valide", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mCodeScanner.setErrorCallback(error -> getActivity().runOnUiThread(
                () -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show()));

        return root;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        progressBar.setVisibility(View.VISIBLE);
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionGranted) {
            mCodeScanner.startPreview();
        }
    }

    private void getCoupon(String url){

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null){
                            try {
                                JSONObject couponObject = new JSONObject(response);
                                Coupon coupon = new Coupon(couponObject);
                                //Toast.makeText(getContext(), coupon.toString(), Toast.LENGTH_LONG).show();

                                Coupon couponTmp = couponsDAO.getById(coupon.getID());
                                if(couponTmp == null){
                                    if(couponInvalide(coupon)){
                                        Toast.makeText(getContext(), "Le coupon scanné est expiré", Toast.LENGTH_SHORT).show();
                                        getParentFragmentManager().popBackStack();
                                        return;
                                    }
                                    couponsDAO.insertAll(coupon);
                                    ((NavActivity)getActivity()).refreshCoupons();
                                    Toast.makeText(getContext(), "Le coupon a bien été enregistré", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getContext(), "Le coupon a déjà été scanné", Toast.LENGTH_LONG).show();
                                }
                                getParentFragmentManager().popBackStack();


                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    private boolean couponInvalide(Coupon coupon) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date strDate = sdf.parse(coupon.getDate_to());

        return new Date().after(strDate);
    }
}