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


    public static final int XML_USER_LOGIN = 6;//id actions
    public static final int XML_USER_REGISTRATION = 7;//id actions

    public static final int PACKAGE_ARRIVES = 8;


    public static final int USER_LOGIN_SUCCESSFUL = 9;
    public static final int USER_LOGIN_FAIL_PASSWORD = 10;
    public static final int USER_LOGIN_FAIL_PHONE = 11;

    public static final int USER_REGISTRATION = 12;
    public static final int USER_REGISTRATION_FAIL_PHONE = 13;
    public static final int USER_REGISTRATION_FAIL_USER_NAME = 14;
    public static final int USER_REGISTRATION_SUCCESSFUL = 15;


    public static final int READ_ALL_CONTACT = 16;

    public static final int CONTACT_ADD = 17;
    public static final int CONTACT_ADD_SUCCESSFUL=18;
    public static final int CONTACT_ADD_FAIL=19;

    public static final int XML_CONTACT_ADD= 20;//id actions
    public static final int XML_GET_USERS_FROM_LIKE= 21;//id actions

    public static final int GET_USERS_FROM_LIKE= 22;

    public static final int UPDATE_RECYCLER_VIEV= 23;



    public static final int XML_RESULT_VALUES_OK = 0;
    public static final int XML_RESULT_VALUES_INCORRECT_PASSWORD = 100;
    public static final int XML_RESULT_VALUES_PHONE_NOT_FOUND = 101;
    public static final int XML_RESULT_VALUES_PHONE_UNAVAILABLE = 102;
    public static final int XML_RESULT_VALUES_USER_NAME_UNAVAILABLE = 103;
    public static final int XML_RESULT_VALUES_TOKEN_UNAVAILABLE = 104;

    public static final String XML_TYPE_REQUEST = "request";
    public static final String XML_TYPE_RESPONSE = "response";
    public static final String XML_ELEMENT_PHONE = "phone";
    public static final String XML_ELEMENT_PASSWORD = "password";
    public static final String XML_ELEMENT_EMAIL = "email";
    public static final String XML_ELEMENT_FIRST_NAME = "first_name";
    public static final String XML_ELEMENT_LAST_NAME = "last_name";
    public static final String XML_ELEMENT_USER_NAME = "user_name";
    public static final String XML_ELEMENT_USERS_ID = "users_id";
    public static final String XML_ELEMENT_TOKEN = "token";
    public static final String XML_ELEMENT_IS_ACTIVE= "is_active";
    public static final String XML_ELEMENT_LAST_ONLINE ="last_online_at";
    public static final String XML_ELEMENT_CONTACT_ID ="contact_id";

    public static final String XML_ELEMENT_USER_NAME_LIKE ="user_name_like";
    public static final String XML_ELEMENT_AFTER ="after_numb";
    public static final String XML_ELEMENT_BEFORE ="before_numb";
    public static final String XML_ELEMENT_USER ="user";


    public static final String XML_ELEMENT_ACTIONS = "actions";
    public static final String XML_ELEMENT_DRVISE_INFO = "device_info";
    public static final String XML_ELEMENT_DRVISE_TOKEN = "device_token";
    public static final String XML_ATRIBUT_RESULT = "result";

}