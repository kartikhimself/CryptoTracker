package Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Adapters.CoinsAdapter;
import Objects.CoinItem;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class Storage {

    private static final String STORAGECT = "CoinStorage";
    private ArrayList<CoinItem> savedCoinList = null;
    public static final String COINLIST = "CoinListKey";
    private Context context;
    SharedPreferences sharedpreferences;

    public Storage(Context context) {
        this.context = context;
        this.savedCoinList = new ArrayList<CoinItem>();

        initArray();
    }

    public void initArray() {

        List<CoinItem> savedList =  new ArrayList<>();

      if(getCoinList() != null) {
          savedList = getCoinList();
          savedCoinList.addAll(savedList);
      }

    }


    public void addCoin(CoinItem newCoin) {

         savedCoinList.add(newCoin);
         saveCoinList();

    }

    public void removeAllFroStaticList() {

        savedCoinList.clear();
    }

    public void saveCoinList() {

        // stringify json
        String coinJSONList = new Gson().toJson(savedCoinList);
        sharedpreferences = context.getSharedPreferences(STORAGECT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(COINLIST,coinJSONList);
        editor.apply();

    }

    public void clearCoins(CoinsAdapter currentAdapter){

        sharedpreferences = context.getSharedPreferences(STORAGECT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        removeAllFroStaticList();
        currentAdapter.clearCoins();


    }

    public List<CoinItem> getCoinList() {

        sharedpreferences = context.getSharedPreferences(STORAGECT, Context.MODE_PRIVATE);
        String coinJSONList = sharedpreferences.getString(COINLIST,"");
        Type type = new TypeToken<List<CoinItem>>(){}.getType();

        List<CoinItem> newCoinList = new ArrayList<>();
        newCoinList =  new Gson().fromJson(coinJSONList, type );

        if(newCoinList != null) {
            return newCoinList;
        }
        else {
            return null;
        }




    }


}