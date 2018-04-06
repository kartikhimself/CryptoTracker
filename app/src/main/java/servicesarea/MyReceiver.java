package servicesarea;



 import android.content.BroadcastReceiver;
 import android.content.Context;
 import android.content.Intent;
 import android.util.Log;

 import com.google.android.gms.analytics.CampaignTrackingReceiver;

 import java.io.UnsupportedEncodingException;
 import java.net.URLDecoder;
 import Utilities.Util;

/*
 *  A simple Broadcast Receiver to receive an INSTALL_REFERRER
 *  intent and pass it to other receivers, including
 *  the Google Analytics receiver.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Pass the intent to other receivers.
        String referrer = intent.getStringExtra("referrer");

        try {
            Util.referrer = URLDecoder.decode( referrer, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        // When you're done, pass the intent to the Google Analytics receiver.
        new CampaignTrackingReceiver().onReceive(context, intent);


    }
}