package Objects;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import Utilities.Util;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class CoinItem implements Serializable {

    private transient JSONObject coin;
    private String COIN_ID;
    private String TITLE;
    private String QUANTITY = "0";
    private String SYMBOL;
    private Double PRICE = Double.parseDouble("0");
    private String IMAGE = "-";


    public CoinItem(JSONObject coin){
        this.coin = coin;
        setCoinDetail();
    }

    public  void setCoinDetail(){

        try {
            COIN_ID = coin.getString("Id");
            TITLE  = coin.getString("CoinName");
            if(coin.has("ImageUrl")) {
                IMAGE = coin.getString("ImageUrl");
            }
            SYMBOL = coin.getString("Symbol");

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public String getID(){
        return this.COIN_ID;
    }

    public String getTitle(){
        return this.TITLE;
    }

    public String getImageUrl(){
        String URL = Util.BaseURL + this.IMAGE;
        return URL;
    }

    public String getSymbol(){
        return this.SYMBOL;
    }

    public void setPrice(Double price) {

        this.PRICE = price;

    }

    public void setQuantity(String quantity) {

        this.QUANTITY = quantity;
    }

    public Double getPrice() {

        return this.PRICE;

    }

    public String getQuantity(){
        return this.QUANTITY;
    }

    public Double getTotalValue() {

        Double value = Integer.parseInt(this.QUANTITY) * this.PRICE;
        return value;

    }




}