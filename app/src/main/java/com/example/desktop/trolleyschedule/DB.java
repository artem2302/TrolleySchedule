package com.example.desktop.trolleyschedule;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DESKTOP on 13.09.2017.
 */

public class DB
{
    private final Context mCtx;
    public static Repository repository;
    private SQLiteDatabase mDB;

    private static final String DB_NAME = Repository.DB_NAME;
    private static final int DB_VERSION = Repository.DB_VERSION;
    //TABLES/COLUMNS:
    public static final String TABLE_ROUTE_NAMES_RU = "route_names_ru";
    public static final String ROUTE_NAMES_RU_COLUMN_ID = "_id";
    public static final String ROUTE_NAMES_RU_COLUMN_FROM = "`from`";
    public static final String ROUTE_NAMES_RU_COLUMN_TO = "`to`";
    public static final String ROUTE_NAMES_RU_COLUMN_ID_ROUTES = "id_routes";

    public static final String TABLE_ROUTE_NAMES_UA = "route_names_ua";
    public static final String ROUTE_NAMES_UA_COLUMN_ID = "_id";
    public static final String ROUTE_NAMES_UA_COLUMN_FROM = "`from`";
    public static final String ROUTE_NAMES_UA_COLUMN_TO = "`to`";
    public static final String ROUTE_NAMES_UA_COLUMN_ID_ROUTES = "id_routes";

    public static final String TABLE_ROUTES = "routes";
    public static final String ROUTES_COLUMN_ID = "_id";
    public static final String ROUTES_COLUMN_NUMBER = "number";
    public static final String ROUTES_COLUMN_ISNIGHT = "is_night";
    public static final String ROUTES_COLUMN_INTERVAL_1 = "interval6_10_16_20";
    public static final String ROUTES_COLUMN_INTERVAL_2 = "interval_10_16_20_2230";

    public static final String TABLE_ROUTES_STOPS = "routes_stops";
    public static final String ROUTES_STOPS_COLUMN_ID = "_id";
    public static final String ROUTES_STOPS_COLUMN_ID_ROUTES = "id_routes";
    public static final String ROUTES_STOPS_COLUMN_ID_STOPS = "id_stops";
    public static final String ROUTES_STOPS_COLUMN_MINUTES_TO_GO = "minutes_to_go";

   /* public static final String TABLE_ROUTES_STOPS_WEEKDAYS = "routes_stops_weekdays";
    public static final String ROUTES_STOPS_WEEKDAYS_COLUMN_ID = "_id";
    public static final String ROUTES_STOPS_WEEKDAYS_COLUMN_ID_ROUTES = "id_routes_stops";
    public static final String ROUTES_STOPS_WEEKDAYS_COLUMN_ID_STOPS = "time_forward_stop";
    public static final String ROUTES_STOPS_WEEKDAYS_COLUMN_MINUTES_TO_GO = "time_back_stop";*/

    public static final String TABLE_SCHEDULE_WEEKDAYS = "schedule_weekdays";
    public static final String SCHEDULE_WEEKDAYS_COLUMN_ID = "_id";
    public static final String SCHEDULE_WEEKDAYS_COLUMN_ID_ROUTES = "id_routes";
    public static final String SCHEDULE_WEEKDAYS_COLUMN_START_FORWARD = "start_forward";
    public static final String SCHEDULE_WEEKDAYS_COLUMN_STOP_FORWARD = "stop_forward";

    public static final String TABLE_SCHEDULE_WEEKEND = "schedule_weekdays";
    public static final String SCHEDULE_WEEKEND_COLUMN_ID = "_id";
    public static final String SCHEDULE_WEEKEND_COLUMN_ID_ROUTES = "id_routes";
    public static final String SCHEDULE_WEEKEND_COLUMN_START_FORWARD = "start_forward";
    public static final String SCHEDULE_WEEKEND_COLUMN_STOP_FORWARD = "stop_forward";

    public static final String TABLE_STOPS = "stops";
    public static final String STOPS_COLUMN_ID = "_id";

    public static final String TABLE_STOPS_NAMES_RU = "stops_names_ru";
    public static final String STOPS_NAMES_RU_COLUMN_ID = "_id";
    public static final String STOPS_NAMES_RU_COLUMN_ID_STOPS = "id_stops";
    public static final String STOPS_NAMES_RU_COLUMN_NAME = "name";

    public static final String TABLE_STOPS_NAMES_UA = "stops_names_ua";
    public static final String STOPS_NAMES_UA_COLUMN_ID = "_id";
    public static final String STOPS_NAMES_UA_COLUMN_ID_STOPS = "id_stops";
    public static final String STOPS_NAMES_UA_COLUMN_NAME = "name";


    public DB(Context ctx)
    {
        mCtx = ctx;
    }

    public void open()
    {
        repository = new Repository(mCtx);
        mDB = repository.getReadableDatabase();
    }

    public void close()
    {
        if (repository != null)
            repository.close();
    }

    public static ArrayList<Map<String, Object>> getRoutes(boolean rus, boolean day)
    {
        String table = rus ? TABLE_ROUTE_NAMES_RU : TABLE_ROUTE_NAMES_UA;
        String sql = "SELECT " + "rts." + ROUTES_COLUMN_ID + ", rts." + ROUTES_COLUMN_NUMBER + ", " + ROUTE_NAMES_RU_COLUMN_FROM + ", " + ROUTE_NAMES_RU_COLUMN_TO + " FROM " + table +
                " nms INNER JOIN " + TABLE_ROUTES + " rts ON rts." + ROUTES_COLUMN_ID + " = nms." + ROUTE_NAMES_RU_COLUMN_ID_ROUTES + " WHERE rts." + ROUTES_COLUMN_ISNIGHT + "= " + (!day ? "'true'" : "'false'") + "ORDER BY rts." + ROUTES_COLUMN_NUMBER;
        Cursor cursor = repository.getReadableDatabase().rawQuery(sql, null);

        ArrayList<Map<String, Object>> trolleys = new ArrayList<>();
        cursor.moveToFirst();
        HashMap<String, Object> m;
        while (!cursor.isAfterLast())
        {
            m = new HashMap<>();
            m.put(ROUTES_COLUMN_ID, cursor.getString(0));
            m.put("name", cursor.getString(1) + ": " + cursor.getString(2) + " - " + cursor.getString(3));
            trolleys.add(m);
            cursor.moveToNext();
        }
        cursor.close();
        return trolleys;
    }

    public static ArrayList<Map<String, Object>> getStops(boolean rus, int routeId)
    {
        String table = rus ? TABLE_STOPS_NAMES_RU : TABLE_STOPS_NAMES_UA;


        String sql = "SELECT DISTINCT stops." + STOPS_NAMES_RU_COLUMN_ID_STOPS + ", " + STOPS_NAMES_RU_COLUMN_NAME + ", rstops."+ ROUTES_STOPS_COLUMN_MINUTES_TO_GO
                + " FROM " + table + " stops"
                + " INNER JOIN " + TABLE_ROUTES_STOPS
                + " rstops ON rstops." + ROUTES_STOPS_COLUMN_ID_ROUTES
                + " = " + String.valueOf(routeId)
                + " AND rstops." + ROUTES_STOPS_COLUMN_ID_STOPS + " = stops." + STOPS_NAMES_RU_COLUMN_ID_STOPS
                + " ORDER BY " + ROUTES_STOPS_COLUMN_MINUTES_TO_GO + " ASC";
        Cursor cursor = repository.getReadableDatabase().rawQuery(sql, null);

        ArrayList<Map<String, Object>> stops = new ArrayList<>();
        HashMap<String, Object> m;

        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            m = new HashMap<>();
            m.put(STOPS_NAMES_RU_COLUMN_ID_STOPS, cursor.getString(0));
            m.put("name", cursor.getString(1));
            m.put("time_to_go",cursor.getString(2));
            stops.add(m);
            cursor.moveToNext();
        }
        return stops;
    }
    public static String getStartTimeWeekdays(int routeId)
    {
        //TABLE_SCHEDULE_WEEKDAYS,SCHEDULE_WEEKDAYS_COLUMN_START_FORWARD,SCHEDULE_WEEKDAYS_COLUMN_ID_ROUTES+" = "+routeId,null,null,null
        Cursor
            curs = repository.getReadableDatabase().
                query(true,TABLE_SCHEDULE_WEEKDAYS,new String[]{SCHEDULE_WEEKDAYS_COLUMN_START_FORWARD},SCHEDULE_WEEKDAYS_COLUMN_ID_ROUTES+" = "+String.valueOf(routeId),null,null,null,null,null);
        curs.moveToFirst();
        try
        {
            return curs.getString(0);
        }
        catch (Exception e)
        {
            return "undefined";
        }
    }
    public static String getStopTimeWeekdays(int routeId)
    {
        //TABLE_SCHEDULE_WEEKDAYS,SCHEDULE_WEEKDAYS_COLUMN_START_FORWARD,SCHEDULE_WEEKDAYS_COLUMN_ID_ROUTES+" = "+routeId,null,null,null
        Cursor
                curs = repository.getReadableDatabase().
                query(true,TABLE_SCHEDULE_WEEKDAYS,new String[]{SCHEDULE_WEEKDAYS_COLUMN_STOP_FORWARD},SCHEDULE_WEEKDAYS_COLUMN_ID_ROUTES+" = "+String.valueOf(routeId),null,null,null,null,null);
        curs.moveToFirst();
        try
        {
            return curs.getString(0);
        }
        catch (Exception e)
        {
            return "undefined";
        }
    }
    public static String getStartTimeWeekend(int routeId)
    {
        //TABLE_SCHEDULE_WEEKDAYS,SCHEDULE_WEEKDAYS_COLUMN_START_FORWARD,SCHEDULE_WEEKDAYS_COLUMN_ID_ROUTES+" = "+routeId,null,null,null
        Cursor
                curs = repository.getReadableDatabase().
                query(true,TABLE_SCHEDULE_WEEKEND,new String[]{SCHEDULE_WEEKEND_COLUMN_START_FORWARD},SCHEDULE_WEEKEND_COLUMN_ID_ROUTES+" = "+String.valueOf(routeId),null,null,null,null,null);
        curs.moveToFirst();
        try
        {
            return curs.getString(0);
        }
        catch (Exception e)
        {
            return "undefined";
        }
    }
    public static String getInterval1(int routeId)
    {
        Cursor
                curs = repository.getReadableDatabase().
                query(true,TABLE_ROUTES,new String[]{ROUTES_COLUMN_INTERVAL_1},ROUTES_COLUMN_ID+" = "+String.valueOf(routeId),null,null,null,null,null);
        curs.moveToFirst();
        try
        {
            return curs.getString(0);
        }
        catch (Exception e)
        {
            return "undefined";
        }
    }
    public static String getInterval2(int routeId)
    {
        Cursor
                curs = repository.getReadableDatabase().
                query(true,TABLE_ROUTES,new String[]{ROUTES_COLUMN_INTERVAL_2},ROUTES_COLUMN_ID+" = "+String.valueOf(routeId),null,null,null,null,null);
        curs.moveToFirst();
        try
        {
            return curs.getString(0);
        }
        catch (Exception e)
        {
            return "undefined";
        }
    }
}
