package com.evmcstudios.cryptotracker.MainPages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TabLayout;


import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.widget.EditText;


import com.evmcstudios.cryptotracker.R;


import Adapters.CoinsPagerAdapter;
import CoinSubPages.CoinGraphsPage;
import CoinSubPages.CoinQuantityPage;
import Objects.CoinItem;

/**
 * Created by Ultranova on 2/17/2018.
 */

public class CTCoinDetails extends AppCompatActivity {

    public CoinItem selectedCoin;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.mipmap.ic_quantity,
            R.mipmap.ic_graphs,

    };

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ct_details_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // setup view pagers

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();

        // get the data

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        selectedCoin =  (CoinItem) bundle.getSerializable("SelectedCoin");

        // set Title

        this.setTitle(selectedCoin.getTitle() + " details");



    }

    public void setUpTabIcons() {


        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);



    }


    private void setupViewPager(ViewPager viewPager) {
        CoinsPagerAdapter adapter = new CoinsPagerAdapter(getSupportFragmentManager());

        CoinQuantityPage quantityPage = new CoinQuantityPage();
        CoinGraphsPage graphsPage = new CoinGraphsPage();

        adapter.addFragment(quantityPage,getString(R.string.quantity));
        adapter.addFragment(graphsPage, getString(R.string.graphs));

        viewPager.setAdapter(adapter);
    }

    public void updateCoin(EditText quantity) {

        String value = quantity.getText().toString();

        if(value.equals("")) {
            value = "0";
        }

        selectedCoin.setQuantity(value);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("SelectedCoin", selectedCoin);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }


}
