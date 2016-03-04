package com.example.smita.represent;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;


public class MainActivity extends Activity {
    public String globalZip = "94704";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            if (bundle.containsKey("reload")){
                Intent intentZip = new Intent(this, ViewPagerActivity.class);
                globalZip = bundle.getString("zip");

                Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                sendIntent.putExtra("from", "zip");
                sendIntent.putExtra("zip", globalZip);

                Bundle data = new Bundle();
                data.putString("zip",globalZip);

                intentZip.putExtras(data);

                startActivity(intentZip);
                startService(sendIntent);
            }
        }

    }


    public void useZip(View view){
        Intent intentZip = new Intent(this, ViewPagerActivity.class);
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);

        EditText zipText = (EditText) findViewById(R.id.zipCode);
        globalZip = zipText.getText().toString();

        Bundle data = new Bundle();
        data.putString("zip",globalZip);
        intentZip.putExtras(data);

        sendIntent.putExtra("from", "zip");
        sendIntent.putExtra("zip", globalZip);

        startActivity(intentZip);
        startService(sendIntent);
    }

    public void useLoc(View view){
        Intent intentLoc = new Intent(this, ViewPagerActivity.class);
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);

        Bundle data = new Bundle();
        data.putString("zip",globalZip);
        intentLoc.putExtras(data);

        sendIntent.putExtra("from", "currLoc");
        sendIntent.putExtra("zip", globalZip);

        startActivity(intentLoc);
        startService(sendIntent);
    }
}
