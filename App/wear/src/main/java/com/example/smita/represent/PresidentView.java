package com.example.smita.represent;

/**
 * Created by Smita on 3/3/2016.
 */

import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Smita on 3/2/2016.
 */
public class PresidentView extends CardFragment{
    @Override
    public View onCreateContentView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("T", "IN ONCREATE: ");
        String obama = getArguments().getString("obama");
        String romney = getArguments().getString("romney");
        String county = getArguments().getString("county");
        String state = getArguments().getString("state");
        View myView = inflater.inflate(R.layout.pres_layout, container, false);

        TextView d = (TextView) myView.findViewById(R.id.dem);
        TextView r = (TextView) myView.findViewById(R.id.rep);
        TextView c = (TextView) myView.findViewById(R.id.county);
        TextView s = (TextView) myView.findViewById(R.id.state);

        d.setText(obama);
        r.setText(romney);
        c.setText(county);
        s.setText(state);

        return myView;
    }

}
