package com.example.smita.represent;

import android.app.Fragment;
import android.content.Intent;
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
public class PersonView extends CardFragment implements View.OnClickListener {
    @Override
    public View onCreateContentView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("T", "IN ONCREATE: ");
        String name = getArguments().getString("name");
        String party = getArguments().getString("party");
        String role = getArguments().getString("role");
        View myView = inflater.inflate(R.layout.person_layout, container, false);

        TextView n = (TextView) myView.findViewById(R.id.name);
        TextView p = (TextView) myView.findViewById(R.id.party);
        TextView r = (TextView) myView.findViewById(R.id.role);
        n.setText(name);
        p.setText(party);
        r.setText(role);
        myView.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View v) {
        launchClick(v);
    }

    private void launchClick(View v){
        Log.i("MyActivity", "MyClass.getView() — get item number " + "DidIEvenClickTho");
            Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
            getActivity().startService(sendIntent);
    }

}
