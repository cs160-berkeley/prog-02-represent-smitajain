package com.example.smita.represent;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by Smita on 3/3/2016. THIS CODE IS FROM STACK OVERFLOW AND http://jasonmcreynolds.com/?p=388
 */
public class ShakeEventListener implements SensorEventListener {
    private int Zips;

    private static final int MIN_FORCE = 10;
    private static final int MIN_DIRECTION_CHANGE = 2;
    private static final int MAX_PAUSE_BTW_CHANGE = 8000;
    private static final int MAX_DURATION = 10000;

    private long mFirstDirecChangeTime = 0;
    private long mLastDirecChangeTime;
    private int mDirectionChangeCount = 0;

    private float lastX=0;
    private float lastY=0;
    private float lastZ=0;

    private OnShakeListener mShakeListener;
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);

        if (totalMovement > MIN_FORCE) {

            // get time
            long now = System.currentTimeMillis();

            // store first movement time
            if (mFirstDirecChangeTime == 0) {
                mFirstDirecChangeTime = now;
                mLastDirecChangeTime = now;
            }

            // check if the last movement was not long ago
            long lastChangeWasAgo = now - mLastDirecChangeTime;
            if (lastChangeWasAgo < MAX_PAUSE_BTW_CHANGE) {

                // store movement data
                mLastDirecChangeTime = now;
                mDirectionChangeCount++;

                // store last sensor data
                lastX = x;
                lastY = y;
                lastZ = z;

                // check how many movements are so far
                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

                    // check total duration
                    long totalDuration = now - mFirstDirecChangeTime;
                    if (totalDuration < MAX_DURATION) {
                        mShakeListener.onShake();
                        resetShakeParameters();
                    }
                }

            } else {
                resetShakeParameters();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnShakeListener {
        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener){
        mShakeListener = listener;
    }

    private void resetShakeParameters() {
        mFirstDirecChangeTime = 0;
        mDirectionChangeCount = 0;
        mLastDirecChangeTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }
}
