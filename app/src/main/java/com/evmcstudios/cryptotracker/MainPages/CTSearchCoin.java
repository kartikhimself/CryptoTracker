package com.evmcstudios.cryptotracker.MainPages;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import com.evmcstudios.cryptotracker.R;

import Adapters.CoinsAdapter;
import Objects.CoinBucket;
import Objects.CoinItem;
import Tasks.GetCoinsListTask;

public class CTSearchCoin extends AppCompatActivity {

    public GetCoinsListTask CoinListTask = null;
    private CoinsAdapter coinsAdapter;
    private ListView coinListView;
    private LinearLayout progressBarLayout;
    private SearchView coinSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ct_search_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        coinSearchView =  (SearchView) findViewById(R.id.search_view_field);

        // set listview

        coinListView = (ListView) findViewById(R.id.coinListView);
        progressBarLayout = (LinearLayout) findViewById(R.id.progressBarLayout);



        showProgress(true);
        getCoins();

    }




    public void getCoins() {

        if(CoinListTask == null) {
            CoinListTask = new GetCoinsListTask(CTSearchCoin.this);
            CoinListTask.execute((Void) null);
        }

    }

    public void setCoins(String coins) {

        final CoinBucket coin_bucket =  new CoinBucket(coins);
        coinsAdapter = new CoinsAdapter(getApplicationContext(), coin_bucket.getCoinList());
        coinListView.setAdapter(coinsAdapter);
        showProgress(false);


        // set search Listener

        coinSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
               return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                coinsAdapter.filterList(newText, coinsAdapter);
                return true;
            }
        });

        // request focus
        coinSearchView.setFocusable(true);
      //  coinSearchView.setIconified(false);
       // coinSearchView.requestFocusFromTouch();


        // listen for Coin Click


        coinListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {



                CoinItem selectedCoin = coin_bucket.getCoinList().get(position);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("SelectedCoin",selectedCoin);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();



            }

        });


    }

    public void showProgress(boolean show) {




        progressBarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        coinListView.setVisibility(show ? View.GONE : View.VISIBLE);


    }



}
