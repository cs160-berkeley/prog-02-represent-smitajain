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
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;



import java.util.Random;

public class MainActivity extends Activity{
    private Random random = new Random();

    private Integer zip = Integer.parseInt("94704");

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

        final Resources res = getResources();
        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            zip = Integer.parseInt(extras.getString("LOCATION"));
        }

        mSensorListener = new ShakeEventListener();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener(){

            public void onShake() {
                Log.i("HELLOFROM THE OUTSIDE","WORLD");
                zip = Math.round(random.nextFloat() * 100000);
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                sendIntent.putExtra("LOCATION" , zip.toString());
                startService(sendIntent);
                Log.i("HELLO", "WORLD" + zip.toString());
            }
        });

        mWatchFragment = new WatchFragment(this, getFragmentManager(), zip);
        //---Assigns an adapter to provide the content for this pager---
        pager.setAdapter(mWatchFragment);

        DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        dotsPageIndicator.setPager(pager);
    }
    class WatchFragment extends FragmentGridPagerAdapter {
        final Context mContext;
        private int Zips;

        public WatchFragment(Context ctx, FragmentManager fm, Integer zips) {
            super(fm);
            mContext = ctx;
            Zips = zips;
        }

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount(int i) {
            return 4;
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
            if (col == 0){
                bundleZ.putString("name", "Barbara Boxer");
                bundleZ.putString("party", "Democrat");
                bundleZ.putString("role", "Senator");
                fragment = new PersonView();
                fragment.setArguments(bundleZ);

            }
            else if (col == 1){
                bundleZ.putString("name", "Diane Fienstein");
                bundleZ.putString("party", "Democrat");
                bundleZ.putString("role", "Senator");
                fragment = new PersonView();
                fragment.setArguments(bundleZ);
            }
            else if (col ==2){
                if (Zips == 94404){
                    bundleZ.putString("name", "Jackie Sprier");
                    bundleZ.putString("party", "Democrat");
                    bundleZ.putString("role", "Representative");
                    fragment = new PersonView();
                    fragment.setArguments(bundleZ);
                }
                else if (Zips == 94704) {
                    bundleZ.putString("name", "Barbara Lee");
                    bundleZ.putString("party", "Democrat");
                    bundleZ.putString("role", "Representative");
                    fragment = new PersonView();
                    fragment.setArguments(bundleZ);
                }
                else{
                    bundleZ.putString("name", "Nancy Pelosi");
                    bundleZ.putString("party", "Democrat");
                    bundleZ.putString("role", "Representative");
                    fragment = new PersonView();
                    fragment.setArguments(bundleZ);
                }


            }
            else{
                if (Zips == 94704){
                    bundleZ.putString("obama", "60% Votes for Obama");
                    bundleZ.putString("romney", "40% Votes for Romney");
                    bundleZ.putString("county", "Alameda County");
                    bundleZ.putString("state", "California");
                    fragment = new PresidentView();
                    fragment.setArguments(bundleZ);
                }
                else if(Zips == 94404){
                    bundleZ.putString("obama", "80% Votes for Obama");
                    bundleZ.putString("romney", "20% Votes for Romney");
                    bundleZ.putString("county", "San Mateo County");
                    bundleZ.putString("state", "California");
                    fragment = new PresidentView();
                    fragment.setArguments(bundleZ);
                }
                else{
                    bundleZ.putString("obama", "75% Votes for Obama");
                    bundleZ.putString("romney", "25% Votes for Romney");
                    bundleZ.putString("county", "Random County");
                    bundleZ.putString("state", "California");
                    fragment = new PresidentView();
                    fragment.setArguments(bundleZ);
                }

            }

            return fragment;

        }

    }

}
