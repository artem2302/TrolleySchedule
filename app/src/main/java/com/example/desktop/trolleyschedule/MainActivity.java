package com.example.desktop.trolleyschedule;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener
{

    final String LOG_TAG = "myLog";
    Resources res;
    Fragment fragmentDay, fragmentNight;
    ImageButton btnLanguage;
    DisplayMetrics dm;
    Configuration conf;
    String currLoc;
    String anotherLoc;
    public DB db;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLanguage = (ImageButton) findViewById(R.id.btnLanguage);

        fragmentNight = new FragmentNight();
        fragmentDay = new FragmentDay();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab = actionBar.newTab();
        tab.setText(getString(R.string.day_routes));
        tab.setTabListener(this);
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setText(getString(R.string.night_routes));
        tab.setTabListener(this);
        actionBar.addTab(tab);

        res = getResources();
        dm = res.getDisplayMetrics();
        conf = res.getConfiguration();
        currLoc = conf.locale.getLanguage();
        btnLanguage.setImageResource((currLoc.equals("ru")) ? R.drawable.rusflag : R.drawable.uaflag);

        db = new DB(this);
        db.open();
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
    {
        if (tab.getText().equals(getString(R.string.day_routes)))
            ft.replace(R.id.llContainer, fragmentDay);
        else if (tab.getText().equals(getString(R.string.night_routes)))
            ft.replace(R.id.llContainer, fragmentNight);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft)
    {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft)
    {

    }

    public void btnLanguage_onClick(View view)
    {
        res = getResources();
        dm = res.getDisplayMetrics();
        conf = res.getConfiguration();
        currLoc = conf.locale.getLanguage();
        anotherLoc = (currLoc.equals("ru")) ? "uk" : "ru";
        conf.locale = new Locale(anotherLoc);
        res.updateConfiguration(conf, dm);
        recreate();
        btnLanguage.setImageResource((currLoc.equals("ru")) ? R.drawable.rusflag : R.drawable.uaflag);
    }
}
