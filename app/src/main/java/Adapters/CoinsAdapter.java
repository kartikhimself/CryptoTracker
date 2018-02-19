package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import Objects.CoinItem;
import Utilities.Util;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class CoinsAdapter extends ArrayAdapter<CoinItem> implements Filterable {

    private List<CoinItem> coinItem;
    public  ArrayList<CoinItem> tempArray;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView Title;
        TextView Amount;
        TextView Symbol;
        TextView Quantity;
        TextView Total;
        ImageView Image;
    }


    public CoinsAdapter(Context context, ArrayList<CoinItem> data ) {
        super(context, R.layout.lv_all_coins , data);

        this.coinItem = data;
        this.mContext = context;

        setTempArray(data);
    }

    public void  setTempArray(ArrayList<CoinItem> data) {

        tempArray = new ArrayList<>();
        tempArray.addAll(data);

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
       // OfferItem currentOffer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.lv_all_coins, parent, false);
            viewHolder.Title = (TextView) convertView.findViewById(R.id.coinName);
            viewHolder.Symbol = (TextView) convertView.findViewById(R.id.coinSymbol);
            viewHolder.Image = (ImageView) convertView.findViewById(R.id.coinImg);
            viewHolder.Amount = (TextView) convertView.findViewById(R.id.coinPrice);
            viewHolder.Total = (TextView) convertView.findViewById(R.id.coinBalance);
            viewHolder.Quantity = (TextView) convertView.findViewById(R.id.coinQuantity);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        // set information

       viewHolder.Title.setText(coinItem.get(position).getTitle());

       Double mValue = coinItem.get(position).getPrice();

       if(mValue != null) {
           String fValue = "$" + Util.getFormattedNumber(mValue);
           viewHolder.Amount.setText(fValue);
       }

        Double mBalance = coinItem.get(position).getTotalValue();

        if(mBalance != null) {
            String fValue = "$" + Util.getFormattedNumber(mBalance);
            viewHolder.Total.setText(fValue);
        }


        String symbol = "(" + coinItem.get(position).getSymbol() + ")";
        viewHolder.Symbol.setText(symbol);

        String tempQ = "x: " + coinItem.get(position).getQuantity();
        viewHolder.Quantity.setText(tempQ);
        Picasso.with(mContext).load(coinItem.get(position).getImageUrl()).fit().centerCrop().into(viewHolder.Image);

        // Return the completed view to render on screen
        return convertView;
    }

    public void updateCoins(List<CoinItem> coins, Integer condition) {

        coinItem.clear();
        tempArray.clear();
        coinItem.addAll(coins);
        tempArray.addAll(coins);

        sortTheCoins(condition);


    }
    public void clearCoins() {

       if(coinItem != null) {
           coinItem.clear();
       }

        if(tempArray != null) {

            tempArray.clear();
        }


        notifyDataSetChanged();


    }

    public void filterList(String text, CoinsAdapter adapter ) {

        String charSet =  text.toLowerCase(Locale.getDefault());



        coinItem.clear();


        if(charSet.length() == 0){
            coinItem.addAll(tempArray);
        }

        else {


            int i;
            for(i = 0; i < tempArray.size(); i++) {

               Boolean inTitle = tempArray.get(i).getTitle().toLowerCase(Locale.getDefault()).contains(charSet);
               Boolean inSymbol = tempArray.get(i).getSymbol().toLowerCase(Locale.getDefault()).contains(charSet);

                if(inTitle || inSymbol ) {

                    coinItem.add(tempArray.get(i));

                }


            }



        }

        adapter.notifyDataSetChanged();



    }


    public List<CoinItem> getAdapterCoinList() {
        return this.coinItem;
    }

    public void sortTheCoins(Integer condition) {




        switch (condition) {


            // by price value
            case 0:

                Collections.sort(coinItem, new Comparator<CoinItem>(){
                    @Override
                    public int compare(CoinItem o1, CoinItem o2) {
                        return (int)((o2.getPrice() - o1.getPrice()));
                    }
                });

                Collections.sort(tempArray, new Comparator<CoinItem>(){
                    @Override
                    public int compare(CoinItem o1, CoinItem o2) {
                        return (int)((o2.getPrice() - o1.getPrice()));
                    }
                });

                Log.i("Condition ", "" + condition);



                break;

            // by quantity owned
            case 1:

                Collections.sort(coinItem, new Comparator<CoinItem>(){
                    @Override
                    public int compare(CoinItem o1, CoinItem o2) {
                        return  ((Integer.parseInt(o2.getQuantity()) - Integer.parseInt(o1.getQuantity())));
                    }
                });

                Collections.sort(tempArray, new Comparator<CoinItem>(){
                    @Override
                    public int compare(CoinItem o1, CoinItem o2) {
                        return  ((Integer.parseInt(o2.getQuantity()) - Integer.parseInt(o1.getQuantity())));
                    }
                });



                break;

            // by value onwned
            case 2:

                Collections.sort(coinItem, new Comparator<CoinItem>(){
                    @Override
                    public int compare(CoinItem o1, CoinItem o2) {
                        return (int)((o2.getTotalValue() - o1.getTotalValue()));
                    }
                });

                Collections.sort(tempArray, new Comparator<CoinItem>(){
                    @Override
                    public int compare(CoinItem o1, CoinItem o2) {
                        return (int)((o2.getTotalValue() - o1.getTotalValue()));
                    }
                });

                break;


        }


        notifyDataSetChanged();
    }

}
