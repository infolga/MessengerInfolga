package com.infolga.messengerinfolga;

import android.os.Message;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessaging";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.

        Log.e(TAG, "remoteMessage getCollapseKey " + remoteMessage.getCollapseKey());
        Log.e(TAG, "remoteMessage getFrom " + remoteMessage.getFrom());
        Log.e(TAG, "remoteMessage getMessageId " + remoteMessage.getMessageId());
        Log.e(TAG, "remoteMessage getMessageType " + remoteMessage.getMessageType());
        Log.e(TAG, "remoteMessage getTo " + remoteMessage.getTo());
        Log.e(TAG, "remoteMessage getTtl " + remoteMessage.getTtl());

        Log.e(TAG, "remoteMessage isEmpty " + remoteMessage.getData().isEmpty());
        Log.e(TAG, "remoteMessage getData " + remoteMessage.getData());

        String type = remoteMessage.getData().get("type2");

        if (type.equals("messages")) {
            Messages messages = new Messages();

            messages.setId(Integer.parseInt(remoteMessage.getData().get("id")));
            messages.setConversation_id(Integer.parseInt(remoteMessage.getData().get("conversation_id")));
            messages.setSender_id(Integer.parseInt(remoteMessage.getData().get("sender_id")));
            messages.setMessage_type(remoteMessage.getData().get("message_type2"));
            messages.setMessage(remoteMessage.getData().get("message"));
            messages.setAttachment_thumb_url(remoteMessage.getData().get("attachment_thumb_url"));
            messages.setAttachment_url(remoteMessage.getData().get("attachment_url"));
            messages.setCreated_at(remoteMessage.getData().get("created_at"));
            messages.setUs_FL_name(remoteMessage.getData().get("us_FL_name"));


            // DD_SQL.showNotificationNewMessage(this,messages);


            Message message = new Message();
            message.what = MSG.FIREBASE_MESSAGING;
            message.obj = messages;

            DD_SQL.instanse(this).HsendMessage(message);
        }else   {}

        Log.w(TAG, "From: " + remoteMessage.getFrom());
        Log.w(TAG, "Notification Message Body: " + remoteMessage.getData());


    }
}
