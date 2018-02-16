package com.evmcstudios.cryptotracker.MainPages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.evmcstudios.cryptotracker.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class CTPreloader extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.content_ctpreloader);
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
