package com.example.desktop.trolleyschedule;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DESKTOP on 06.09.2017.
 */

public class FragmentDay extends Fragment
{
    private boolean rus;
    private ListView lvTrolleys;
    ArrayList<Map<String, Object>> trolleys;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.fragment_day, null);

        lvTrolleys = ((ListView) (v.findViewById(R.id.lvTrolleys)));

        rus = ((((MainActivity) getActivity()).currLoc.equals("ru")));

        String[] from = {"name"};
        int[] to = {android.R.id.text1};

        trolleys = DB.getRoutes(rus, true);

        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), trolleys, android.R.layout.simple_list_item_1, from, to);
        lvTrolleys.setAdapter(simpleAdapter);
        lvTrolleys.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getContext(), TrolleyInfoActivity.class);
                String exId = (String) trolleys.get(position).get(DB.ROUTES_COLUMN_ID);
                String exName = (String) trolleys.get(position).get("name");
                intent.putExtra("route_id", exId);
                intent.putExtra("name", exName);
                intent.putExtra("rus", rus);
                startActivity(intent);
            }
        });

        return v;
    }
}
