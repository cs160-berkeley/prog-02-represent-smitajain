package com.example.smita.represent;

/**
 * Created by Smita on 2/27/2016.
 */


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    // Tab Titles
    private String tabtitles[] = new String[] { "Senator 1", "Senator 2", "Representative" };
    Context context;
    private Bundle data;

    public ViewPagerAdapter(Bundle bData, FragmentManager fm) {
        super(fm);
        data = bData;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // Open FragmentTab1.java
            case 0:
                SenatorFragment fragmenttab1 = new SenatorFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("rep_name", "Barbara Boxer");
                bundle1.putString("website", "https://www.boxer.senate.gov/");
                bundle1.putString("party", "Democrat");
                bundle1.putString("twitter", "@SenatorBoxer");
                bundle1.putString("email", "senatorboxer@ca.gov");

                fragmenttab1.setArguments(bundle1);
                return fragmenttab1;

            // Open FragmentTab2.java
            case 1:
                SenatorFragment fragmenttab2 = new SenatorFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("rep_name", "Diane Fienstein");
                bundle2.putString("website", "https://www.feinstein.senate.gov/");
                bundle2.putString("party", "Democrat");
                bundle2.putString("twitter", "@SenFeinstein");
                bundle2.putString("email", "senatorfienstein@ca.gov");

                fragmenttab2.setArguments(bundle2);

                return fragmenttab2;

            // Open FragmentTab3.java
            case 2:
                SenatorFragment fragmenttab3 = new SenatorFragment();
                Bundle bundle3 = new Bundle();
                if (data.getString("zip").equals("94404")){
                    bundle3.putString("rep_name", "Jackie Spiere");
                    bundle3.putString("website", "https://www.spiere.senate.gov/");
                    bundle3.putString("party", "Democrat");
                    bundle3.putString("twitter", "@SenSpiere");
                    bundle3.putString("email", "repjspeier@ca.gov");
                }
                else if (data.getString("zip").equals("94704")){
                    bundle3.putString("rep_name", "Barbara Lee");
                    bundle3.putString("website", "https://www.pelosi.senate.gov/");
                    bundle3.putString("party", "Democrat");
                    bundle3.putString("twitter", "@SenPelosi");
                    bundle3.putString("email", "repnpelosin@ca.gov");
                }
                else{
                    bundle3.putString("rep_name", "Nancy Pelosi");
                    bundle3.putString("website", "https://www.blee.senate.gov/");
                    bundle3.putString("party", "Democrat");
                    bundle3.putString("twitter", "@SenLee");
                    bundle3.putString("email", "repbarbaralee@ca.gov");
                }
                fragmenttab3.setArguments(bundle3);

                return fragmenttab3;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}