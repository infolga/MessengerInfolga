package com.infolga.messengerinfolga;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by infol on 24.03.2018.
 */

public class ServerConnect {
    private static final String TAG = "ServerConnect";

    private static ServerConnect serverConnect;


    private Context cont;
    private Socket socket;
    private Handler mHandlerServerConnect;

    private String address;
    private String serverPort;
    private InputStream sin;
    private OutputStream sout;
    private Handler h;
    private int countByte = -1;
    private int offset = 0;
    private ByteBuffer byteBuffer;
    Runnable waitAndReedMsg = new Runnable() {
        public void run() {
            Log.e(TAG, "waitAndReedMsg");
            try {

                if (countByte == -1) {
                    byte[] b = new byte[4];
                    int co = sin.read(b);
                    if (co == -1) {
                        throw new IOException();
                    }
                    int ret = 0;
                    for (int i = 0; i < 4; i++) {
                        ret <<= 8;
                        ret |= (int) b[i] & 0xFF;
                    }
                    countByte = ret;
                    byteBuffer = byteBuffer.allocate(countByte);
                    offset = 0;
                } else {
                    int countav = sin.available();
                    if (countav > 0) {
                        if (countav + offset > countByte) {
                            countav = countByte - offset;
                        }
                        byte[] bytes = new byte[countav];
                        int realreed = sin.read(bytes);

                        for (int i = 0; i < countav; ++i)
                            byteBuffer.put(bytes[i]);

                        offset += countav;
                        if (offset == countByte) {
                            byte[] by = Base64.decode(byteBuffer.array(), Base64.DEFAULT);

                            String text = new String(by, "UTF-8");
                            Log.e(TAG, "text " + text);
                            Message message   =  new Message();
                            message.what= MSG.PACKAGE_ARRIVES;
                            message.obj = by;
                            DD_SQL.instanse(null).HsendMessage(message);
                            countByte = -1;
                            offset = 0;

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    h.removeCallbacks(waitAndReedMsg);
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (!socket.isClosed()) {
                h.postDelayed(waitAndReedMsg, 70);
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

    public void HsendMessage(Message msg) {
        mHandlerServerConnect.sendMessage(msg);
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

    private void SendPackege(String str) throws IOException {

        if (!isConnect()) {
            Connect();
        }
        // Log.e(TAG, (String) msg.obj);
        byte[] bd = str.getBytes("UTF-8");

        byte[] bd3 = Base64.encode(bd, Base64.DEFAULT);

        int length = bd3.length;
        Log.e(TAG, "length ms " + length);
        byte[] result = new byte[4];

        result[0] = (byte) (length >> 24);
        result[1] = (byte) (length >> 16);
        result[2] = (byte) (length >> 8);
        result[3] = (byte) (length /*>> 0*/);

        sout.write(result);
        sout.write(bd3);
        sout.flush();

    }

    private class MyHandlerServer extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            Log.e(TAG, "#: " + msg.what);
            try {
                switch (msg.what) {
                    case MSG.SEND_PACKEGE:
                        SendPackege((String) msg.obj);
                        break;


                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


