package com.evmcstudios.cryptotracker.MainPages;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.R;

import Adapters.CoinsAdapter;
import Objects.CoinBucket;
import Tasks.GetCoinsListTask;
import Utilities.Util;

public class CTMain extends AppCompatActivity {

    public GetCoinsListTask CoinListTask = null;
    private CoinsAdapter coinsAdapter;
    private ListView coinListView;
    private LinearLayout progressBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ct_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // set listview

        coinListView = (ListView) findViewById(R.id.coinListView);
        progressBarLayout = (LinearLayout) findViewById(R.id.progressBarLayout);



        showProgress(true);
        getCoins();

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




                    coinsAdapter.filterList(newText, coinsAdapter);



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


    public void getCoins() {

        if(CoinListTask == null) {
            CoinListTask = new GetCoinsListTask(CTMain.this);
            CoinListTask.execute((Void) null);
        }

    }

    public void setCoins(String coins) {

       CoinBucket coin_bucket =  new CoinBucket(coins);

        coinsAdapter = new CoinsAdapter(getApplicationContext(), coin_bucket.getCoinList());
        coinListView.setAdapter(coinsAdapter);

        showProgress(false);

    }

    public void showProgress(boolean show) {




        progressBarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        coinListView.setVisibility(show ? View.GONE : View.VISIBLE);


    }



}
