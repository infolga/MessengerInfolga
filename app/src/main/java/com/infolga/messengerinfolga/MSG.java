package com.infolga.messengerinfolga;


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

    public static final int XML_CONVERSATION_ADD = 24;//id actions
    public static final int XML_CONVERSATION_SINGLE_CREATE= 25;
    public static final int GET_ALL_CONVERSATION = 26;
    public static final int XML_GET_ALL_CONVERSATION = 27;//id actions

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
    public static final String XML_ELEMENT_TITLE ="title";
    public static final String XML_ELEMENT_NAME_CONVERSATION ="name_conversation";
    public static final String XML_ELEMENT_PHOTO_ID ="photo_id";
    public static final String XML_ELEMENT_TYPE ="type";
    public static final String XML_ELEMENT_TYPE_SINGLE ="single";
    public static final String XML_ELEMENT_TYPE_GROUP ="group";
    public static final String XML_ELEMENT_CONVERSATION_ID = "conversation_id";
    public static final String XML_ELEMENT_CONVERSATION_CREATOR_ID = "creator_id";
    public static final String XML_ELEMENT_CREATED_AT = "created_at";
    public static final String XML_ELEMENT_CONVERSATION_TIME_LAST_VIEV = "time_last_viev";
    public static final String XML_ELEMENT_CONVERSATION  = "conversation";

    public static final String XML_ELEMENT_MESSAGES  = "messages";
    public static final String XML_ELEMENT_MESSAGE_ID  = "message_id";
    public static final String XML_ELEMENT_SENDER_ID  = "sender_id";
    public static final String XML_ELEMENT_MESSAGE_TYPE  = "message_type";
    public static final String XML_ELEMENT_MESSAGE  = "message";
    public static final String XML_ELEMENT_ATTACHMENT_THUMB_URL  = "attachment_thumb_url";
    public static final String XML_ELEMENT_ATTACHMENT_URL  = "attachment_url";

    public static final String XML_ELEMENT_PARTICIPANTS  = "participants";
    public static final String XML_ELEMENT_PARTICIPANTS_ID  = "participants_id";

    public static final String XML_ELEMENT_ACTIONS = "actions";
    public static final String XML_ELEMENT_DRVISE_INFO = "device_info";
    public static final String XML_ELEMENT_DRVISE_TOKEN = "device_token";
    public static final String XML_ATRIBUT_RESULT = "result";

}