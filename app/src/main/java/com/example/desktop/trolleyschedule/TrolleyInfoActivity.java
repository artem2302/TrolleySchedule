package com.example.desktop.trolleyschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by DESKTOP on 06.09.2017.
 */

public class TrolleyInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    private ListView lvStops;
    private int trolleyId;
    private boolean rus;
    private String routeName, routeNo, timeStart, timeStop, interval;
    private  ArrayList<Map<String, Object>> stops;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trolleyinfo);

        trolleyId = Integer.valueOf(getIntent().getStringExtra("route_id"));
        rus = getIntent().getBooleanExtra("rus", true);
        routeName = getIntent().getStringExtra("name");

        lvStops = (ListView) findViewById(R.id.lvStops);

        String[] from = {"name", "time_to_go"};
        int[] to = {R.id.tvStopName, R.id.tvTimeToGo};

        stops = DB.getStops(rus, trolleyId);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, stops, R.layout.infoactivity_item, from, to);
        lvStops.setAdapter(simpleAdapter);
        lvStops.setOnItemClickListener(this);

        routeNo = routeName.substring(0, routeName.indexOf(":"));
        routeName = routeName.substring(routeName.indexOf(":") + 1, routeName.length());

        //TODO: differ WE and WD times
        timeStart = DB.getStartTimeWeekdays(trolleyId);
        timeStop = DB.getStopTimeWeekdays(trolleyId);
        interval = DB.getInterval1(trolleyId) + "/" + DB.getInterval2(trolleyId);


        ((TextView) findViewById(R.id.tvRoute)).append(routeName);
        ((TextView) findViewById(R.id.tvTrolleyno)).append(routeNo);
        ((TextView) findViewById(R.id.tvTimeStart)).append(" " + timeStart);
        ((TextView) findViewById(R.id.tvTimeStop)).append(" " + timeStop);
        ((TextView) findViewById(R.id.tvInterval)).append(" " + interval);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(this, ScheduleActivity.class);
        //int stopId = Integer.valueOf((String)stops.get(position).get(DB.STOPS_NAMES_RU_COLUMN_ID_STOPS));
        intent.putExtra("startTimeWD",timeStart);
        intent.putExtra("stopTimeWD",timeStop);
        intent.putExtra("interval",Integer.valueOf(DB.getInterval1(trolleyId)));
        intent.putExtra("time_to_go",Integer.valueOf((String)stops.get(position).get("time_to_go")));
        startActivity(intent);
    }
}
