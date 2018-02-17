package com.evmcstudios.cryptotracker.MainPages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.evmcstudios.cryptotracker.R;
import com.squareup.picasso.Picasso;

import Objects.CoinItem;

/**
 * Created by Ultranova on 2/17/2018.
 */

public class CTCoinDetails extends AppCompatActivity {

    private CoinItem selectedCoin;
    private EditText quantity;
    private Button updatebtn;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ct_details_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        // get the data

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        selectedCoin =  (CoinItem) bundle.getSerializable("SelectedCoin");


        // set the views

        ImageView mainImage = findViewById(R.id.coin_image);


        // load image


        quantity = findViewById(R.id.quantity);
        updatebtn = findViewById(R.id.update_btn);

         quantity.setText(selectedCoin.getQuantity());






        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateCoin(quantity);

            }
        });


        Picasso.with(getApplicationContext()).load(selectedCoin.getImageUrl()).fit().centerCrop().into(mainImage);

    }



    public void updateCoin(EditText quantity) {


        String value = quantity.getText().toString();

        Snackbar.make(findViewById(R.id.coin_image) , value, Snackbar.LENGTH_SHORT).setAction("Action", null).show();

        selectedCoin.setQuantity(value);


        Intent returnIntent = new Intent();
        returnIntent.putExtra("SelectedCoin", selectedCoin);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();


    }


}
