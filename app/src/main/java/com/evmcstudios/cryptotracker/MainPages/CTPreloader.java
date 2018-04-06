package com.evmcstudios.cryptotracker.MainPages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.evmcstudios.cryptotracker.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Timer;
import java.util.TimerTask;

import Utilities.Referrer;

import Utilities.Util;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class CTPreloader extends AppCompatActivity {

    private  FirebaseAnalytics mFirebaseAnalytics;
    public Referrer referrerObj = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.content_ctpreloader);

        mFirebaseAnalytics = mFirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,"Loading", null );


        // referrer stuff

       if(Util.referrer.length() > 0) {

           Log.i("Reff" , " - " + Util.referrer);
           referrerObj = new Referrer(getApplicationContext());
           referrerObj.saveOriginalReferrer(Util.referrer);

       }


         goToMainPage(getApplicationContext());



         }



     public void goToMainPage(final Context context) {


       Timer timer = new Timer();

       TimerTask action = new TimerTask() {
           @Override
           public void run() {

               Intent mainPage = new Intent(context, CTWatchList.class );
               startActivity(mainPage);
               finish();
           }
       };


       timer.schedule(action, 2500);






     }


}
