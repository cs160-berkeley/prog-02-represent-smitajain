package com.example.smita.represent;

/**
 * Created by Smita on 2/27/2016.
 */
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.*;


import java.io.IOException;
import java.util.List;

public class SenatorFragment extends Fragment {
    String Tid;
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
        TextView role = (TextView) view.findViewById(R.id.role);
        Button more = (Button) view.findViewById(R.id.more);

        final Bundle bundle = getArguments();

        name.setText(bundle.getString("rep_name"));
        party.setText(bundle.getString("party"));
        if(party.getText().equals("Republican")){
            img.setBackgroundColor(Color.parseColor("#FFF80E22"));
        }else if(party.getText().equals("Independent")){
            img.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        website.setText(bundle.getString("website"));
        email.setText(bundle.getString("email"));
        role.setText(bundle.getString("role"));
        twitter.setText(bundle.getString("twitter"));
        String tt = bundle.getString("t_t_2");
        setImages(bundle.getString("personId"),img);
        twt(view, tt);

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

    public void twt(View v, String tweet_usr){
        int r_id = R.id.rel;
        final LinearLayout rel_lay = (LinearLayout) v.findViewById(r_id);
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService search = twitterApiClient.getStatusesService();
        try {
            search.userTimeline(null, tweet_usr, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                @Override
                public void success(Result<List<Tweet>> result) {
                    TweetView tweetView = new TweetView(getActivity(), result.data.get(0), R.style.tw__TweetDarkWithActionsStyle);
                    rel_lay.addView(tweetView);
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.d("TwitterKit", "Load Tweet failure", exception);
                }
            });
        }catch(Exception e){
            System.out.println("another failure");
        }
    }
    public void setImages(String id, ImageView img) {

        String url = "https://theunitedstates.io/images/congress/225x275/"+id+".jpg";
        Picasso.with(getActivity().getApplicationContext()).load(url).into(img);
    }
}