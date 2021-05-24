package com.example.codescannergostyle.ui.home;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.codescannergostyle.util.*;
import com.example.codescannergostyle.NavActivity;
import com.example.codescannergostyle.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<Coupon> coupons;
    private ListView listeCoupons;
    private CouponsAdapter adapter;

    private View searchbar;
    private CardView selectedPos;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        listeCoupons = root.findViewById(R.id.listeCoupons);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        coupons = ((NavActivity)getActivity()).getCoupons();
        setList();
    }

    @Override
    public void onResume() {
        super.onResume();
        coupons = ((NavActivity)getActivity()).getCoupons();
        setList();
    }

    private void setList(){
        adapter = new CouponsAdapter(getContext(), coupons);
        //listeCoupons.addHeaderView(searchbar);
        listeCoupons.setAdapter(adapter);
        listeCoupons.setDividerHeight(0);

        listeCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardView cv = view.findViewById(R.id.details);

                LayoutTransition layoutTransition = cv.getLayoutTransition();
                ViewGroup.LayoutParams lp = cv.getLayoutParams();
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                if(selectedPos != null && selectedPos != cv && selectedPos.getHeight() != 0){
                    slideBackView(selectedPos, selectedPos.getHeight(), 0);
                }
                if(selectedPos == cv && cv.getHeight() > 0){
                    slideBackView(cv, cv.getHeight(),0);
                }

                layoutTransition.setDuration(500); // Change duration
                layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

                cv.setLayoutParams(lp);
                cv.requestLayout();

                Log.d("Position", String.valueOf(position));
                selectedPos = cv;
            }
        });
    }

    public void slideBackView(View view, int currentHeight, int newHeight) {
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(currentHeight, newHeight)
                .setDuration(500);

        slideAnimator.addUpdateListener(animation1 -> {
            Integer value = (Integer) animation1.getAnimatedValue();
            view.getLayoutParams().height = value.intValue();

            view.requestLayout();

        });

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start();
    }
}