package Utilities;

import com.evmcstudios.cryptotracker.MainPages.CTCoinDetails;
import com.evmcstudios.cryptotracker.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Ultranova on 2/24/2018.
 */

public class PrettyFyGraph {


    private static GridLabelRenderer grid;
    private static LineGraphSeries<DataPoint> dps;



    public static GraphView setAndGetGraph( CTCoinDetails activity, GraphView graph, DataPoint[] data, String range)  {

        Double firstDate = data[0].getX();
        Double lastDate = data[data.length-1].getX();

        SimpleDateFormat format = getDateFormatOnRange(range);

        dps = new LineGraphSeries<>(data);

        int linecolor = activity.getResources().getColor(R.color.linecolor);
        int bgcolor = activity.getResources().getColor(R.color.bgcolor);
        int gridcolor = activity.getResources().getColor(R.color.gridcolor);

        dps.setColor(linecolor);
        dps.setDrawDataPoints(true);
        dps.setDataPointsRadius(8);
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
