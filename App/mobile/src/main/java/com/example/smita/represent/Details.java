package com.example.smita.represent;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Details extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView name = (TextView) findViewById(R.id.Name);
        TextView party = (TextView) findViewById(R.id.partySen);
        TextView website = (TextView) findViewById(R.id.website);
        TextView email = (TextView) findViewById(R.id.email);
        TextView twitter = (TextView) findViewById(R.id.twitter);
        TextView comm = (TextView) findViewById(R.id.comm);
        TextView bills = (TextView) findViewById(R.id.bill);
        Bundle bundleData = getIntent().getExtras();

        name.setText(bundleData.getString("rep_name"));
        party.setText(bundleData.getString("party"));
        website.setText(bundleData.getString("website"));
        email.setText(bundleData.getString("zip"));
        twitter.setText(bundleData.getString("twitter"));

        comm.setText("Farming Committee \n Balloons Committee \n Education Committee");
        bills.setText("UC Regents defunding \n Cars and Bars \n Not quite sure");
    }


}
