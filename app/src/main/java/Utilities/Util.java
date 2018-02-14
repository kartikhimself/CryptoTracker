package Utilities;

import android.text.Html;

/**
 * Created by Ultranova on 2/10/2018.
 */

public class Util {
    public static String BaseURL = "https://www.cryptocompare.com";
    public static String BaseAPI = "https://min-api.cryptocompare.com/data/";
    public static String BaseCOINLIST = "https://min-api.cryptocompare.com/data/all/coinlist";



    public static String getPlainText(String html) {

        String temp;
        temp = Html.fromHtml(html).toString();;
        return temp;

    }

}
