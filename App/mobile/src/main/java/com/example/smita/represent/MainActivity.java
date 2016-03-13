package com.example.smita.represent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.wearable.Wearable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends Activity implements

        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "88ABSXLfiwFxLgIZtDGXy5WBW";
    private static final String TWITTER_SECRET = "mGtvNU51lc5BlHUrbWSElDLSHSPyutcw8A29lXk3nRua4bhww6";

    private TwitterLoginButton loginButton;


    public String globalZip = "94704";
    public double LAT;
    public double LONG;
    String API_URL ="http://congress.api.sunlightfoundation.com/legislators/locate?";
    String API_KEY = "efc4cb36d34f424d9ca520bb227d3ff4";
    String GEO_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
    String GEO_KEY = "AIzaSyB2Wp_6QOUFGe9hXHVjuJVLYdzOZo9bMVU";
    JSONArray resultArray;
    JSONArray countyArray;
    Bundle bData = new Bundle();

    String countyname;
    String stateshort;
    String statelong;

    private GoogleApiClient mGoogleApiClient;
    public static String TAG = "GPSActivity";
    public static int UPDATE_INTERVAL_MS = 2000;
    public static int FASTEST_INTERVAL_MS = 1000;

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_INTERVAL_MS);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mGoogleApiClient, locationRequest, this)
                .setResultCallback(new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        if (status.getStatus().isSuccess()) {
                            Log.d(TAG, "Successfully requested");
                        } else {
                            Log.e(TAG, status.getStatusMessage());
                        }
                    }
                });

    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connResult) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

        if (bundle != null){
            if (!Fabric.isInitialized()) {
                TwitterAuthConfig authConfig2 = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
                Fabric.with(this, new Twitter(authConfig2));
            }

            if (bundle.containsKey("LAT")){
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Log.i("YOOO", "SHAKEING IT ALL UP");
                    Log.d("T", "IN DOWNLOADWEBPAGETASK22222");
                    LAT = Double.valueOf(bundle.getString("LAT"));
                    LONG = Double.valueOf(bundle.getString("LONG"));
                    Log.i("THIS IS IT",bundle.getString("LAT") + "," + bundle.getString("LONG"));
                    String[] urlAry = new String[2];
                    urlAry[0] = API_URL + "latitude=" + String.valueOf(LONG) + "&longitude=" + String.valueOf(LAT)+ "&apikey=" + API_KEY;
                    urlAry[1] = GEO_URL + "latlng=" + String.valueOf(LONG) + "," + String.valueOf(LAT)+ "&key=" + GEO_KEY;

                    new RetrieveJSONData().execute(urlAry);
                } else {
                    Log.d("No","NETWORK CONNECTION AVAILABLE");
                }

            }
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)  // used for data layer API
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }
    @Override
    public void onLocationChanged(Location location) {
        // Do some work here with the location you have received
        LAT = location.getLatitude();
        LONG = location.getLongitude();
    }

    public void useZip(View view){

        EditText zipText = (EditText) findViewById(R.id.zipCode);
        globalZip = zipText.getText().toString();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("T", "IN DOWNLOADWEBPAGETASK");
            String[] urlAry = new String[2];
            urlAry[0] = API_URL + "zip=" + globalZip + "&apikey=" + API_KEY;
            urlAry[1] = GEO_URL + "&address=" + globalZip + "&key=" + GEO_KEY;
            new RetrieveJSONData().execute(urlAry);
        } else {
            Log.d("No","NETWORK CONNECTION AVAILABLE");
        }
    }

    public void useLoc(View view){

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("T", "IN DOWNLOADWEBPAGETASK");

            String[] urlAry = new String[2];
            urlAry[0] = API_URL + "latitude=" + String.valueOf(LAT) + "&longitude=" + String.valueOf(LONG)+ "&apikey=" + API_KEY;
            urlAry[1] = GEO_URL + "latlng=" + String.valueOf(LAT) + "," + String.valueOf(LONG)+ "&key=" + GEO_KEY;

            new RetrieveJSONData().execute(urlAry);
        } else {
            Log.d("No","NETWORK CONNECTION AVAILABLE");
        }

    }

    class RetrieveJSONData extends AsyncTask<String,Void,String[]> {
        @Override
        protected String[] doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                String[] ary2 = new String[2];
                for (int k= 0; k < urls.length; k++) {
                    Log.i("We're in builder","noted");
                    ary2[k] = downloadUrl(urls[k],k);
                }
                return ary2;
            } catch (IOException e) {
                String[] error = new String[1];
                error[0] = "Unable to retrieve web page. URL may be invalid.";
                return error;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
                Log.d("T", "STRING RESULT IS: " + result[0]);
                Log.d("T", "STRING RESULT IS: " + result[1]);

                try {
                    JsonElement object = new JsonParser().parse(result[1]);
                    JsonObject value = object.getAsJsonObject();
                    JsonArray arry = value.get("results").getAsJsonArray();
                    JsonObject arry2 = arry.get(0).getAsJsonObject();
                    JsonArray objz = arry2.get("address_components").getAsJsonArray();
                    for (int p = 0; p < objz.size(); p++){
                        JsonObject temp = objz.get(p).getAsJsonObject();
                        String st =temp.get("types").getAsJsonArray().get(0).toString() ;
                        if(st.toString().equals("\"administrative_area_level_2\"")){
                            countyname = temp.get("long_name").getAsString();
                        }
                        if(st.toString().equals("\"administrative_area_level_1\"")){
                            stateshort = temp.get("short_name").getAsString();
                            statelong = temp.get("long_name").getAsString();
                        }

                    }

                    JSONObject jsonObject = new JSONObject(result[0]);
                    Log.d("T", "RIGHT HERE");
                    resultArray = jsonObject.getJSONArray("results");
                    int count = resultArray.length();
                    Log.d("THIS BE HERE", String.valueOf(count));

                    bData.putInt("length", count);
                    String service="";
                    for (int i = 0; i < count; i++){
                        String[] obj = new String[8];
                        obj[0] = resultArray.getJSONObject(i).getString("first_name") + " " + resultArray.getJSONObject(i).getString("last_name");
                        obj[1] = resultArray.getJSONObject(i).getString("title");
                        obj[2] = resultArray.getJSONObject(i).getString("party");
                        obj[3] = resultArray.getJSONObject(i).getString("website");
                        obj[4] = resultArray.getJSONObject(i).getString("oc_email");
                        obj[5] = resultArray.getJSONObject(i).getString("twitter_id");
                        obj[6] = resultArray.getJSONObject(i).getString("bioguide_id");
                        obj[7] = resultArray.getJSONObject(i).getString("term_end");

                        bData.putStringArray(String.valueOf(i), obj);
                        service = service + obj[0] + ":" + obj[1] + ":" + obj[2] + ":" + obj[6]+ ":" + obj[7]+";";
                    }


                    String s = loadJSON();
                    StringBuilder str = new StringBuilder();
                    try {
                        JSONObject jObj = new JSONObject(s);
                        Iterator<?> keyz = jObj.keys();
                        while (keyz.hasNext()) {
                            String key = (String) keyz.next();
                            String[] kys = key.split(", ");
                            if (kys[0].equals(countyname) && kys[1].equals(stateshort)) {
                                str.append(kys[0] + ":" + statelong + ":");
                                JSONObject val = jObj.getJSONObject(key);
                                str.append(val.getString("romney") + ":" + val.getString("obama"));
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        Log.d("T", "JSON exception");
                    }
                    service = service + str.toString();
                    Intent startIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                    startIntent.putExtra("data", service);
                    startIntent.putExtra("from", "data");
                    Intent sendIntent = new Intent(MainActivity.this, ViewPagerActivity.class);
                    sendIntent.putExtras(bData);
                    startActivity(sendIntent);
                    startService(startIntent);

                } catch (JSONException e) {
                    Log.d("T", "JSONException found and caught");
                }
            }
        }


    private String downloadUrl(String myurl, int k) throws IOException {
        // Do some validation here

        try {
            URL url = new URL(myurl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                String[] ary2 = new String[2];
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                Log.i("We got the data", "AND MOVING ON ");
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    public String loadJSON() {
        String json = null;
        try {

            InputStream is = getAssets().open("election_results_2012.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}