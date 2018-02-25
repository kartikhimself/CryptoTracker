package Tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.MainPages.CTSearchCoin;

import java.lang.ref.WeakReference;

import HttpClient.HttpHandler;
import Utilities.Util;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class GetCoinsListTask extends AsyncTask<Void,Void,String> {

     private WeakReference<CTSearchCoin> activity;
     public GetCoinsListTask(CTSearchCoin activity) {
        this.activity = new WeakReference<CTSearchCoin>(activity);
    }



    @Override
    protected String doInBackground(Void... voids) {

        String coins = null;
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url = Util.BaseCOINLIST;
        coins = sh.makeServiceCall(url,"GET");
        return coins;
    }


    @Override
    protected void onPostExecute(String coins) {
        super.onPostExecute(coins);

        if(coins != null) {
            activity.get().setCoins(coins);
        }
        else {
            Toast.makeText(activity.get().getApplicationContext(), "Error loading coin list", Toast.LENGTH_SHORT).show();
        }



        // cancel task here

        if(activity.get().CoinListTask  != null) {
            activity.get().CoinListTask.cancel(true);
            activity.get().CoinListTask = null;
        }




    }




}
