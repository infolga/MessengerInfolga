package com.infolga.messengerinfolga;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by infol on 24.03.2018.
 */

public class ServerConnect {

    public static final int SERVER_CONNECT = 1;
    public static final int CONNECTION_SUCCESSFUL = 2;
    public static final int CONNECTION_ERROR = 3;
    public static final int SEND_MESSAGE = 4;

    private static final String TAG = "ServerConnect";
    private static ServerConnect serverConnect;

    private boolean isAnswer;
    private Context cont;
    private Socket socket;
    private Handler mHandlerServerConnect;
    private Handler mHandlerActiveViwe;
    private Handler mHandlerDB;
    private String address;
    private String serverPort;
    private InputStream sin;

    private OutputStream sout;
    private Handler h;
    Runnable waitAndReedMsg = new Runnable() {
        public void run() {
            Log.e(TAG, "waitAndReedMsg");
            try {

               byte[]  b  = new byte[4];
                sin.read(b);

                int ret = 0;
                for (int i=0; i<4 ; i++) {
                    ret <<= 8;
                    Log.e(TAG, " i "+  (int)b[i]);
                    ret |= (int)b[i] & 0xFF;
                }
                byte[]  bytes  = new byte[ret];
                Log.e(TAG, "t "+sin.read(bytes));
                b= Base64.decode(bytes,Base64.DEFAULT);

                String text = new String(b, "UTF-8");
                Log.e(TAG, "text "+ret+"  " + text);



            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (!socket.isClosed()) {
                h.post(waitAndReedMsg);
            }
        }
    };
    private Thread listenerServerThread = new Thread() {
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            h = new Handler();
            Looper.loop();
        }
    };
    private Thread listenerMessegeThread = new Thread() {
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            mHandlerServerConnect = new MyHandlerServer();
            Looper.loop();
        }
    };

    private ServerConnect(Context C) {
        cont = C;
        address = cont.getString(R.string.localaddress);
        serverPort = cont.getString(R.string.serverPort);
    }

    public static ServerConnect instanse(Context C) {
        if (serverConnect != null) {
            return serverConnect;
        } else {
            if (C != null) {
                serverConnect = new ServerConnect(C);
                serverConnect.listenerMessegeThread.start();
                serverConnect.listenerServerThread.start();
                return serverConnect;
            } else {
                return null;
            }
        }
    }

    public void setmHandlerActiveViwe(Handler mHandlerActiveViwe) {
        this.mHandlerActiveViwe = mHandlerActiveViwe;
    }

    public void setmHandlerDB(Handler mHandlerDB) {
        this.mHandlerDB = mHandlerDB;
    }

    public Handler getmHandlerServerConnect() {
        return mHandlerServerConnect;
    }


    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    private boolean isConnect() {
        if (socket != null && !socket.isClosed()) {
            return true;
        } else {
            return false;
        }

    }

    private void Connect() throws IOException {

        Log.e(TAG, "CONNECT : " + address + " " + serverPort);
        socket = new Socket(address, Integer.parseInt(serverPort));
        sin = socket.getInputStream();
        sout = socket.getOutputStream();



        h.postDelayed(waitAndReedMsg, 1000);
    }

    private void Send_Message(Message msg) throws  IOException{

        if (!isConnect()) {
            Connect();
        }
        Log.e(TAG, (String) msg.obj);
        byte[] bd =((String) msg.obj).getBytes("UTF-8");
        sout.write(Base64.encode(bd,Base64.DEFAULT));


    }

    private class MyHandlerServer extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            Log.e(TAG, "#: " + msg.what);
            boolean b;
            switch (msg.what) {
                case SERVER_CONNECT:

                    if (isConnect()) {
                        b = isAnswer ? mHandlerActiveViwe.sendEmptyMessage(CONNECTION_SUCCESSFUL) : false;
                    } else {
                        try {
                            Connect();
                            b = isAnswer ? mHandlerActiveViwe.sendEmptyMessage(CONNECTION_SUCCESSFUL) : false;
                        } catch (IOException e) {
                            b = isAnswer ? mHandlerActiveViwe.sendEmptyMessage(CONNECTION_ERROR) : false;
                            e.printStackTrace();
                        }
                    }
                    break;
                case SEND_MESSAGE:
                    try {
                         Send_Message(msg);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    break;

                default:
                    break;
            }
            isAnswer = true;
        }
    }

}


