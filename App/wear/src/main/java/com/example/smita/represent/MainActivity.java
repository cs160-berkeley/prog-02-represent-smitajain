package com.example.smita.represent;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class MainActivity extends Activity{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    private Random random = new Random();
    private String[] ary;
    private String from;
    private int count;
    String data;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    private WatchFragment mWatchFragment;

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("THIS IS WHERE", "THE PARTY STARTS");
        final Resources res = getResources();
        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras != null){
            from = extras.getString("LOCATION");
            if(from.equals("data")){
                data = extras.getString("data");
                ary = data.split(";");
                count = ary.length;
            }

        }

        mSensorListener = new ShakeEventListener();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener(){

            public void onShake() {
                Log.i("HELLOFROM THE OUTSIDE","WORLD");
                double tLat = (-81.1 - (random.nextFloat() * 36));
                double tLon = 32.78+(random.nextFloat() * 8);
                DecimalFormat df = new DecimalFormat("0.####");
                df.setRoundingMode(RoundingMode.DOWN);
                double oLat = Double.valueOf(df.format(tLat));
                double oLon = Double.valueOf(df.format(tLon));
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                sendIntent.putExtra("COORDINATES", String.valueOf(oLat) + "," + String.valueOf(oLon));
                sendIntent.putExtra("FROM","SHAKE");
                sendIntent.putExtra("data","rando");
                Log.d("COOORDINATESSS", String.valueOf(oLat) + "," + String.valueOf(oLon));
                startService(sendIntent);
                Log.i("HELLO", "WORLD");
            }
        });

        mWatchFragment = new WatchFragment(this, getFragmentManager());
        //---Assigns an adapter to provide the content for this pager---
        pager.setAdapter(mWatchFragment);

        DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        dotsPageIndicator.setPager(pager);
    }
    class WatchFragment extends FragmentGridPagerAdapter {
        final Context mContext;

        public WatchFragment(Context ctx, FragmentManager fm) {
            super(fm);
            mContext = ctx;
        }

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount(int i) {
            return count;
        }

        //---Go to current column when scrolling up or down (instead of default column 0)---
        @Override
        public int getCurrentColumnForRow(int row, int currentColumn) {
            return currentColumn;
        }

        //---Return our car image based on the provided row and column---
        @Override
        public Fragment getFragment(int row, int col) {
            Fragment fragment;
            Bundle bundleZ = new Bundle();
            if ( col < count-1){
                Log.i("This is for all", "the other ones");
                String[] vals = ary[col].split(":");
                bundleZ.putString("name", vals[0]);
                Log.i("This is for all", vals[0]);
                bundleZ.putString("party", vals[2]);
                bundleZ.putString("role", vals[1]);
                bundleZ.putString("personId", vals[3]);
                bundleZ.putString("term", vals[4]);
                fragment = new PersonView();
                fragment.setArguments(bundleZ);

            }
            else{
                String[] vals = ary[col].split(":");
                bundleZ.putString("obama", vals[3]);
                bundleZ.putString("romney", vals[2]);
                bundleZ.putString("county", vals[0]);
                bundleZ.putString("state", vals[1]);
                fragment = new PresidentView();
                fragment.setArguments(bundleZ);

            }

            return fragment;

        }

    }

}
