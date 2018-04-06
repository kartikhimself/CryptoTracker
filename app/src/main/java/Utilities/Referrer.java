package Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Ultranova on 4/5/2018.
 */



public class Referrer {


    private static final String FKEY = "referrerkey";
    private static final String subaff1 = "utm_campaign";
    private static final String subaff2 = "utm_source";
    private static final String subaff3 = "utm_content";
    private static final String subaff4 = "utm_term";
    private static final String subaff5 = "utm_expid";
    SharedPreferences sharedpref;
    private Context context;

    public Referrer(Context context) {
        this.context = context;
    }



    public void saveOriginalReferrer(String ref) {

        sharedpref = context.getSharedPreferences(FKEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString(FKEY,ref);
        editor.apply();

    }

    public String getOriginalReferrer() {
        sharedpref = context.getSharedPreferences(FKEY, Context.MODE_PRIVATE);
        String ref = sharedpref.getString(FKEY,"");
        return ref;
    }

    public String getFinalQueryString() {

        String finalString = "";
        String query = getOriginalReferrer();

        if(!query.contains("?")) {
            query = "?" + query;

           }

        Uri uri= Uri.parse(query);

       String s1 = uri.getQueryParameter(subaff1);
       String s2 = uri.getQueryParameter(subaff2);
       String s3 = uri.getQueryParameter(subaff3);
       String s4 = uri.getQueryParameter(subaff4);
       String s5 = uri.getQueryParameter(subaff5);

        if (s1 != null) finalString += ( !s1.equals("") ? "&subaff1=" + s1 : s1);
        if (s2 != null) finalString += ( !s2.equals("") ? "&subaff2=" + s2 : s2);
        if (s3 != null) finalString += ( !s3.equals("") ? "&subaff3=" + s3 : s3);
        if (s4 != null) finalString += ( !s4.equals("") ? "&subaff4=" + s4 : s4);
        if (s5 != null) finalString += ( !s5.equals("") ? "&subaff5=" + s5 : s5);



        return finalString;
    }


}
