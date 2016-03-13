package com.example.smita.represent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class Details extends Activity {
    TextView name;
    TextView party;
    TextView comm;
    TextView bills;
    TextView term;
    TextView role;
    Bundle bundleData;
    Bitmap imgur;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        bundleData = getIntent().getExtras();

        String memberId = bundleData.getString("personId");
        Log.d("THIS BE IT", memberId);

        Log.d("T", "BEFORE CONNECTIVITY CHECK");
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("T", "IN DOWNLOADWEBPAGETASK");
            String[] urlAry = new String[2];
            urlAry[0] = "http://congress.api.sunlightfoundation.com/committees?member_ids=" + memberId + "&apikey=3d4edffb86e547eabb5402be5f52e315";
            urlAry[1] = "http://congress.api.sunlightfoundation.com/bills?sponsor_id=" + memberId + "&apikey=3d4edffb86e547eabb5402be5f52e315";
            //urlAry[2] = "http://theunitedstates.io/images/congress/225x275/" + memberId + ".jpg";
            new RetrieveJSONData().execute(urlAry);
        }else{
            Log.d("T", "No network connection available.");
        }

        name = (TextView) findViewById(R.id.Name);
        term = (TextView) findViewById(R.id.term);
        role = (TextView) findViewById(R.id.role);
        party = (TextView) findViewById(R.id.partySen);
        comm = (TextView) findViewById(R.id.comm);
        bills = (TextView) findViewById(R.id.bill);
        img = (ImageView) findViewById(R.id.pic);


    }
    private class RetrieveJSONData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                StringBuilder join = new StringBuilder();
                for (int i = 0; i < urls.length; i++) {
                    join.append(downloadUrl(urls[i], i));
                    Log.i("Send message", "why you hate me" + i);
                }
                return join.toString();

            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.i("WE EHRE", "IN POST EXECUTE");
            String[] Jsons = result.split("\n");
            try{
                Log.i("BUT REALLY", "WHAT IT DO");
                StringBuilder committee = new StringBuilder();
                JSONObject jCommObject = new JSONObject(Jsons[0]);

                JSONArray Comm = jCommObject.getJSONArray("results");
                int i = Comm.length();
                Log.i("WHAT THE VALUE B ", String.valueOf(i));
                int j = 0;
                while(j < i){
                    JSONObject iComm = Comm.getJSONObject(j);
                    committee.append((j + 1) + ". " + iComm.getString("name") + "\n");
                    j++;
                    Log.i("BUT REALLY", "IN HERE AGAIN");
                }
                StringBuilder bill = new StringBuilder();
                JSONObject jBillObject = new JSONObject(Jsons[1]);
                JSONArray Bill = jBillObject.getJSONArray("results");
                int l = jBillObject.length();
                int k = 0;
                while(k < l){
                    JSONObject iBill = Bill.getJSONObject(k);
                    bill.append((k + 1) + ". " + iBill.getString("official_title") + "\n");
                    String[] date = iBill.getString("introduced_on").split("-");
                    bill.append("Date Introduced: " + date[2]+"."+date[2]+"."+date[0] + "\n \n");
                    k++;
                    Log.i("BUT REALLY", "IN HERE AGAIN2222");
                }
                String strinz = committee.toString();
                Log.i("BUT REALLY LOOK AT ME", strinz);

                comm.setText(strinz);
                bills.setText(bill.toString());
                name.setText(bundleData.getString("rep_name"));
                if (bundleData.getString("party").equals("D")){
                    party.setText("Democrat");
                }else if(bundleData.getString("party").equals("R")){
                    party.setText("Republican");
                }else{
                    party.setText("Independant");
                }

                String[] termsp = bundleData.getString("term").split("-");
                term.setText("Term End Date: " + termsp[2] + "-" + termsp[1] + "-" + termsp[0]);

                if (bundleData.getString("role").equals("Sen")){
                    role.setText("Senator");
                }
                else{
                    role.setText("Representative");
                }
                setImages(bundleData.getString("personId"),img);

            }catch (JSONException e) {
                Log.d("T", "JSONException found and caught");
            }
        }


        private String downloadUrl(String myurl, int k) throws IOException {
            // Do some validation here

            try {
                URL url = new URL(myurl);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
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
    }

    public void setImages(String id, ImageView img) {

        String url = "https://theunitedstates.io/images/congress/225x275/"+id+".jpg";
        Picasso.with(getApplicationContext()).load(url).into(img);
    }

}
