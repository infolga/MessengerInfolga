package com.infolga.messengerinfolga;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.jdom2.JDOMException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by infol on 21.03.2018.
 */

public class DD_SQL {

    private static final String TAG = "DD_SQL";
    private static final String DATABASE_NAME = "MyDB.db";
    private static final int DATABASE_VERSION = 1;

    private static DD_SQL dd_sql;
    private Context cont;
    private MyDB DB;

    private Handler mHandlerActiveViwe;
    private Handler mHandlerDB;
    private Thread listenerMessegeThread = new Thread() {
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            mHandlerDB = new MyHandlerDB();
            Looper.loop();
        }
    };

    private DD_SQL(Context context) {
        DB = new MyDB(context);
        listenerMessegeThread.start();

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

    public void setmHandlerActiveViwe(Handler mHandlerActiveViwe) {
        this.mHandlerActiveViwe = mHandlerActiveViwe;
    }

    public void HsendMessage(Message msg) {
        mHandlerDB.sendMessage(msg);
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
            s = null;
        } else {

            Date currentTime = Calendar.getInstance().getTime();
            Log.v(currentTime.toString(), this.toString());

            long aLong = cursor.getLong(1);
            Log.v("" + currentTime.getTime(), this.toString());
            if (aLong < currentTime.getTime()) {
                s = null;
            } else {
                s = cursor.getString(0);
            }
        }
        cursor.close();
        return s;
    }

    private void clearUs() {
        DB.getReadableDatabase().execSQL(cont.getString(R.string.SQLgetDeleteUS));
    }

    private void clearAccess() {
        DB.getReadableDatabase().execSQL(cont.getString(R.string.SQLgetDeleteAccess));
    }

    private class MyHandlerDB extends Handler {
        @SuppressLint("HardwareIds")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyXML X;
            Bundle bundle;
            Message message;
            Log.e(TAG, "#: " + msg.what);
            boolean b;
            switch (msg.what) {
                case MSG.USER_LOGIN:
                    bundle = (Bundle) msg.obj;
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_USER_LOGIN);


                    X.addChild(MSG.XML_ELEMENT_PHONE, bundle.getString(MSG.XML_ELEMENT_PHONE));
                    Log.e(TAG, X.toString());
                    X.addChild(MSG.XML_ELEMENT_PASSWORD, bundle.getString(MSG.XML_ELEMENT_PASSWORD));


                    X.addChild(MSG.XML_ELEMENT_DRVISE_INFO, Build.MANUFACTURER + " " + Build.MODEL);
                    X.addChild(MSG.XML_ELEMENT_DRVISE_TOKEN, FirebaseInstanceId.getInstance().getToken());

                    Log.e(TAG, X.toString());

                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toString();
                    clearUs();
                    clearAccess();
                    ServerConnect.instanse(null).HsendMessage(message);
                    Log.e(TAG, X.toString());
                    break;

                case MSG.PACKAGE_ARRIVES:
                    try {
                        X = new MyXML((String) msg.obj);
                        analisXML(X);

                    } catch (JDOMException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG.USER_REGISTRATION:
                    bundle = (Bundle) msg.obj;
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_USER_REGISTRATION);

                    X.addChild(MSG.XML_ELEMENT_PHONE, bundle.getString(MSG.XML_ELEMENT_PHONE));
                    X.addChild(MSG.XML_ELEMENT_PASSWORD, bundle.getString(MSG.XML_ELEMENT_PASSWORD));

                    X.addChild(MSG.XML_ELEMENT_USER_NAME, bundle.getString(MSG.XML_ELEMENT_USER_NAME));
                    X.addChild(MSG.XML_ELEMENT_FIRST_NAME, bundle.getString(MSG.XML_ELEMENT_FIRST_NAME));
                    X.addChild(MSG.XML_ELEMENT_LAST_NAME, bundle.getString(MSG.XML_ELEMENT_LAST_NAME));
                    X.addChild(MSG.XML_ELEMENT_EMAIL, bundle.getString(MSG.XML_ELEMENT_EMAIL));


                    X.addChild(MSG.XML_ELEMENT_DRVISE_INFO, Build.MANUFACTURER + " " + Build.MODEL);
                    X.addChild(MSG.XML_ELEMENT_DRVISE_TOKEN, FirebaseInstanceId.getInstance().getToken());
                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toString();
                    clearUs();
                    clearAccess();
                    ServerConnect.instanse(null).HsendMessage(message);
                    Log.e(TAG, X.toString());

                    break;


                default:
                    break;
            }

        }

        private void analisXML(MyXML XML) {

            if (MSG.XML_TYPE_REQUEST.equals(XML.getTypeXML())) {

            } else {

                switch (XML.getIdActionsXML()) {
                    case MSG.XML_USER_LOGIN:
                        userLoginResponse(XML);
                        break;
                    case MSG.XML_USER_REGISTRATION:
                        userRegistrationResponse(XML);

                        break;
                }


            }


        }

        private void userRegistrationResponse(MyXML XML) {
            int result = XML.getAttributeResult();


            Message message = new Message();
            message.what = XML.getAttributeResult();
            mHandlerActiveViwe.sendMessage(message);
            switch (result) {
                case MSG.XML_RESULT_VALUES_OK:
                    message = new Message();
                    message.what = MSG.USER_REGISTRATION_SUCCESSFUL;

                    mHandlerActiveViwe.sendMessage(message);
                    break;
                case MSG.XML_RESULT_VALUES_PHONE_UNAVAILABLE:
                    message = new Message();
                    message.what = MSG.USER_REGISTRATION_FAIL_PHONE;

                    mHandlerActiveViwe.sendMessage(message);
                    break;
                case MSG.XML_RESULT_VALUES_USER_NAME_UNAVAILABLE:
                    message = new Message();
                    message.what = MSG.USER_REGISTRATION_FAIL_USER_NAME;

                    mHandlerActiveViwe.sendMessage(message);
                    break;


            }
        }

        private void userLoginResponse(MyXML XML) {
            int result = XML.getAttributeResult();
            Message message;
            switch (result) {
                case MSG.XML_RESULT_VALUES_OK:
                    message = new Message();
                    message.what = MSG.USER_LOGIN_SUCCESSFUL;

                    mHandlerActiveViwe.sendMessage(message);
                    break;
                case MSG.XML_RESULT_VALUES_INCORRECT_PASSWORD:
                    message = new Message();
                    message.what = MSG.USER_LOGIN_FAIL_PASSWORD;
                    mHandlerActiveViwe.sendMessage(message);
                    break;
                case MSG.XML_RESULT_VALUES_PHONE_NOT_FOUND:
                    message = new Message();
                    message.what = MSG.USER_LOGIN_FAIL_PHONE;
                    mHandlerActiveViwe.sendMessage(message);
                    break;
                default:
                    break;
            }
        }


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
