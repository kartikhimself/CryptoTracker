package com.evmcstudios.cryptotracker.MainPages;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.R;

/**
 * Created by edwardvalerio on 3/19/2018.
 */

public class CTPrivacyPolicy extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_ctprivacy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_watch);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.setTitle(R.string.app_privacy_policy);

        // Web View

        WebView ppViewWeb = (WebView) findViewById(R.id.privacy_view);




        ppViewWeb.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        ppViewWeb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });

        ppViewWeb.loadUrl(getString(R.string.app_purl));



    }
}
