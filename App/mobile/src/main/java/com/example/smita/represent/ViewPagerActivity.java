package com.example.smita.represent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class ViewPagerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_main.xml
        setContentView(R.layout.activity_view_pager);

        // Locate the viewpager in activity_main.xml
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        Bundle data = getIntent().getExtras();

        // Set the ViewPagerAdapter into ViewPager
        viewPager.setAdapter(new ViewPagerAdapter(data,getSupportFragmentManager()));
    }
}