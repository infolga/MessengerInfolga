package com.infolga.messengerinfolga;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by infol on 21.03.2018.
 */

public class DD_SQL {

    private static final String DATABASE_NAME = "MyDB.db";
    private static final int DATABASE_VERSION = 1;
    private static Context cont;
    private static DD_SQL dd_sql;
    private MyDB DB;

    private DD_SQL(Context context) {
        DB = new MyDB(context);

    }

    public static DD_SQL instanse(Context C) {

        if (dd_sql != null) {
            return dd_sql;
        } else {
            if (C != null) {
                dd_sql = new DD_SQL(C);
                return dd_sql;

            } else {
                return null;
            }
        }
    }

//    public String getMyUser(){
//        Cursor cursor = DB.getReadableDatabase().rawQuery(cont.getString(R.string. ), null);
//        cursor.moveToFirst();
//        String s;
//
//
//
//    }


    public String getAccessToken() {

        Cursor cursor = DB.getReadableDatabase().rawQuery(cont.getString(R.string.SQLgetAccessToken), null);
        cursor.moveToFirst();
        String s;

        if (cursor.getCount() == 0) {
            s=null;
        } else {

            Date currentTime = Calendar.getInstance().getTime();
            Log.v(currentTime.toString(), this.toString());

            long aLong = cursor.getLong(1);
            Log.v(""+currentTime.getTime(), this.toString());
            if (aLong<currentTime.getTime()){
                s= null;
            }else
            {
                s= cursor.getString(0);
            }
        }
        cursor.close();
        return s;
    }


    private class MyDB extends SQLiteOpenHelper {

        public MyDB(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            context.deleteDatabase(DATABASE_NAME);

            Log.v("DATABASE_constructor", this.toString());
            cont = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            AssetManager assets = cont.getAssets();
            String line = " ";
            BufferedReader bufferedReader;
            String buf;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(assets.open("DB_create")));
                while ((buf = bufferedReader.readLine()) != null) {
                    line = line + buf;
                }

            } catch (IOException e) {
                Log.v("file is not found", this.toString());
                e.printStackTrace();
            }
            for (String retval : line.split(";")) {
                Log.v(retval, this.toString());
                db.execSQL(retval);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

    }


}
