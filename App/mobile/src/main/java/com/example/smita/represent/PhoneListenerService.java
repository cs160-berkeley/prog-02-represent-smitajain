package com.example.smita.represent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by Smita on 2/28/2016.
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String TOAST2 = "/send_bread";
    private static final String TOAST = "/send_coord";


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(TOAST2) ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            // Make a toast with the String
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Log.i("MyActivity", "MyClass.getView() — get item number " + "But why didnt it load");
            Intent sendIntent = new Intent(this, Details.class);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String[] vals = value.split(":");
            Bundle bundle1 = new Bundle();

            bundle1.putString("rep_name", vals[0]);
            bundle1.putString("party", vals[1]);
            bundle1.putString("role", vals[2]);
            bundle1.putString("personId", vals[3]);
            bundle1.putString("term", vals[4]);
            sendIntent.putExtras(bundle1);

            startActivity(sendIntent);
        }else if( messageEvent.getPath().equalsIgnoreCase(TOAST) ){
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            // Make a toast with the String
            Context context = getApplicationContext();
            Log.i("MyActivity", "MyClass.getView() — get item number ");
            Intent sendIntent = new Intent(this, MainActivity.class);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String[] vals = value.split(",");
            Bundle bundle1 = new Bundle();
            bundle1.putString("LAT", vals[0]);
            bundle1.putString("LONG", vals[1]);

            sendIntent.putExtras(bundle1);
            startActivity(sendIntent);

        }else{
            Log.i("IDK WHAT THE DRAMA IS", "MyClass.getView() — get item number " + "But why didnt it load");
        }

    }
}
