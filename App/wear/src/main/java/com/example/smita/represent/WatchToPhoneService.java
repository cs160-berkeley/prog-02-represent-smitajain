package com.example.smita.represent;

/**
 * Created by Smita on 3/1/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WatchToPhoneService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mWatchApiClient;
    private List<Node> nodes = new ArrayList<>();
    final Service _this = this;
    private String globalZip = "empty";;
    String extra1;

    @Override
    public void onCreate() {
        Log.i("THAT IT HAS BEEN SENT", "found nodes");
        super.onCreate();
        //initialize the googleAPIClient for message passing
        mWatchApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
                .build();
        //and actually connect it
        mWatchApiClient.connect();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWatchApiClient.disconnect();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
    public void onConnected(Bundle bundle) {
        Log.d("T", "in onconnected");
        Wearable.NodeApi.getConnectedNodes(mWatchApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        nodes = getConnectedNodesResult.getNodes();

                        Log.i("THAT IT HAS BEEN SENT", "found nodes");
                        //when we find a connected node, we populate the list declared above
                        //finally, we can send a message
                        //sendMessage("/send_toast", "Goodjob!");
                        //sendMessage("hello", "itsme");
//                        _this.stopSelf();
                    }
                });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Which cat do we want to feed? Grab this info from INTENT
        // which was passed over when we called startService
        final String extras = intent.getStringExtra("data");
        if (extras.equals("rando")){
            extra1 = intent.getStringExtra("COORDINATES");
        }

        // Send the message with the cat name
        new Thread(new Runnable() {
            @Override
            public void run() {
                //first, connect to the apiclient
                mWatchApiClient.connect();
                //now that you're connected, send a massage with the cat name
                //sendMessage("/" + "hello", globalZip);
                if (extras.equals("rando")){
                    sendMessage("/send_coord", extra1);
                }
                else{
                    sendMessage("/send_bread",extras);
                }


            }
        }).start();

        return START_STICKY;
    }

    @Override //we need this to implement GoogleApiClient.ConnectionsCallback
    public void onConnectionSuspended(int i) {}

    private void sendMessage(final String path, final String text ) {
        for (Node node : nodes) {
            Wearable.MessageApi.sendMessage(
                    mWatchApiClient, node.getId(), path, text.getBytes());
        }
    }

}
