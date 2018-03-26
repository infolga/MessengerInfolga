package com.infolga.messengerinfolga;

/**
 * Created by infol on 25.03.2018.
 */

public abstract class MSG {


    //public static final int SERVER_CONNECT = 1; // подключиться к серверу и проверить соединение
    public static final int CONNECTION_SUCCESSFUL = 2;
    public static final int CONNECTION_ERROR = 3;
    public static final int SEND_PACKEGE = 4;
    public static final int USER_LOGIN = 5;

    public static final int XML_USER_LOGIN = 6;
    public static final int XML_USER_CREATE = 7;


    public static final String XML_TYPE_REQUEST = "request";
    public static final String XML_TYPE_RESPONSE = "response";
    public static final String XML_ELEMENT_PHONE = "phone";
    public static final String XML_ELEMENT_PASSWORD = "password";
    public static final String XML_ELEMENT_DRVISE_INFO = "device_info";
    public static final String XML_ELEMENT_DRVISE_TOKEN = "device_token";
}
