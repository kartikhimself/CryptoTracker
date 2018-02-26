package Utilities;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.MainPages.CTCoinDetails;
import com.evmcstudios.cryptotracker.R;
import com.github.florent37.viewtooltip.ViewTooltip;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.tooltip.Tooltip;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Ultranova on 2/24/2018.
 */

public class PrettyFyGraph {


    private static GridLabelRenderer grid;
    private static LineGraphSeries<DataPoint> dps;



    public static GraphView setAndGetGraph(final CTCoinDetails activity, final GraphView graph, DataPoint[] data, String range, final Boolean lowValue)  {

        Double firstDate = data[0].getX();
        Double lastDate = data[data.length-1].getX();




        SimpleDateFormat format = getDateFormatOnRange(range);

        dps = new LineGraphSeries<>(data);

        final int linecolor = activity.getResources().getColor(R.color.linecolor);
        final int bgcolor = activity.getResources().getColor(R.color.bgcolor);
        final int gridcolor = activity.getResources().getColor(R.color.gridcolor);


        dps.setColor(linecolor);
        dps.setDrawDataPoints(true);
        dps.setDataPointsRadius(12);
        dps.setThickness(4);
        dps.setDrawBackground(true);
        dps.setBackgroundColor(bgcolor);
        dps.setAnimated(true);

        dps.setDrawAsPath(true);

        // grid

        grid = graph.getGridLabelRenderer();

        grid.setLabelFormatter(new DateAsXAxisLabelFormatter(activity, format ));


        grid.setGridColor(gridcolor);
        grid.setVerticalLabelsColor(gridcolor);
        grid.setHorizontalLabelsColor(gridcolor);
        grid.setTextSize(23);
        grid.setNumHorizontalLabels(5);



        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(firstDate);
        graph.getViewport().setMaxX(lastDate);




        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);



        graph.addSeries(dps);


        dps.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {

                Double pricePointY = dataPoint.getY();

                if(lowValue){

                    pricePointY = pricePointY/Util.lowNumberMultiplier;

                }




                ViewTooltip
                        .on(graph)
                        .position(ViewTooltip.Position.TOP)
                        .text("$" + Util.getNumberForGraph(pricePointY))
                        .clickToHide(true)
                        .autoHide(true, 2000)
                        .animation(new ViewTooltip.FadeTooltipAnimation(500))
                        .color(linecolor)
                        .textColor(gridcolor)
                        .textSize(1, 20)
                        .align(ViewTooltip.ALIGN.CENTER)
                        .show();


                /* Tooltip tooltip = new Tooltip.Builder(graph)
                        .setText("$" + Util.getNumberForGraph(pricePointY))
                        .setCancelable(true)
                        .setGravity(Gravity.TOP)
                        .setBackgroundColor(bgcolor)
                        .setTextColor(gridcolor)
                        .setCornerRadius(R.dimen.tipradius)
                        .setTextSize(R.dimen.tipsize)
                        .show();
                        */




            }
        });



        return graph;
    }


    public static SimpleDateFormat getDateFormatOnRange(String range) {

        SimpleDateFormat format;

        switch (range) {

            case "1H":
                format =  new SimpleDateFormat("h:mm:ss", Locale.US);
                break;

            case "24H":
                format =  new SimpleDateFormat("hh:mm", Locale.US);
                break;

            case "30D":
                format =  new SimpleDateFormat("MM/dd", Locale.US);
                break;

            default:
                format =  new SimpleDateFormat("h:mm:ss", Locale.US);
                break;

        }

        return format;
    }

}
