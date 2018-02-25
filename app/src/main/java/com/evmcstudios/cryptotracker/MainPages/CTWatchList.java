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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.CoinsAdapter;
import Objects.CoinItem;
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

    public GetCoinsPricesTask PricesTask = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ct_watchlist_page);

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

        startActivityForResult(updateCoin, 2);

    }

    public void deleteCointAt(int position) {


        CoinItem selectedCoin =  mainCoinsAdapter.getAdapterCoinList().get(position);
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


        /*

        MainCoinListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Capture ListView item click
        MainCoinListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = MainCoinListView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_about:

                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.list_view_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });

            */



    }


    public void setSortingCondition(Integer condition) {

        StoredCoins.saveSortingCondition(condition);

        setWatchList();



    }


}
