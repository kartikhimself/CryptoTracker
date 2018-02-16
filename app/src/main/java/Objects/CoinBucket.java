package Objects;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class CoinBucket implements Serializable {

    private final String bucket;



    private JSONObject responseObj;
    private JSONObject coinsArray;
    private ArrayList<CoinItem> CoinList;

    public CoinBucket(String bucket) {

        this.bucket = bucket;
        setBucket();
    }


    public void setBucket() {

        CoinList = new ArrayList<CoinItem>();

        try {
            responseObj = new JSONObject(bucket);


            if(responseObj.has("Data")) {
                // offers
                coinsArray = responseObj.getJSONObject("Data");

                Iterator<?> keys = coinsArray.keys();

                // looping through All Coins

                while( keys.hasNext() ) {
                    String key = (String)keys.next();
                    if ( coinsArray.get(key) instanceof JSONObject ) {
                        // do what ever you want with the JSONObject.....
                        CoinItem tempItem = new CoinItem(coinsArray.getJSONObject(key));
                        CoinList.add(tempItem);

                    }
                }



            }





        }
        catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public ArrayList<CoinItem> getCoinList(){
        return this.CoinList;
    }


}