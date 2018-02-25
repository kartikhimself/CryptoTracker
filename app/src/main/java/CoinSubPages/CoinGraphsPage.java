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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Interfaces.TaskCompleted;
import Objects.CoinItem;
import Tasks.GetCoinGraphTask;
import Utilities.Util;

import static Utilities.PrettyFyGraph.setAndGetGraph;

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

    public void setGraphStyle(DataPoint[] data) {



        graph = setAndGetGraph( (CTCoinDetails) getActivity(), graph, data, range  );


    }



    @Override
    public void onTaskComplete(String result) {

        try {

        JSONObject graphObj = new JSONObject(result);

        JSONArray graphArr = graphObj.getJSONArray("Data");

        points = new DataPoint[graphArr.length()];


        int i;
        for(i = 0; i < graphArr.length(); i++) {

            String price = graphArr.getJSONObject(i).getString("close");
            String time = graphArr.getJSONObject(i).getString("time");

            Double dprice = Double.parseDouble(price);

            Long tsLong = Long.parseLong(time);
            Date labelDate = new Date(tsLong*1000);

            points[i] = new DataPoint(labelDate,dprice);

           }

            setGraphStyle(points);

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
