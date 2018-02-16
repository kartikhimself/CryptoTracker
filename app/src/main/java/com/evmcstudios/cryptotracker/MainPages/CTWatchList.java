package com.evmcstudios.cryptotracker.MainPages;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.evmcstudios.cryptotracker.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.CoinsAdapter;
import Objects.CoinItem;
import Tasks.GetCoinsPricesTask;
import Utilities.Storage;

/**
 * Created by edwardvalerio on 2/14/2018.
 */

public class CTWatchList extends AppCompatActivity{


    public Storage StoredCoins = null;
    private CoinsAdapter mainCoinsAdapter = null;
    private ListView MainCoinListView;
    private ArrayList<CoinItem> currentCoins;
    public GetCoinsPricesTask PricesTask = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ct_watchlist_page);

        // list view for coins

        MainCoinListView = (ListView) findViewById(R.id.mainCoinsListView);

        // stored coins

        StoredCoins = new Storage(getApplicationContext());

        setWatchList();

        // toolbar stuff

         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_watch);
         setSupportActionBar(toolbar);


        FloatingActionButton SearchStart = (FloatingActionButton) findViewById(R.id.search_action);
        SearchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent searchCoin = new Intent(getApplicationContext(), CTSearchCoin.class);
                startActivityForResult(searchCoin, 1);



            }
        });


        FloatingActionButton DeleteCoins = (FloatingActionButton) findViewById(R.id.clear_action);
        DeleteCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                  StoredCoins.clearCoins(mainCoinsAdapter);
                  Snackbar.make(view, "Deleted coins", Snackbar.LENGTH_LONG).setAction("Action", null).show();




            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                CoinItem finalCoin = (CoinItem) data.getSerializableExtra("SelectedCoin");

                Snackbar.make(findViewById(R.id.action_search), finalCoin.getTitle(), Snackbar.LENGTH_LONG).setAction("Coin", null).show();

                StoredCoins.addCoin(finalCoin);

                setWatchList();



            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ctmain, menu);



        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =  (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

               // coinsAdapter.filterList(newText, coinsAdapter);
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void setWatchList() {


   if(StoredCoins.getCoinList() != null) {

     ArrayList<CoinItem> newArr = new ArrayList<>();
     newArr.addAll(StoredCoins.getCoinList());

      if(mainCoinsAdapter == null) {

        mainCoinsAdapter = new CoinsAdapter(getApplicationContext(), (ArrayList<CoinItem>) StoredCoins.getCoinList());
        MainCoinListView.setAdapter(mainCoinsAdapter);
        mainCoinsAdapter.notifyDataSetChanged();

          }
      else {

        mainCoinsAdapter.updateCoins(StoredCoins.getCoinList());

          }


        getPrices(StoredCoins.getCoinList());





        }

    }


    public void getPrices(List<CoinItem> list) {

            if(PricesTask == null) {
                PricesTask= new GetCoinsPricesTask(CTWatchList.this, list);
                PricesTask.execute((Void) null);
            }


    }


    public void setCoinsPrice(String coinprices) {


        StoredCoins.setCoinsPrices(coinprices, mainCoinsAdapter);


    }


}
