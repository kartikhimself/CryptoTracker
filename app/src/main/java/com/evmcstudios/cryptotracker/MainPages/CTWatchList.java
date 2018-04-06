package com.evmcstudios.cryptotracker.MainPages;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.evmcstudios.cryptotracker.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.CoinsAdapter;
import Objects.CoinItem;
import Utilities.Referrer;
import Utilities.Util;
import servicesarea.MyFirebaseInstanceIDService;
import Tasks.GetCoinsPricesTask;
import Utilities.Sorting;
import Utilities.Storage;

/**
 * Created by edwardvalerio on 2/14/2018.
 */

public class CTWatchList extends AppCompatActivity{


    public Storage StoredCoins = null;
    private CoinsAdapter mainCoinsAdapter = null;
    private ListView MainCoinListView;
    private TextView Balance;
    private SwipeRefreshLayout refreshLayout;
    private MyFirebaseInstanceIDService service;
    private FirebaseAnalytics FirebaseAnalytics;
    private InterstitialAd mInterstitialAd;

    public GetCoinsPricesTask PricesTask = null;
    public Referrer referrerObj = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ct_watchlist_page);

        FirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseAnalytics.setCurrentScreen(this, getString(R.string.app_watchlist), null );

        service = new MyFirebaseInstanceIDService();

        service.onTokenRefresh();
        Log.i("TOKEN" ,  "" + service.getToken());

       // referrer retrieval

        referrerObj = new Referrer(getApplicationContext());

        Log.i("Retreival " , referrerObj.getFinalQueryString());



        // advertisement
        MobileAds.initialize(this, getString(R.string.AdMobID));

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.AdUnitInterstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        // balance

        Balance = findViewById(R.id.portfolio_balance);

        // list view & refresh for coins

        MainCoinListView = (ListView) findViewById(R.id.mainCoinsListView);
        MainCoinListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        MainCoinListView.setItemsCanFocus(false);
        registerForContextMenu(MainCoinListView);

        setListViewListeners();




        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                               @Override
                                               public void onRefresh() {

                                                   setWatchList();
                                               }

                                           });

        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark,R.color.colorAccent,R.color.colorAccent);


        // stored coins

        StoredCoins = new Storage(getApplicationContext());

        setWatchList();

        // toolbar stuff

         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_watch);
         setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.logo_plain_no_effect);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        FloatingActionButton SearchStart = (FloatingActionButton) findViewById(R.id.search_action);
        SearchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendCustomEvent("Coin_Search","Started", "Searching");
                Intent searchCoin = new Intent(getApplicationContext(), CTSearchCoin.class);
                startActivityForResult(searchCoin, 1);

            }
        });


        FloatingActionButton DeleteCoins = (FloatingActionButton) findViewById(R.id.clear_action);
        DeleteCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  StoredCoins.clearCoins(mainCoinsAdapter,Balance);
                  Snackbar.make(view, "Deleted coins", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


         // search for coin
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                CoinItem finalCoin = (CoinItem) data.getSerializableExtra("SelectedCoin");
                StoredCoins.addCoin(finalCoin);
                setWatchList();

                sendCustomEvent("Coin_Added", finalCoin.getID(), finalCoin.getTitle());


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }


        // updated coin

        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                CoinItem finalCoin = (CoinItem) data.getSerializableExtra("SelectedCoin");
                StoredCoins.updateCoin(finalCoin);
                setWatchList();

                sendCustomEvent("Coin_Added",finalCoin.getID(), finalCoin.getTitle());
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

                mainCoinsAdapter.filterList(newText, mainCoinsAdapter);
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
        if (id == R.id.action_about) {

            Intent aboutUs = new Intent(getApplicationContext(), CTAbout.class);
            startActivity(aboutUs);
            return true;
        }

        if(id == R.id.action_sort) {


            Sorting sort = new Sorting(CTWatchList.this);

        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.mainCoinsListView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.list_view_menu, menu);
        }

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {

            case R.id.action_edit:

                editCoinAt(info.position);

                return true;
            case R.id.action_delete:

                deleteCointAt(info.position);
                setWatchList();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }




    public void setWatchList() {


        refreshLayout.setRefreshing(true);

   if(StoredCoins.getCoinList() != null) {

     ArrayList<CoinItem> newArr = new ArrayList<>();
     newArr.addAll(StoredCoins.getCoinList());

      if(mainCoinsAdapter == null) {
        mainCoinsAdapter = new CoinsAdapter(getApplicationContext(), (ArrayList<CoinItem>) StoredCoins.getCoinList());
        MainCoinListView.setAdapter(mainCoinsAdapter);
        mainCoinsAdapter.notifyDataSetChanged();
          }


      getPrices(StoredCoins.getCoinList());


        }

   else {

       refreshLayout.setRefreshing(false);

          }

   }


    public void getPrices(List<CoinItem> list) {

            if(PricesTask == null) {
                PricesTask= new GetCoinsPricesTask(CTWatchList.this, list);
                PricesTask.execute((Void) null);
            }


    }


    public void setCoinsPrice(String coinprices) {

        StoredCoins.setCoinsPrices(coinprices, mainCoinsAdapter, Balance );



        delayeRefresh(refreshLayout);


    }

    public void editCoinAt(int position) {





        Intent updateCoin = new Intent(getApplicationContext(), CTCoinDetails.class);
        CoinItem selectedCoin =  mainCoinsAdapter.getAdapterCoinList().get(position);   // StoredCoins.getCoinList().get(position);
        updateCoin.putExtra("SelectedCoin", selectedCoin);



        sendCustomEvent("Coin_details_click", selectedCoin.getID(), selectedCoin.getTitle());

        // start ad AND CHECK for loading - start activity if ads not ready

        showAd(updateCoin);

      // startActivityForResult(updateCoin, 2);

    }

    public void deleteCointAt(int position) {


        CoinItem selectedCoin =  mainCoinsAdapter.getAdapterCoinList().get(position);

        sendCustomEvent("Coin_Deleted",selectedCoin.getID(), selectedCoin.getTitle());

        StoredCoins.deleteCoin(selectedCoin.getID());

    }

    public void delayeRefresh(final SwipeRefreshLayout refresher) {

        Timer time = new Timer();

        TimerTask action = new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        refresher.setRefreshing(false);

                    }
                });



            }
        };

        time.schedule(action, 1000);

    }


    public void setListViewListeners() {


        MainCoinListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                editCoinAt(position);

            }

        });






    }


    public void setSortingCondition(Integer condition) {

        StoredCoins.saveSortingCondition(condition);

        setWatchList();



    }


    public void sendCustomEvent(String event, String action, String label) {

        Bundle bundle = new Bundle();
        bundle.putString(com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_ID, action);
        bundle.putString(com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_NAME, label);
        FirebaseAnalytics.logEvent(event , bundle);


    }

    public void showAd(final Intent updateCoin) {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();


            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    startActivityForResult(updateCoin, 2);
                }



            });



        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");

            startActivityForResult(updateCoin, 2);

        }




    }


}
