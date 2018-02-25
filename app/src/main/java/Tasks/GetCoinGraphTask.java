package Tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.MainPages.CTSearchCoin;

import java.lang.ref.WeakReference;

import CoinSubPages.CoinGraphsPage;
import HttpClient.HttpHandler;
import Utilities.Util;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class GetCoinGraphTask extends AsyncTask<Void,Void,String> {

    private WeakReference<CoinGraphsPage> fragment;
    private String symbol;
    private String range;

    public GetCoinGraphTask(CoinGraphsPage fragment, String symbol, String range) {
        this.fragment = new WeakReference<CoinGraphsPage>(fragment);
        this.symbol = symbol;
        this.range = range;
    }


    @Override
    protected String doInBackground(Void... voids) {

        String datapoints = null;
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url = null;

        switch (range) {

            case "1H":
                url = Util.BaseGRAPHMin + symbol;
                break;

            case "24H":
                url = Util.BaseGRAPHHour + symbol;
                break;

            case "30D":
                url = Util.BaseGRAPHDay + symbol;
                break;

            default:
                url = Util.BaseGRAPHMin + symbol;
                break;

        }


        datapoints = sh.makeServiceCall(url,"GET");
        return datapoints;
    }


    @Override
    protected void onPostExecute(String datapoints) {
        super.onPostExecute(datapoints);

        if(datapoints != null) {

            fragment.get().onTaskComplete(datapoints);

        }
        else {
            Toast.makeText(fragment.get().getActivity().getApplicationContext(), "Error loading graphs", Toast.LENGTH_SHORT).show();
        }

        // cancel task here

        if(fragment.get().graphTask  != null) {
            fragment.get().graphTask.cancel(true);
            fragment.get().graphTask = null;
        }




    }




}
