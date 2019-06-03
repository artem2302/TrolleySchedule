package com.example.desktop.trolleyschedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.sql.Time;
import java.util.List;

/**
 * Created by DESKTOP on 19.09.2017.
 */

public class ScheduleActivity extends AppCompatActivity
{
    private int interval, time_to_go;
    private String startTimeWDS, stopTimeWDS, startTimeWES, stopTimeWES;
    private Time startTimeWD, stopTimeWD, startTimeWE, stopTimeWE;
    static DateFormat formatter = new SimpleDateFormat("HH:mm");

    static Time addMinutes(Time time, int minutes)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.MINUTE, minutes);
        try
        {
            return new Time(formatter.parse(formatter.format(cal.getTime())).getTime());
        } catch (ParseException e)
        {
            return null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        startTimeWDS = getIntent().getStringExtra("startTimeWD");
        stopTimeWDS = getIntent().getStringExtra("stopTimeWD");
        interval = getIntent().getIntExtra("interval", -1);
        time_to_go = getIntent().getIntExtra("time_to_go", -1);

        try
        {
            startTimeWD = new Time(formatter.parse(startTimeWDS).getTime());
            stopTimeWD = new Time(formatter.parse(stopTimeWDS).getTime());
            /*    Toast.makeText(this,formatter.format(startTimeWD),Toast.LENGTH_SHORT).show();
                Toast.makeText(this,formatter.format(addMinutes(startTimeWD,10)),Toast.LENGTH_SHORT).show();
                Toast.makeText(this,formatter.format(addMinutes(startTimeWD,60)),Toast.LENGTH_SHORT).show();*/
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        // showAllTime();
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                showAllTime();
            }
        });
        t.run();
        // showAllTime();
    }

    private void showAllTime()
    {
        if (interval == -1) return;
        Time t = startTimeWD;
        int hours = startTimeWD.getHours();
        ArrayList<String> times = new ArrayList<>();

        times.add(formatter.format(addMinutes(startTimeWD, time_to_go)));
        while (t.getTime() < stopTimeWD.getTime())
        {
            t = addMinutes(addMinutes(t, interval), time_to_go);
            if (t.getHours() > hours)
            {
                times.add("NEW HOUR");
                ++hours;
            }
            times.add(formatter.format(t));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, times);
        ((ListView) findViewById(R.id.lvStopSchedule)).setAdapter(adapter);
    }
}
