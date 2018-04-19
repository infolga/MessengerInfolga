package com.infolga.messengerinfolga;

import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MyAdapterConversation extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_PROGRES_BAR = 3;

    private static final int COUNT_MESS_LOADING = 75;


    private final String TAG = "MyAdapterConversation";
    boolean is_loading;
    boolean WaitingForDownload;

    private User MyUser;
    private Conversation conversation;
    private OnItemClickListener mItemClickListener;
    private boolean invalide = true;

    private ArrayList<Messages> messagesArrayList;
    private int count;
    private int ExpectedNumberOfMessages;

    public MyAdapterConversation(Conversation con) {
        ExpectedNumberOfMessages = 0;
        invalide = true;
        conversation = con;

        MyUser = DD_SQL.instanse(null).SQL_select_Myusers_where();
        //Log.e(TAG, "public MyAdapterConversation ");
        messagesArrayList = DD_SQL.instanse(null).SQL_select_all_Messages_in_con_for_rv(conversation.getConversation_id());
        is_loading = true;
        WaitingForDownload = false;

    }


    public void invalideRV() {
        invalide = true;
    }

    public void WaitingForDownloadF() {
        WaitingForDownload = false;
    }

    public void ExpectedNumberOfMessagesIncr() {
        ExpectedNumberOfMessages++;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        // Log.e(TAG, "onCreateViewHolder " + viewType);
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_sent, viewGroup, false);
            return new SentMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_received, viewGroup, false);
            return new ReceivedMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_PROGRES_BAR) {

            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_message_progres_bar, viewGroup, false);
            return new ReceivedMessageProgresBarViewHolder(view);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // if (position +COUNT_MESS_LOADING/2 > ExpectedNumberOfMessages - 1 && !WaitingForDownload &&is_loading)
        if (2 * position > ExpectedNumberOfMessages - 1 && !WaitingForDownload && is_loading) {
            Messages messages = new Messages();


//            Log.e(TAG, "onBindViewHolder position1 " + position);
//             Log.e(TAG, "onBindViewHolder maxBin1 " + ExpectedNumberOfMessages);
//            Log.e(TAG, "onBindViewHolder  is_loading1   " + is_loading);
//            Log.e(TAG, "getItemCount1  " + count);

            // *проверка на выход за массив


            messages.setCreated_at(messagesArrayList.get(ExpectedNumberOfMessages - 1).getCreated_at());
            messages.setId(messagesArrayList.get(ExpectedNumberOfMessages - 1).getId());
            messages.setConversation_id(conversation.getConversation_id());

            Message message = new Message();
            message.what = MSG.GET_MESSAGES_FO_DATE;
            message.obj = messages;
            message.arg2 = COUNT_MESS_LOADING;

            ExpectedNumberOfMessages += COUNT_MESS_LOADING;
            DD_SQL.instanse(null).HsendMessage(message);

            WaitingForDownload = true;
        }

//        Log.e(TAG, "onBindViewHolder position " + position);
//        Log.e(TAG, "onBindViewHolder maxBin " + ExpectedNumberOfMessages);
//        Log.e(TAG, "onBindViewHolder  is_loading   " + is_loading);
//        Log.e(TAG, "getItemCount  " + count);


        //Log.e(TAG, "onBindViewHolder position " + position);
        // Log.e(TAG, "onBindViewHolder maxBin " + ExpectedNumberOfMessages);
        //Log.e(TAG, "onBindViewHolder  is_loading   " + is_loading);

        if (count == position) {
            //Log.e(TAG, "count == position");

        } else {
            Messages messages = messagesArrayList.get(position);

            switch (holder.getItemViewType()) {
                case VIEW_TYPE_MESSAGE_SENT:
                    ((SentMessageViewHolder) holder).bind(messages);
                    break;
                case VIEW_TYPE_MESSAGE_RECEIVED:
                    ((ReceivedMessageViewHolder) holder).bind(messages);
                    break;
                case VIEW_TYPE_MESSAGE_PROGRES_BAR:
                    ((ReceivedMessageProgresBarViewHolder) holder).bind(messages);
            }
        }

    }


    public void updeteMmessagesArrayList() {
        messagesArrayList = DD_SQL.instanse(null).SQL_select_all_Messages_in_con_for_rv(conversation.getConversation_id());
        count = messagesArrayList.size();
    }


    @Override
    public int getItemCount() {


        if (invalide) {
            updeteMmessagesArrayList();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date now = Calendar.getInstance().getTime();

            Messages messages = new Messages();
            messages.setCreated_at(df.format(now));
            messages.setId(0);
            messages.setConversation_id(conversation.getConversation_id());

            Message message = new Message();
            message.what = MSG.GET_MESSAGES_FO_DATE;
            message.obj = messages;
            message.arg2 = COUNT_MESS_LOADING;

            ExpectedNumberOfMessages = COUNT_MESS_LOADING;
            DD_SQL.instanse(null).HsendMessage(message);
            invalide = false;
            WaitingForDownload = true;
            is_loading = true;



        }


        if (count < ExpectedNumberOfMessages && !WaitingForDownload) {
            is_loading = false;
        }

//         Log.e(TAG, "getItemCount2  " + count);
//         Log.e(TAG, "getItemCount  ExpectedNumberOfMessages 2  " + ExpectedNumberOfMessages);
//         Log.e(TAG, "getItemCount  is_loading  2 " + is_loading);

        if (is_loading) {
            return count + 1;
        } else {
            return count;
        }
    }

    @Override
    public int getItemViewType(int position) {
        //Log.e(TAG, "getItemViewType " + position);

        if (position == count) {
            //Log.e(TAG, "VIEW_TYPE_MESSAGE_PROGRES_BAR " + position);
            return VIEW_TYPE_MESSAGE_PROGRES_BAR;

        } else {
            Messages message = messagesArrayList.get(position);

            if (message.getSender_id() == MyUser.getUsers_id()) {
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }
    }

    public class SentMessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageText, timeText;

        public SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(Messages UserMessage) {
            messageText.setText(UserMessage.getMessage());

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            Date now = Calendar.getInstance().getTime();
            Date dateMS = null;
            try {

                //dateMS = df.parse(conversation.getTime_last_Mes());
                dateMS = df.parse(UserMessage.getCreated_at());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Log.e(TAG,"ddd "+ conversation.getCreated_at());
            // Log.e(TAG, df.format(dateMS));
            Calendar cal = Calendar.getInstance();

            cal.setTime(now);
            cal.add(Calendar.HOUR, -12);
            Date nowhous = cal.getTime();

            cal.setTime(now);
            cal.add(Calendar.DAY_OF_YEAR, -7);
            Date nowweek = cal.getTime();

            //timeText.setText(dateMS.toString());

//TODO
            if (nowhous.before(dateMS)) {
                DateFormat day = new SimpleDateFormat("HH:mm", Locale.getDefault());
                timeText.setText(day.format(dateMS));
            } else if (nowweek.before(dateMS)) {
                DateFormat week = new SimpleDateFormat("E", Locale.getDefault());
                timeText.setText(week.format(dateMS));
            } else {
                DateFormat mons = new SimpleDateFormat("d MMM", Locale.getDefault());
                timeText.setText(mons.format(dateMS));
            }
        }
    }

    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);

        }

        void bind(Messages UserMessage) {
            messageText.setText(UserMessage.getMessage());

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            Date now = Calendar.getInstance().getTime();
            Date dateMS = null;
            try {

                //dateMS = df.parse(conversation.getTime_last_Mes());
                dateMS = df.parse(UserMessage.getCreated_at());

            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Log.e(TAG,"ddd "+ conversation.getCreated_at());
            // Log.e(TAG, df.format(dateMS));
            Calendar cal = Calendar.getInstance();

            cal.setTime(now);
            cal.add(Calendar.HOUR, -12);
            Date nowhous = cal.getTime();

            cal.setTime(now);
            cal.add(Calendar.DAY_OF_YEAR, -7);
            Date nowweek = cal.getTime();

            //timeText.setText( dateMS.toString());
            //SimpleDateFormat  day = new SimpleDateFormat("HH:mm", Locale.getDefault());


            if (nowhous.before(dateMS)) {
                DateFormat day = new SimpleDateFormat("HH:mm", Locale.getDefault());
                timeText.setText(day.format(dateMS));
            } else if (nowweek.before(dateMS)) {
                DateFormat week = new SimpleDateFormat("E", Locale.getDefault());
                timeText.setText(week.format(dateMS));
            } else {
                DateFormat mons = new SimpleDateFormat("d MMM", Locale.getDefault());
                timeText.setText(mons.format(dateMS));
            }
            nameText.setText(UserMessage.getUs_FL_name());
        }
    }

    public class ReceivedMessageProgresBarViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public ReceivedMessageProgresBarViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarMess);
        }

        void bind(Messages UserMessage) {
        }
    }
}
