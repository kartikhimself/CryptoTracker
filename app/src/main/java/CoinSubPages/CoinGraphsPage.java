package CoinSubPages;




import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.evmcstudios.cryptotracker.MainPages.CTCoinDetails;
import com.evmcstudios.cryptotracker.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Interfaces.TaskCompleted;
import Objects.CoinItem;
import Tasks.GetCoinGraphTask;

import Utilities.PrettyFyGraph;
import Utilities.Util;



/**
 * Created by Ultranova on 2/24/2018.
 */

public class CoinGraphsPage extends Fragment implements TaskCompleted, View.OnClickListener {


    private GraphView graph;
    public GetCoinGraphTask graphTask = null;
    private String coinSymbol;
    private DataPoint[] points;
    private String range = Util.defaultRange;

    public CoinGraphsPage() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        coinSymbol = ((CTCoinDetails) getActivity()).selectedCoin.getSymbol();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.subpage_content_graphs, container, false);

        // button listeners

        Button oneHour = (Button) view.findViewById(R.id.btn_1h);
        oneHour.setOnClickListener(this);
        Button hour24 = (Button) view.findViewById(R.id.btn_24h);
        hour24.setOnClickListener(this);
        Button day30 = (Button) view.findViewById(R.id.btn_30D);
        day30.setOnClickListener(this);

        graph = (GraphView) view.findViewById(R.id.graph);
        getDataPoints(range);

        return view;

    }


    public void getDataPoints(String Range) {


        if(graphTask == null) {
            graphTask = new GetCoinGraphTask(CoinGraphsPage.this, coinSymbol, Range );
            graphTask.execute((Void) null);
        }
        else {

            graphTask.cancel(true);
            graphTask = new GetCoinGraphTask(CoinGraphsPage.this, coinSymbol, Range);
            graphTask.execute((Void) null);

        }


    }

    public void setGraphStyle(DataPoint[] data, Boolean lowValue) {



        graph = PrettyFyGraph.setAndGetGraph( (CTCoinDetails) getActivity(), graph, data, range, lowValue  );


    }



    @Override
    public void onTaskComplete(String result) {

        Boolean lowValue = false;

        try {

        JSONObject graphObj = new JSONObject(result);

        JSONArray graphArr = graphObj.getJSONArray("Data");

        points = new DataPoint[graphArr.length()];


        int i;
        for(i = 0; i < graphArr.length(); i++) {

            Double price = graphArr.getJSONObject(i).getDouble("close");
            String time = graphArr.getJSONObject(i).getString("time");

            BigDecimal currentNumber = new BigDecimal(price);
            BigDecimal decimal2 = currentNumber.setScale(9, RoundingMode.HALF_UP);

            Double toBeTruncated = Double.parseDouble(decimal2.toPlainString());

            Long tsLong = Long.parseLong(time);
            Date labelDate = new Date(tsLong*Util.timeMultiplier);

            if(toBeTruncated <= Util.defaultLowNumber){
                lowValue = true;
                toBeTruncated = toBeTruncated * Util.lowNumberMultiplier;
            }

            points[i] = new DataPoint(labelDate, toBeTruncated );

           }

            setGraphStyle(points, lowValue);

        } catch (JSONException e) {

    e.printStackTrace();

        }

    }



    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btn_1h:
                range = "1H";
                break;

            case R.id.btn_24h:
                range = "24H";
                break;

            case R.id.btn_30D:
                range = "30D";
                break;

            default:
                break;
        }


        graph.removeAllSeries();
        getDataPoints(range);

    }
}
