package com.example.smita.represent;

/**
 * Created by Smita on 2/27/2016.
 */


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.json.JSONArray;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    int PAGE_COUNT = 3;
    // Tab Titles
    private String[] tabtitles;
    private Bundle data;
    public ViewPagerAdapter(Bundle bData, FragmentManager fm) {
        super(fm);
        data = bData;
        PAGE_COUNT = data.getInt("length");
        Log.i("WE out here", String.valueOf(PAGE_COUNT));
        tabtitles = new String[PAGE_COUNT];
        for(int j = 0; j < PAGE_COUNT; j++){
            String[] name = data.getStringArray(String.valueOf(j))[0].split(" ");
            tabtitles[j] = name[1];
        }
    }




    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        SenatorFragment fragmenttab2 = new SenatorFragment();

        Bundle bundle2 = new Bundle();
            Log.i("GETTING INFO", "WE HERE");
            bundle2.putString("rep_name", data.getStringArray(String.valueOf(position))[0]);
            bundle2.putString("website", "Website: "+data.getStringArray(String.valueOf(position))[3]);
            if (data.getStringArray(String.valueOf(position))[2].equals("R")){
                bundle2.putString("party", "Republican");
            }else if (data.getStringArray(String.valueOf(position))[2].equals("D")){
                bundle2.putString("party", "Democrat");
            }
            else{
                bundle2.putString("party", "Independent");
            }
            bundle2.putString("twitter","Twitter: @"+ data.getStringArray(String.valueOf(position))[5]);
            bundle2.putString("t_t_2",data.getStringArray(String.valueOf(position))[5]);
            bundle2.putString("personId",data.getStringArray(String.valueOf(position))[6]);
            bundle2.putString("email", "Email: " + data.getStringArray(String.valueOf(position))[4]);
            if (data.getStringArray(String.valueOf(position))[1].equals("Rep")){
                bundle2.putString("role", "Representative");
            }else{
                bundle2.putString("role", "Senator");
            }

            bundle2.putString("term",data.getStringArray(String.valueOf(position))[7]);

            fragmenttab2.setArguments(bundle2);
            return fragmenttab2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }

}