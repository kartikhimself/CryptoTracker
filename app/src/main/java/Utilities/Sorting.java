package Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.MainPages.CTWatchList;

/**
 * Created by Ultranova on 2/18/2018.
 */

public class Sorting {

    CTWatchList activity;
    Integer selected = 0;

    public Sorting( CTWatchList activity){

        this.activity = activity;

        setSorting();
    }


    public void setSorting() {

        final CharSequence[] items = {"Coin price", "Owned quantity", "Owned value"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Sort list by:");

        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected = which;
            }
        });


        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

             activity.setSortingCondition(selected);

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();

    }




}
