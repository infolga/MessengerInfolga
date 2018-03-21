package com.infolga.messengerinfolga;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by infol on 21.03.2018.
 */

public class DD_SQL extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDB.db";
    private static final int DATABASE_VERSION = 1;
    private  Context cont;

    public DD_SQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        cont=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        AssetManager assets = cont.getAssets();
        assets.
        db.execSQL(R.string.TABLE_contacts);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
