package com.example.smita.represent;

/**
 * Created by Smita on 2/27/2016.
 */
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SenatorFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from xml
        View view = inflater.inflate(R.layout.fragment_senator, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.pic);
        TextView name = (TextView) view.findViewById(R.id.Name);
        TextView party = (TextView) view.findViewById(R.id.partySen);
        TextView website = (TextView) view.findViewById(R.id.website);
        TextView email = (TextView) view.findViewById(R.id.email);
        TextView twitter = (TextView) view.findViewById(R.id.twitter);
        Button more = (Button) view.findViewById(R.id.more);

        final Bundle bundle = getArguments();

        name.setText(bundle.getString("rep_name"));
        party.setText(bundle.getString("party"));
        website.setText(bundle.getString("website"));
        email.setText(bundle.getString("zip"));
        twitter.setText(bundle.getString("twitter"));
        img.setImageResource(R.drawable.blank);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Details.class);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

}