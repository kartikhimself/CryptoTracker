package Utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class Storage {

    public static final String STORAGEPS = "CoinStorage";
    public static final String COOKIE = "CoinKey";
    public static final String COINACCOUNT = "CoinListKey";
    public String savedCookie = "";
    public String savedAccount = "";
    private Context context;
    SharedPreferences sharedpreferences;

    public Storage(Context context) {
        this.context = context;
    }



    public void saveCookie(String mCookie) {

        sharedpreferences = context.getSharedPreferences(STORAGEPS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(COOKIE, mCookie);
        editor.apply();


    }

    public void saveUserAccount(String mAccount) {

        sharedpreferences = context.getSharedPreferences(STORAGEPS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(COINACCOUNT, mAccount);
        editor.apply();


    }

    public void clearCookie(){

        sharedpreferences = context.getSharedPreferences(STORAGEPS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();

    }

    public String getCookie() {

        sharedpreferences = context.getSharedPreferences(STORAGEPS, Context.MODE_PRIVATE);
        savedCookie = sharedpreferences.getString(COOKIE,"");

        return savedCookie;

    }

    public String getUserAccount() {

        sharedpreferences = context.getSharedPreferences(STORAGEPS, Context.MODE_PRIVATE);
        savedAccount = sharedpreferences.getString(COINACCOUNT,"");

        return savedAccount;

    }


}