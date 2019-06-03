package com.example.desktop.trolleyschedule;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * Created by DESKTOP on 06.09.2017.
 */

public class FragmentNight extends Fragment
{
    private ListView lvTrolleys;
    private boolean rus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_night, null);
        lvTrolleys = ((ListView) (v.findViewById(R.id.lvTrolleys)));

        rus = ((((MainActivity) getActivity()).currLoc.equals("ru")));

        String[] from = {"name"};
        int[] to = {android.R.id.text1};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), (DB.getRoutes(rus, false)), android.R.layout.simple_list_item_1, from, to);
        lvTrolleys.setAdapter(simpleAdapter);
        lvTrolleys.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                startActivity(new Intent(getContext(), TrolleyInfoActivity.class));
            }
        });

        return v;
    }
}
