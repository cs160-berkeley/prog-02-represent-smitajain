package com.example.smita.represent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;


public class ViewPagerActivity extends FragmentActivity {

    Bundle bData = new Bundle();
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_view_pager);

        // Locate the viewpager in activity_main.xml
        viewPager = (ViewPager) findViewById(R.id.pager);
        Bundle bData = getIntent().getExtras();
        Log.i("THIS IS RANDOM", "BUT DID IT GO");
        viewPager.setAdapter(new ViewPagerAdapter(bData, getSupportFragmentManager()));

    }

}