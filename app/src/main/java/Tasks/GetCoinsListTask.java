package Tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.MainPages.CTSearchCoin;

import HttpClient.HttpHandler;
import Utilities.Util;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class GetCoinsListTask extends AsyncTask<Void,Void,String> {

     private CTSearchCoin activity;

     public GetCoinsListTask(CTSearchCoin activity) {
        this.activity = activity;
    }



    @Override
    protected String doInBackground(Void... voids) {

        String offers = null;
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url = Util.BaseCOINLIST;
        offers = sh.makeServiceCall(url,"GET");
        return offers;
    }


    @Override
    protected void onPostExecute(String coins) {
        super.onPostExecute(coins);

        if(coins != null) {
             activity.setCoins(coins);
        }
        else {
            Toast.makeText(activity.getApplicationContext(), "Error loading offers", Toast.LENGTH_SHORT).show();
        }



        // cancel task here

        if(activity.CoinListTask  != null) {
            activity.CoinListTask.cancel(true);
            activity.CoinListTask = null;
        }




    }




}
