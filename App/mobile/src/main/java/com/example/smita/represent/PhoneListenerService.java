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
    private static final String TOAST = "/send_toast";
    private static final String TOAST2 = "/send_bread";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(TOAST) ) {

            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            // Make a toast with the String
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Log.i("MyActivity", "MyClass.getView() — get item number " + "But why didnt it load");
            Intent sendIntent = new Intent(this, Details.class);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle1 = new Bundle();
            bundle1.putString("rep_name", "Barbara Boxer");
            bundle1.putString("website", "https://www.boxer.senate.gov/");
            bundle1.putString("party", "Democrat");
            bundle1.putString("twitter", "@SenatorBoxer");
            bundle1.putString("email", "senatorboxer@ca.gov");
            sendIntent.putExtras(bundle1);

            startActivity(sendIntent);

            // so you may notice this crashes the phone because it's
            //''sending message to a Handler on a dead thread''... that's okay. but don't do this.
            // replace sending a toast with, like, starting a new activity or something.
            // who said skeleton code is untouchable? #breakCSconceptions

        } else if(messageEvent.getPath().equalsIgnoreCase(TOAST2)){
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            // Make a toast with the String
            Context context = getApplicationContext();

            Intent sendIntent = new Intent(this, MainActivity.class);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Bundle bundle1 = new Bundle();
            bundle1.putString("reload","yes");
            bundle1.putString("zip", value);
            sendIntent.putExtras(bundle1);

            startActivity(sendIntent);


            Log.i("IDK WHAT THE DRAMA IS", "MyClass.getView() — get item number " + "But why didnt it load");
            //super.onMessageReceived( messageEvent );
        }
        else{
            Log.i("IDK WHAT THE DRAMA IS", "MyClass.getView() — get item number " + "But why didnt it load");
        }

    }
}
