package Utilities;

import android.text.Html;

import java.text.DecimalFormat;

/**
 * Created by Ultranova on 2/10/2018.
 */

public class Util {

    public static String CURRENCY = "USD";
    public static String BaseURL = "https://www.cryptocompare.com";
    public static String BaseAPI = "https://min-api.cryptocompare.com/data/";
    public static String BaseCOINLIST = "https://min-api.cryptocompare.com/data/all/coinlist";
    public static String BaseMULTICURRENCY = "https://min-api.cryptocompare.com/data/pricemulti?tsyms="+ CURRENCY +"&fsyms=";
    public static String BaseGRAPHMin = "https://min-api.cryptocompare.com/data/histominute?tsym="+ CURRENCY +"&limit=60&fsym=";
    public static String BaseGRAPHHour = "https://min-api.cryptocompare.com/data/histohour?tsym="+ CURRENCY +"&limit=8&fsym=";
    public static String BaseGRAPHDay = "https://min-api.cryptocompare.com/data/histoday?tsym="+ CURRENCY +"&limit=7&fsym=";
    public static String defaultRange = "1H";

    // text stuff

    public static String NUMBERPATTERN = "###,###,###,###,###.###";


    public static String getPlainText(String html) {

        String temp;
        temp = Html.fromHtml(html).toString();;
        return temp;


    }

    public static String getFormattedNumber(Double number){

        DecimalFormat decimalFormatter = new DecimalFormat(Util.NUMBERPATTERN);
        decimalFormatter.setMinimumFractionDigits(2);
        decimalFormatter.setMaximumFractionDigits(3);

       return decimalFormatter.format(number);


    }

}
