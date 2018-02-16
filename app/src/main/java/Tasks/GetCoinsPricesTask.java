package Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.MainPages.CTSearchCoin;
import com.evmcstudios.cryptotracker.MainPages.CTWatchList;

import java.util.List;

import HttpClient.HttpHandler;
import Objects.CoinItem;
import Utilities.Util;

/**
 * Created by edwardvalerio on 2/16/2018.
 */

public class GetCoinsPricesTask extends AsyncTask<Void,Void,String> {

    private CTWatchList activity;
    private List<CoinItem> list;

    public GetCoinsPricesTask(CTWatchList activity, List<CoinItem> list) {
        this.activity = activity;
        this.list = list;
    }


    @Override
    protected String doInBackground(Void... voids) {

        String coins = null;
        HttpHandler sh = new HttpHandler();

        // setup the list of strings

        String symbols = "";

        int i;
        for(i = 0; i < list.size(); i++) {
            CoinItem tempItem = list.get(i);

            if(list.size() == 1) {
                symbols +=  tempItem.getSymbol();
            }

            else if(list.size() > 1 && ((list.size()-i) != 1) ) {

                symbols +=  tempItem.getSymbol() + ",";
            }

            else if(  ((list.size()-i) == 1) ) {

                symbols +=  tempItem.getSymbol();
            }

        }

        Log.i("Symbols: " , symbols);



        // Making a request to url and getting response
        String url = Util.BaseMULTICURRENCY;
        coins = sh.makeServiceCall(url,"GET");
        return coins;

    }


    @Override
    protected void onPostExecute(String coinprices) {
        super.onPostExecute(coinprices);


        if(coinprices != null) {
            activity.setCoinsPrice(coinprices);
        }
        else {
            Toast.makeText(activity.getApplicationContext(), "Error loading offers", Toast.LENGTH_SHORT).show();
        }


        // cancel task here

        if(activity.PricesTask != null) {
            activity.PricesTask.cancel(true);
            activity.PricesTask = null;
        }





    }


}


