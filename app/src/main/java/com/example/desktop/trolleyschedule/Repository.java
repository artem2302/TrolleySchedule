package com.example.desktop.trolleyschedule;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by DESKTOP on 13.09.2017.
 */

public class Repository extends SQLiteOpenHelper
{

    static final int DB_VERSION = 3;
    static final String DB_NAME = "trolley_db.sqlite";
    private static  File DB_FILE;

    private boolean mInvalidDatabaseFile = false;
    private Context mContext;

    Repository(Context ctx)
    {
        super(ctx, DB_NAME, null, DB_VERSION);
        mContext = ctx;
        DB_FILE = ctx.getDatabasePath(DB_NAME);
        SQLiteDatabase db = null;
        try
        {
            db = getReadableDatabase();
            if (db != null)
                db.close();
            if (mInvalidDatabaseFile)
                copyDatabase();
        } catch (SQLiteException e)
        {

        } finally
        {
            if (db != null && db.isOpen())
                db.close();
        }
    }

    private void copyDatabase()
    {
        AssetManager assetManager = mContext.getResources().getAssets();
        InputStream in = null;
        OutputStream out = null;
        try
        {
            in = assetManager.open(DB_NAME);
            out = new FileOutputStream(DB_FILE);
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) != -1)
                out.write(buffer, 0, read);
        } catch (IOException e)
        {
        } finally
        {
            if (in != null)
                try
                {
                    in.close();
                } catch (IOException e)
                {
                }
        }
        if (out != null)
            try
            {
                out.close();
            } catch (IOException e)
            {
            }
        setDatabaseVersion();
        mInvalidDatabaseFile = false;
    }

    private void setDatabaseVersion()
    {
        SQLiteDatabase db = null;
        try
        {
            db = SQLiteDatabase.openDatabase(DB_FILE.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
            db.execSQL("PRAGMA user_version = "+DB_VERSION);
        } catch (SQLiteException e)
        {
        }
        finally
        {
            if (db != null && db.isOpen())
                db.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        mInvalidDatabaseFile = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        mInvalidDatabaseFile = true;
    }
}
