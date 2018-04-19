package com.infolga.messengerinfolga;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


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

//    public static void showNotificationNewMessage(Context cont, Messages messages) {
//
//
//        Intent intent = new Intent(cont, ConversationActivity.class);
//
//        intent.putExtra("conversation_id", messages.getConversation_id());
//
//
//        // Intent intent = new Intent(this, MainActivity2.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(cont, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeResource(cont.getResources(), R.drawable.img, options);
//
//        String channelId = "default_notification_channel_id";   // getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(cont, channelId)
//
//                        .setContentTitle(messages.getUs_FL_name())
//                        .setContentText(messages.getMessage())
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri)
//                        .setColor(cont.getResources().getColor(R.color.colorPrimary))
//                        .setContentIntent(pendingIntent);
//
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            notificationBuilder.setLargeIcon(bitmap)
//                    .setSmallIcon(R.drawable.ic_launcher_foreground);
//        }
//
//
//        NotificationManager notificationManager =
//                (NotificationManager) cont.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//
//
//    }

    private ArrayList<MtContact> readAllContact() {
        ArrayList<MtContact> list = new ArrayList<>();
        Cursor cursor = cont.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                final String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    Cursor pCur = cont.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Log.e("TAG", "phone=" + phone);
                        list.add(new MtContact(name, phone));
                    }
                    pCur.close();
                }
            }
        }
        cursor.close();
        return list;
    }

    public void showNotificationNewMessage( String Title, String Text, int conversation_id) {

        Log.e(TAG, "conversation_id 1231312312   "+conversation_id);
        Intent intent2 = new Intent(cont, ConversationActivity.class);

        intent2.putExtra("conversation_id2",conversation_id);


        // Intent intent = new Intent(this, MainActivity2.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(cont, 0, intent2, 0);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(cont.getResources(), R.drawable.img, options);

        String channelId = "default_notification_channel_id";   // getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(cont, channelId)

                        .setContentTitle(Title)
                        .setContentText(Text)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setColor(cont.getResources().getColor(R.color.colorPrimary))
                        .setContentIntent(pendingIntent);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationBuilder.setLargeIcon(bitmap)
                    .setSmallIcon(R.drawable.ic_launcher_foreground);

        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationBuilder.setLargeIcon(bitmap)
                    .setSmallIcon(R.mipmap.ic_launcher_round);
            // .setSmallIcon(R.drawable.ic_launcher_foreground);
        }

        NotificationManager notificationManager =
                (NotificationManager) cont.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }


    public void setmHandlerActiveViwe(Handler mHandlerActiveViwe) {
        this.mHandlerActiveViwe = mHandlerActiveViwe;
    }

    public void HsendMessage(Message msg) {
        while (mHandlerDB == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mHandlerDB.sendMessage(msg);
    }

    public String getAccessToken() {
        Cursor cursor = DB.getReadableDatabase().rawQuery(cont.getString(R.string.SQLgetAccessToken), null);
        cursor.moveToFirst();
        String s;

        if (cursor.getCount() == 0) {
            s = null;
        } else {
            s = cursor.getString(0);
        }
        cursor.close();
        return s;
    }

    private void clearUsers() {
        DB.getReadableDatabase().execSQL(cont.getString(R.string.SQLgetDeleteUS));
    }

    private void clearMessages() {
        DB.getReadableDatabase().execSQL(cont.getString(R.string.SQLgetDeleteMes));
    }

    private void clearConversation() {
        DB.getReadableDatabase().execSQL(cont.getString(R.string.SQLgetDeleteCon));
    }

    private void clearParticipants() {
        DB.getReadableDatabase().execSQL(cont.getString(R.string.SQLgetDeletePsr));
    }

    public void clearAccess() {
        DB.getReadableDatabase().execSQL(cont.getString(R.string.SQLgetDeleteAccess));
    }


    private String corectPhone(String phone) {

        phone = phone.replaceAll("\\s+", "");
        if (Pattern.matches("[+]\\d{12}", phone)) {
            phone = phone.replace("+", "");
        }
        if (Pattern.matches("38\\d{10}", phone)) {
            phone = phone.replace("38", "");
        }
        if (Pattern.matches("\\d{10}", phone)) {
            return "38" + phone;
        } else return null;
    }


    private void SQL_insert_into_accses_token(int users_id, String token) {
        String sql = cont.getString(R.string.SQLgetAddToken);
        String sql_exe = String.format(sql, Integer.toString(users_id), token);
        DB.getReadableDatabase().execSQL(sql_exe);
    }

    private void SQL_insert_into_users(User user) {
        int users_id = user.getUsers_id();
        String user_name = user.getUser_name();
        String phone = user.getPhone();
        String email = user.getEmail();
        String first_name = user.getFirst_name();
        String last_name = user.getLast_name();
        int is_active = user.getIs_active();
        String last_online_at = user.getLast_online_at();

        String sql = cont.getString(R.string.SQLgetgetCountUserWhereId);
        String sql_exe = String.format(sql, Integer.toString(users_id));

        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            sql = cont.getString(R.string.SQLgetAddUs);
            sql_exe = String.format(sql, Integer.toString(users_id), user_name, phone, email, first_name, last_name, Integer.toString(is_active), last_online_at);
            DB.getReadableDatabase().execSQL(sql_exe);

        } else {
            sql = cont.getString(R.string.SQLgetUpdateUs);
            sql_exe = String.format(sql, Integer.toString(users_id), user_name, phone, email, first_name, last_name, Integer.toString(is_active), last_online_at);
            DB.getReadableDatabase().execSQL(sql_exe);
        }
        cursor.close();
    }


//    protected User SQL_select_all_into_users_where_like(String user_name_like, int numRow) {
//        String sql = cont.getString(R.string.SQLgetgetuserslike);
//        String sql_exe = String.format(sql, "%" + user_name_like + "%");
//        // Log.e(TAG, sql_exe);
//        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);
//        cursor.moveToFirst();
//        if (cursor.getCount() > numRow) {
//            cursor.move(numRow);
//            User user = new User();
//            user.setUsers_id(cursor.getInt(cursor.getColumnIndex("users_id")));
//            user.setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
//            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
//            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
//            user.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
//            user.setLast_name(cursor.getString(cursor.getColumnIndex("last_name")));
//            user.setIs_active(cursor.getInt(cursor.getColumnIndex("is_active")));
//            user.setLast_online_at(cursor.getString(cursor.getColumnIndex("last_online_at")));
//            cursor.close();
//            return user;
//        } else {
//            cursor.close();
//            return null;
//        }
//
//    }


    protected User SQL_select_Myusers_where() {
        String sql = cont.getString(R.string.SQLgetgetMyUsers);

        // Log.e(TAG, sql_exe);
        Cursor cursor = DB.getReadableDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            User user = new User();
            user.setUsers_id(cursor.getInt(cursor.getColumnIndex("users_id")));
            user.setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
            user.setLast_name(cursor.getString(cursor.getColumnIndex("last_name")));
            user.setIs_active(cursor.getInt(cursor.getColumnIndex("is_active")));
            user.setLast_online_at(cursor.getString(cursor.getColumnIndex("last_online_at")));
            cursor.close();
            return user;
        } else {
            cursor.close();
            return null;
        }

    }


    protected ArrayList<Conversation> SQL_select_all_conversation_for_rv() {


        String sql = cont.getString(R.string.SQLgetgetConversationRV);

        User user = SQL_select_Myusers_where();
        String sql_exe;
        if (user != null) {
            sql_exe = String.format(sql, Integer.toString(user.getUsers_id()));
        } else {

            sql_exe = String.format(sql, "-1");
        }
        // Log.e(TAG, sql);
        ArrayList<Conversation> conversationArrayList = new ArrayList<>();

        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);

        while (cursor.moveToNext()) {
            Conversation conversation = new Conversation();

            conversation.setConversation_id(cursor.getInt(cursor.getColumnIndex("con_conversation_id")));
            conversation.setTitle(cursor.getString(cursor.getColumnIndex("con_title")));
            conversation.setPhoto_id(cursor.getInt(cursor.getColumnIndex("con_photo_id")));
            conversation.setCreator_id(cursor.getInt(cursor.getColumnIndex("con_creator_id")));
            conversation.setCreated_at(cursor.getString(cursor.getColumnIndex("con_created_at")));
            conversation.setType(cursor.getString(cursor.getColumnIndex("con_type")));
            conversation.setName_conversation(cursor.getString(cursor.getColumnIndex("con_name_conversation")));
            conversation.setTime_last_viev(cursor.getString(cursor.getColumnIndex("con_time_lasting")));

            conversation.setTime_last_Mes(cursor.getString(cursor.getColumnIndex("mes_created_at")));
            conversation.setText_last_Mes(cursor.getString(cursor.getColumnIndex("mes_message")));
            conversationArrayList.add(conversation);

        }
        cursor.close();
        return conversationArrayList;

    }


    protected Conversation SQL_select_conversation_from_conversations_where_id(int conversation_id) {

        User user = SQL_select_Myusers_where();


        String sql = cont.getString(R.string.SQLgetgetConversation_ID);
        String sql_exe = String.format(sql, Integer.toString(user.getUsers_id()), Integer.toString(conversation_id));
        // Log.e (TAG,sql_exe);
        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);

        cursor.moveToFirst();

        Conversation conversation = new Conversation();

        conversation.setConversation_id(cursor.getInt(cursor.getColumnIndex("con_conversation_id")));
        conversation.setTitle(cursor.getString(cursor.getColumnIndex("con_title")));
        conversation.setPhoto_id(cursor.getInt(cursor.getColumnIndex("con_photo_id")));
        conversation.setCreator_id(cursor.getInt(cursor.getColumnIndex("con_creator_id")));
        conversation.setCreated_at(cursor.getString(cursor.getColumnIndex("con_created_at")));
        conversation.setType(cursor.getString(cursor.getColumnIndex("con_type")));
        conversation.setName_conversation(cursor.getString(cursor.getColumnIndex("con_name_conversation")));
        conversation.setTime_last_viev(cursor.getString(cursor.getColumnIndex("con_time_lasting")));
        cursor.close();
        return conversation;

    }


    protected ArrayList<Messages> SQL_select_all_Messages_in_con_for_rv(int conversation_id) {
        String sql = cont.getString(R.string.SQLgetgetMessagesInConversation);
        String sql_exe;
        sql_exe = String.format(sql, Integer.toString(conversation_id));
        //Log.e(TAG, sql);
        ArrayList<Messages> messagesArrayList = new ArrayList<>();

        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);

        while (cursor.moveToNext()) {
            Messages messages = new Messages();

            messages.setId(cursor.getInt(cursor.getColumnIndex("mes_id")));
            messages.setConversation_id(cursor.getInt(cursor.getColumnIndex("mes_conversation_id")));
            messages.setSender_id(cursor.getInt(cursor.getColumnIndex("mes_sender_id")));
            messages.setMessage_type(cursor.getString(cursor.getColumnIndex("mes_message_type")));
            messages.setMessage(cursor.getString(cursor.getColumnIndex("mes_message")));
            messages.setAttachment_thumb_url(cursor.getString(cursor.getColumnIndex("mes_attachment_thumb_url")));
            messages.setAttachment_url(cursor.getString(cursor.getColumnIndex("mes_attachment_url")));
            messages.setCreated_at(cursor.getString(cursor.getColumnIndex("mes_created")));

            messages.setUs_FL_name(cursor.getString(cursor.getColumnIndex("us_FL_name")));
            messagesArrayList.add(messages);

        }
        cursor.close();
        return messagesArrayList;

    }

    protected User SQL_select_User_where_id(int users_id) {

        String sql = cont.getString(R.string.SQLgetgetCountUserWhereId);
        String sql_exe = String.format(sql, Integer.toString(users_id));
        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);
        User user = new User();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            user.setUsers_id(cursor.getInt(cursor.getColumnIndex("users_id")));
            user.setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
            user.setLast_name(cursor.getString(cursor.getColumnIndex("last_name")));
            user.setIs_active(cursor.getInt(cursor.getColumnIndex("is_active")));
            user.setLast_online_at(cursor.getString(cursor.getColumnIndex("last_online_at")));
        }
        return user;
    }


    protected ArrayList<User> SQL_select_all_into_users_where_like(String user_name_like) {
        String sql = cont.getString(R.string.SQLgetgetuserslike);
        String sql_exe = String.format(sql, "%" + user_name_like + "%");
        // Log.e(TAG, sql_exe);
        ArrayList<User> users = new ArrayList<>();

        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);
        while (cursor.moveToNext()) {
            User user = new User();
            user.setUsers_id(cursor.getInt(cursor.getColumnIndex("users_id")));
            user.setUser_name(cursor.getString(cursor.getColumnIndex("user_name")));
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
            user.setLast_name(cursor.getString(cursor.getColumnIndex("last_name")));
            user.setIs_active(cursor.getInt(cursor.getColumnIndex("is_active")));
            user.setLast_online_at(cursor.getString(cursor.getColumnIndex("last_online_at")));
            users.add(user);
        }
        cursor.close();
        return users;

    }


    protected int SQL_select_count_into_users_where_like(String user_name_like) {
        String sql = cont.getString(R.string.SQLgetgetuserslikeCount);

        String sql_exe = String.format(sql, "%" + user_name_like + "%");
        //Log.e(TAG, sql_exe);
        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    private void SQL_insert_conversation(Conversation conversation) {
        int conversation_id = conversation.getConversation_id();
        String title = conversation.getTitle();
        String name_conversation = conversation.getName_conversation();
        int photo_id = conversation.getPhoto_id();
        String type = conversation.getType();
        int creator_id = conversation.getCreator_id();
        String created_at = conversation.getCreated_at();
        String time_last_viev = conversation.getTime_last_viev();

        String sql = cont.getString(R.string.SQLgetgetConversation);
        String sql_exe = String.format(sql, Integer.toString(conversation_id));

        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);

        if (cursor.getCount() == 0) {
            sql = cont.getString(R.string.SQLgetAddConversation);
            sql_exe = String.format(sql, Integer.toString(conversation_id), title, name_conversation, Integer.toString(photo_id), type, Integer.toString(creator_id), created_at, time_last_viev);
            // Log.e(TAG, sql_exe);
            DB.getReadableDatabase().execSQL(sql_exe);
        } else {
            sql = cont.getString(R.string.SQLgetUpdateConversation);
            sql_exe = String.format(sql, Integer.toString(conversation_id), title, name_conversation, Integer.toString(photo_id), type, Integer.toString(creator_id), created_at, time_last_viev);
            //Log.e(TAG, sql_exe);
            DB.getReadableDatabase().execSQL(sql_exe);
        }
        cursor.close();
    }

    private int SQL_insert_messages(Messages messages) {
        int id = messages.getId();
        int conversation_id = messages.getConversation_id();
        int sender_id = messages.getSender_id();
        String message_type = messages.getMessage_type();
        String message = messages.getMessage();
        String attachment_thumb_url = messages.getAttachment_thumb_url();
        String attachment_url = messages.getAttachment_url();
        String created_at = messages.getCreated_at();

        String sql = cont.getString(R.string.SQLgetgetMessages);
        String sql_exe = String.format(sql, Integer.toString(id));
//Log.e(TAG, sql_exe);
        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);
        if (cursor.getCount() == 0) {
            sql = cont.getString(R.string.SQLgetAddMessages);
            sql_exe = String.format(sql, Integer.toString(id), Integer.toString(conversation_id), Integer.toString(sender_id), message_type, message, attachment_thumb_url, attachment_url, created_at);
            // Log.e(TAG, sql_exe);
            DB.getReadableDatabase().execSQL(sql_exe);
            cursor.close();
            return 1;
        } else {
            sql = cont.getString(R.string.SQLgetUpdateMessages);
            sql_exe = String.format(sql, Integer.toString(id), Integer.toString(conversation_id), Integer.toString(sender_id), message_type, message, attachment_thumb_url, attachment_url, created_at);
            // Log.e(TAG, sql_exe);
            DB.getReadableDatabase().execSQL(sql_exe);
            cursor.close();
            return -1;
        }

    }

    private void SQL_insert_participants(Participants participants) {
        int id_participants = participants.getId_participants();
        int conversation_id = participants.getConversation_id();
        int users_id = participants.getUsers_id();


        String sql = cont.getString(R.string.SQLgetgetParticipants);
        String sql_exe = String.format(sql, Integer.toString(id_participants));

        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);
        if (cursor.getCount() == 0) {
            sql = cont.getString(R.string.SQLgetAddParticipants);
            sql_exe = String.format(sql, Integer.toString(id_participants), Integer.toString(conversation_id), Integer.toString(users_id));
            //Log.e(TAG, sql_exe);
            DB.getReadableDatabase().execSQL(sql_exe);
        } else {
            sql = cont.getString(R.string.SQLgetUpdateParticipants);
            sql_exe = String.format(sql, Integer.toString(id_participants), Integer.toString(conversation_id), Integer.toString(users_id));
            // Log.e(TAG, sql_exe);
            DB.getReadableDatabase().execSQL(sql_exe);
        }
        cursor.close();
    }


    private void SQL_insert_into_contacts(int contacts_id, String first_name, String last_name, String phone) {

        String sql = cont.getString(R.string.SQLgetgetcontact);
        String sql_exe = String.format(sql, Integer.toString(contacts_id));
        Cursor cursor = DB.getReadableDatabase().rawQuery(sql_exe, null);
        if (cursor.getCount() == 0) {
            sql = cont.getString(R.string.SQLgetAddcontact);
            sql_exe = String.format(sql, Integer.toString(contacts_id), phone, first_name, last_name);
            // Log.e(TAG, sql_exe);
            DB.getReadableDatabase().execSQL(sql_exe);
        } else {
            sql = cont.getString(R.string.SQLgetUpdatecontact);
            sql_exe = String.format(sql, Integer.toString(contacts_id), phone, first_name, last_name);
            //Log.e(TAG, sql_exe);
            DB.getReadableDatabase().execSQL(sql_exe);
        }
        cursor.close();
    }

    private Message userRegistrationResponse(MyXML XML) {
        int result = XML.getAttributeResult();

        Message message = new Message();
        message.what = XML.getAttributeResult();
        mHandlerActiveViwe.sendMessage(message);
        switch (result) {
            case MSG.XML_RESULT_VALUES_OK:
                message.what = MSG.USER_REGISTRATION_SUCCESSFUL;
                break;

            case MSG.XML_RESULT_VALUES_PHONE_UNAVAILABLE:
                message.what = MSG.USER_REGISTRATION_FAIL_PHONE;
                break;

            case MSG.XML_RESULT_VALUES_USER_NAME_UNAVAILABLE:
                message.what = MSG.USER_REGISTRATION_FAIL_USER_NAME;
                break;
        }
        return message;
    }

    private Message userLoginResponse(MyXML myXML) {
        int result = myXML.getAttributeResult();
        Message message = new Message();
        ;
        switch (result) {
            case MSG.XML_RESULT_VALUES_OK:
                message.what = MSG.USER_LOGIN_SUCCESSFUL;
                clearAccess();
                Element element = myXML.getCildElement(MSG.XML_ELEMENT_USER);

                User user = new User(element);

                String token = myXML.getValueInActionsXML(MSG.XML_ELEMENT_TOKEN);

                SQL_insert_into_users(user);
                SQL_insert_into_accses_token(user.getUsers_id(), token);


                break;
            case MSG.XML_RESULT_VALUES_INCORRECT_PASSWORD:
                message.what = MSG.USER_LOGIN_FAIL_PASSWORD;

                break;
            case MSG.XML_RESULT_VALUES_PHONE_NOT_FOUND:
                message.what = MSG.USER_LOGIN_FAIL_PHONE;

                break;
            default:
                break;
        }
        return message;
    }

    private Message addContact(MyXML myXML) {
        int result = myXML.getAttributeResult();
        Message messages = new Message();
        switch (result) {
            case MSG.XML_RESULT_VALUES_OK:
                String contacts_id = myXML.getValueInActionsXML(MSG.XML_ELEMENT_CONTACT_ID);
                String phone = myXML.getValueInActionsXML(MSG.XML_ELEMENT_PHONE);
                String first_name = myXML.getValueInActionsXML(MSG.XML_ELEMENT_FIRST_NAME);
                String last_name = myXML.getValueInActionsXML(MSG.XML_ELEMENT_LAST_NAME);

                SQL_insert_into_contacts(Integer.parseInt(contacts_id), first_name, last_name, phone);
                break;

            default:
                break;
        }
        return messages;
    }

    private Message addUsers(MyXML myXML) {
        int result = myXML.getAttributeResult();
        Message message = new Message();
        switch (result) {
            case MSG.XML_RESULT_VALUES_OK:

                List<Element> cildrenListElement = myXML.getCildrenListElement(MSG.XML_ELEMENT_USER);
                //Log.e(TAG, cildrenListElement.get(0).getName());

                for (int i = 0; i < cildrenListElement.size(); i++) {
                    User user = new User(cildrenListElement.get(i));
                    SQL_insert_into_users(user);
                }

                message.what = MSG.UPDATE_RECYCLER_VIEV_ADD_USERS;

                break;
            default:
                break;
        }
        return message;
    }

    private Message addConversation(MyXML myXML) {
        int result = myXML.getAttributeResult();
        Message message = new Message();
        switch (result) {
            case MSG.XML_RESULT_VALUES_OK:

                List<Element> cildrenListElement = myXML.getCildrenListElement(MSG.XML_ELEMENT_CONVERSATION);
                //Log.e(TAG, cildrenListElement.get(0).getName());

                for (int i = 0; i < cildrenListElement.size(); i++) {
                    Conversation conversation = new Conversation(cildrenListElement.get(i));
                    SQL_insert_conversation(conversation);
                }

                List<Element> cildrenListElementP = myXML.getCildrenListElement(MSG.XML_ELEMENT_PARTICIPANTS);
                for (int i = 0; i < cildrenListElementP.size(); i++) {
                    Participants participants = new Participants(cildrenListElementP.get(i));

                    SQL_insert_participants(participants);
                }


                message.what = MSG.UPDATE_RECYCLER_VIEV_ADD_CONVERSATION;

                break;
            default:
                break;
        }
        return message;

    }

    private void analisXML(MyXML XML) {
        Message message;
        if (MSG.XML_TYPE_REQUEST.equals(XML.getTypeXML())) {
            switch (XML.getIdActionsXML()) {
                case MSG.XML_CONVERSATION_ADD:

                    mHandlerActiveViwe.sendMessage(addConversation(XML));
                    Element cildElement = XML.getCildElement(MSG.XML_ELEMENT_CONVERSATION);
                    Conversation conversation = new Conversation(cildElement);

                    Log.e(TAG, "conversation_id 1231312312   "+conversation.getName_conversation());

                    showNotificationNewMessage("You were added to the conversation", "",conversation.getConversation_id() );
                    break;

            }


        } else {

            switch (XML.getIdActionsXML()) {
                case MSG.XML_USER_LOGIN:
                    mHandlerActiveViwe.sendMessage(userLoginResponse(XML));

                    break;
                case MSG.XML_USER_REGISTRATION:
                    mHandlerActiveViwe.sendMessage(userRegistrationResponse(XML));

                    break;
                case MSG.XML_CONTACT_ADD:
                    mHandlerActiveViwe.sendMessage(addContact(XML));

                    break;
                case MSG.XML_GET_USERS_FROM_LIKE:
                    mHandlerActiveViwe.sendMessage(addUsers(XML));
                    break;
                case MSG.XML_CONVERSATION_ADD:

                    mHandlerActiveViwe.sendMessage(addConversation(XML));
                    break;

                case MSG.XML_GET_ALL_CONVERSATION:
                    mHandlerActiveViwe.sendMessage(addConversationALL(XML));

                    break;
                case MSG.XML_GET_MESSAGES_FO_DATE:
                    mHandlerActiveViwe.sendMessage(addMessages(XML));
                    break;
                case MSG.XML_ADD_MESSAGES:
                    Element cildrenM = XML.getCildElement(MSG.XML_ELEMENT_MESSAGES);
                    //Log.e(TAG, cildrenListElement.get(0).getName());

                    Messages messages = new Messages(cildrenM);
                    if (SQL_insert_messages(messages) == 1) {
                        message = new Message();
                        message.obj = messages;
                        message.what = MSG.UPDATE_RECYCLER_VIEV_ADD_MESSAGES;
                        mHandlerActiveViwe.sendMessage(message);
                    }

                    break;
                case MSG.XML_CONVERSATION_ADD_USERS:
                    addConversationALL(XML);
                    message = new Message();
                    message.what = MSG.UPDATE_RECYCLER_VIEV_ADD_CONVERSATION_ALL;
                    mHandlerActiveViwe.sendMessage(message);

                    break;
            }
        }
    }

    private Message addMessages(MyXML myXML) {
        int result = myXML.getAttributeResult();
        Message   message = new Message();
        switch (result) {
            case MSG.XML_RESULT_VALUES_OK:


                List<Element> cildrenM = myXML.getCildrenListElement(MSG.XML_ELEMENT_MESSAGES);
                //Log.e(TAG, cildrenListElement.get(0).getName());
                for (int i = 0; i < cildrenM.size(); i++) {
                    Messages messages = new Messages(cildrenM.get(i));
                    SQL_insert_messages(messages);
                }

                message.what = MSG.UPDATE_RECYCLER_VIEV_ADD_MESSAGES_DATE;
                break;
            default:
                break;
        }
        return message;
    }

    private Message addConversationALL(MyXML myXML) {

        int result = myXML.getAttributeResult();
        Message message = new Message();
        switch (result) {
            case MSG.XML_RESULT_VALUES_OK:

                List<Element> cildrenListElementC = myXML.getCildrenListElement(MSG.XML_ELEMENT_CONVERSATION);
                List<Element> cildrenListElementM = myXML.getCildrenListElement(MSG.XML_ELEMENT_MESSAGES);
                List<Element> cildrenListElementP = myXML.getCildrenListElement(MSG.XML_ELEMENT_PARTICIPANTS);
                List<Element> cildrenListElementU = myXML.getCildrenListElement(MSG.XML_ELEMENT_USER);
                //Log.e(TAG, cildrenListElement.get(0).getName());
                if (cildrenListElementC != null) {
                    for (int i = 0; i < cildrenListElementC.size(); i++) {
                        Conversation conversation = new Conversation(cildrenListElementC.get(i));

                        SQL_insert_conversation(conversation);
                    }
                }
                if (cildrenListElementM != null) {
                    for (int i = 0; i < cildrenListElementM.size(); i++) {
                        Messages messages = new Messages(cildrenListElementM.get(i));

                        SQL_insert_messages(messages);
                    }
                }
                if (cildrenListElementP != null) {
                    for (int i = 0; i < cildrenListElementP.size(); i++) {
                        Participants participants = new Participants(cildrenListElementP.get(i));

                        SQL_insert_participants(participants);
                    }
                }
                if (cildrenListElementU != null) {
                    for (int i = 0; i < cildrenListElementU.size(); i++) {
                        User user = new User(cildrenListElementU.get(i));
                        SQL_insert_into_users(user);
                    }
                }

                message.what = MSG.UPDATE_RECYCLER_VIEV_ADD_CONVERSATION_ALL;
                break;
            default:
                break;
        }

        return message;
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
                case MSG.PACKAGE_ARRIVES:
                    try {
                        X = new MyXML((byte[]) msg.obj);
                        //Log.e(TAG, X.toString());
                        analisXML(X);

                    } catch (JDOMException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG.USER_LOGIN:
                    bundle = (Bundle) msg.obj;
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_USER_LOGIN);
                    X.addChild(MSG.XML_ELEMENT_PHONE, bundle.getString(MSG.XML_ELEMENT_PHONE));
                    //Log.e(TAG, X.toString());
                    X.addChild(MSG.XML_ELEMENT_PASSWORD, bundle.getString(MSG.XML_ELEMENT_PASSWORD));
                    X.addChild(MSG.XML_ELEMENT_DRVISE_INFO, Build.MANUFACTURER + " " + Build.MODEL);
                    X.addChild(MSG.XML_ELEMENT_DRVISE_TOKEN, FirebaseInstanceId.getInstance().getToken());
                    //Log.e(TAG, X.toString());
                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toByteArray();
                    clearUsers();
                    clearAccess();
                    clearParticipants();
                    clearConversation();
                    clearMessages();

                    ServerConnect.instanse(null).HsendMessage(message);
                    // Log.e(TAG, X.toString());
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
                    message.obj = X.toByteArray();
                    clearUsers();
                    clearAccess();
                    ServerConnect.instanse(null).HsendMessage(message);
                    Log.e(TAG, X.toString());

                    break;
                case MSG.READ_ALL_CONTACT:
                    ArrayList<MtContact> mtContacts = readAllContact();

                    for (int i = 0; i < mtContacts.size(); i++) {
                        String phone = corectPhone(mtContacts.get(i).getPhone());
                        if (phone != null) {
                            X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_CONTACT_ADD);
                            X.addChild(MSG.XML_ELEMENT_TOKEN, getAccessToken());

                            //Log.e("TAG", phone);
                            String[] subStr;

                            String str = mtContacts.get(i).getName();
                            subStr = str.split(" ", 2);
                            X.addChild(MSG.XML_ELEMENT_PHONE, phone);

                            X.addChild(MSG.XML_ELEMENT_FIRST_NAME, subStr[0]);

                            //Log.e("TAG F", subStr[0]);
                            if (subStr.length > 1) {
                                X.addChild(MSG.XML_ELEMENT_LAST_NAME, subStr[1]);
                                //Log.e("TAG L", subStr[1]);

                            } else {
                                X.addChild(MSG.XML_ELEMENT_LAST_NAME, " ");
                            }
                            Log.e(TAG, "send_contact");
                            message = new Message();
                            message.what = MSG.SEND_PACKEGE;
                            message.obj = X.toByteArray();
                            ServerConnect.instanse(cont).HsendMessage(message);

                        }
                    }
                    break;

                case MSG.GET_USERS_FROM_LIKE:
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_GET_USERS_FROM_LIKE);
                    X.addChild(MSG.XML_ELEMENT_TOKEN, getAccessToken());
                    X.addChild(MSG.XML_ELEMENT_USER_NAME_LIKE, (String) msg.obj);
                    X.addChild(MSG.XML_ELEMENT_AFTER, Integer.toString(msg.arg1));
                    X.addChild(MSG.XML_ELEMENT_BEFORE, Integer.toString(msg.arg2));
                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toByteArray();
                    ServerConnect.instanse(cont).HsendMessage(message);
                    break;

                case MSG.XML_CONVERSATION_SINGLE_CREATE:
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_CONVERSATION_ADD);
                    X.addChild(MSG.XML_ELEMENT_TOKEN, getAccessToken());

                    Element element = new Element(MSG.XML_ELEMENT_USER);
                    element = MyXML.addChild(element, MSG.XML_ELEMENT_USERS_ID, Integer.toString(msg.arg1));

                    X.addChild(MSG.XML_ELEMENT_TYPE, MSG.XML_ELEMENT_TYPE_SINGLE);
                    X.addChildElement(element);
                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toByteArray();
                    ServerConnect.instanse(cont).HsendMessage(message);

                    break;
                case MSG.GET_ALL_CONVERSATION:
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_GET_ALL_CONVERSATION);
                    X.addChild(MSG.XML_ELEMENT_TOKEN, getAccessToken());

                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toByteArray();
                    ServerConnect.instanse(cont).HsendMessage(message);
                    break;
                case MSG.GET_MESSAGES_FO_DATE:
                    Messages messages = (Messages) msg.obj;
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_GET_MESSAGES_FO_DATE);
                    X.addChild(MSG.XML_ELEMENT_TOKEN, getAccessToken());
                    X.addChild(MSG.XML_ELEMENT_COUNT, Integer.toString(msg.arg2));
                    X.addChildElement(messages.getXMLElement());

                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toByteArray();
                    ServerConnect.instanse(cont).HsendMessage(message);

//                    Message message1 = new Message();
//                    message1.what = MSG.UPDATE_RECYCLER_VIEV_LOAD_RUN;
//                    mHandlerActiveViwe.sendMessage(message1);

                    break;
                case MSG.ADD_MESSAGES:
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_ADD_MESSAGES);
                    X.addChild(MSG.XML_ELEMENT_TOKEN, getAccessToken());
                    X.addChildElement(((Messages) msg.obj).getXMLElement());

                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toByteArray();
                    ServerConnect.instanse(cont).HsendMessage(message);
                    break;

                case MSG.SING_OUT:
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_SING_OUT);
                    X.addChild(MSG.XML_ELEMENT_TOKEN, getAccessToken());

                    clearAccess();

                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toByteArray();
                    ServerConnect.instanse(cont).HsendMessage(message);
                    break;
                case MSG.FIREBASE_MESSAGING:


                    Messages messages1 = (Messages) msg.obj;
                    User user = SQL_select_User_where_id(messages1.getSender_id());
                    messages1.setUs_FL_name(user.getFirst_name() + " " + user.getLast_name());
                    if (SQL_insert_messages(messages1) == 1) {
                        if (mHandlerActiveViwe != null) {
                            message = new Message();
                            message.obj = messages1;
                            message.what = MSG.UPDATE_RECYCLER_VIEV_ADD_MESSAGES;
                            mHandlerActiveViwe.sendMessage(message);

                        } else {

                            User user1 = SQL_select_Myusers_where();


                            if (user1.getUsers_id() != messages1.getSender_id()) {
                                DD_SQL.instanse(null).showNotificationNewMessage(messages1.getUs_FL_name(),messages1.getMessage(),  messages1.getConversation_id());
                            }
                        }
                    }
                    break;
                case MSG.XML_CONVERSATION_GROUP_CREATE:
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_CONVERSATION_ADD);
                    X.addChild(MSG.XML_ELEMENT_TOKEN, getAccessToken());

                    element = new Element(MSG.XML_ELEMENT_USER);
                    element = MyXML.addChild(element, MSG.XML_ELEMENT_USERS_ID, Integer.toString(msg.arg1));

                    X.addChild(MSG.XML_ELEMENT_TYPE, MSG.XML_ELEMENT_TYPE_GROUP);
                    X.addChild(MSG.XML_ELEMENT_NAME_CONVERSATION, (String) msg.obj);

                    X.addChildElement(element);

                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toByteArray();
                    ServerConnect.instanse(cont).HsendMessage(message);
                    break;
                case MSG.CONVERSATION_ADD_USERS:
                    X = new MyXML(MSG.XML_TYPE_REQUEST, MSG.XML_CONVERSATION_ADD_USERS);
                    X.addChild(MSG.XML_ELEMENT_TOKEN, getAccessToken());

                    X.addChild(MSG.XML_ELEMENT_USERS_ID, Integer.toString(msg.arg1));
                    X.addChild(MSG.XML_ELEMENT_CONVERSATION_ID, Integer.toString(msg.arg2));

                    message = new Message();
                    message.what = MSG.SEND_PACKEGE;
                    message.obj = X.toByteArray();
                    ServerConnect.instanse(cont).HsendMessage(message);
                    break;


                default:
                    break;
            }
        }
    }


    private class MyDB extends SQLiteOpenHelper {

        public MyDB(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

            //context.deleteDatabase(DATABASE_NAME);

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
                Log.e("file is not found", this.toString());
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
