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
    Bundle b;

    @Override
    public View onCreateContentView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("T", "IN ONCREATE: ");
        b =getArguments();
        String name = b.getString("name");
        String party = b.getString("party");
        String role = b.getString("role");

        View myView = inflater.inflate(R.layout.person_layout, container, false);

        TextView n = (TextView) myView.findViewById(R.id.name);
        TextView p = (TextView) myView.findViewById(R.id.party);
        TextView r = (TextView) myView.findViewById(R.id.role);
        n.setText(name);
        if (party.equals("D")){
            p.setText("Democrat");
        }
        else if (party.equals("R")){
            p.setText("Democrat");
        }
        else{
            p.setText("Independent");
        }
        if (role.equals("Sen")){
            r.setText("Senator");
        }
        else {
            r.setText("Representative");
        }
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
            String name = b.getString("name");
            String party = b.getString("party");
            String role = b.getString("role");
            String personId = b.getString("personId");
            String term = b.getString("term");
            String data = name + ":" + party + ":" + role + ":" + personId + ":" + term;
            sendIntent.putExtra("data",data);
            getActivity().startService(sendIntent);
    }

}
