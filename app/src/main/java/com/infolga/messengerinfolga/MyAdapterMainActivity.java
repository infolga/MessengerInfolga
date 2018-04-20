package com.infolga.messengerinfolga;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by infol on 20.03.2018.
 */

public class MyAdapterMainActivity extends RecyclerView.Adapter<MyAdapterMainActivity.ViewHolder> {

    private final String TAG = "MyAdapterMainActivity";
    private OnItemClickListener mItemClickListener;
    private boolean invalide = true;
    private boolean MSG_updete = true;
    private ArrayList<Conversation> ConversationsArr;
    private int count;
    private String userMameLike;

    public void setInvalide() {
        this.invalide = true;
    }
    //private int maxBin;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.conversation_headline, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {

        if (invalide) {
            Message message = new Message();
            message.what = MSG.GET_ALL_CONVERSATION;
            DD_SQL.instanse(null).HsendMessage(message);
            invalide = false;
              Log.e(TAG, " message.what = MSG.GET_ALL_CONVERSATION;");
        }
        if (MSG_updete) {

            ConversationsArr = DD_SQL.instanse(null).SQL_select_all_conversation_for_rv();
            count = ConversationsArr.size();
            MSG_updete = false;
        }
        // Log.e(TAG, "getItemCount  " + DD_SQL.instanse(null).SQL_select_count_into_users_where_like(userMameLike));
        return count;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Conversation conversation = ConversationsArr.get(position);
        holder.conversation = conversation;
        holder.name_conversation.setText(conversation.getName_conversation());
        holder.last_msg.setText(conversation.getText_last_Mes());

        if (conversation . getType().equals("group")){
            holder.name_conversation.setTextColor(Color.RED);

        }else   {
            holder.name_conversation.setTextColor(Color.BLUE);
        }

          DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date now = Calendar.getInstance().getTime();
        Date dateMS = null;
        try {
            if (conversation.getTime_last_Mes() != null) {

                dateMS = df.parse(conversation.getTime_last_Mes());
            } else if (conversation.getCreated_at() != null) {

                dateMS = df.parse(conversation.getCreated_at());

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateMS != null) {

            Calendar cal = Calendar.getInstance();

            cal.setTime(now);
            cal.add(Calendar.HOUR, -12);
            Date nowhous = cal.getTime();

            cal.setTime(now);
            cal.add(Calendar.DAY_OF_YEAR, -7);
            Date nowweek = cal.getTime();

            //
            holder.last_msg_time.setText( dateMS.toString());


            if (nowhous.before(dateMS)) {
                 DateFormat day = new SimpleDateFormat("HH:mm", Locale.getDefault());
                holder.last_msg_time.setText(day.format(dateMS));
            } else if (nowweek.before(dateMS)) {
                 DateFormat week = new SimpleDateFormat("E", Locale.getDefault());
                holder.last_msg_time.setText(week.format(dateMS));
            } else {
               DateFormat mons = new SimpleDateFormat("d MMM", Locale.getDefault());
                holder.last_msg_time.setText(mons.format(dateMS));
            }

        }

    }


    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void MSG_updete() {
        MSG_updete = true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Conversation conversation;

        private TextView name_conversation;
        private TextView last_msg;
        private TextView last_msg_time;
        private CircularImageView avatarImg;
        private TextView countMsg;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name_conversation = itemView.findViewById(R.id.name_conversation);
            last_msg = itemView.findViewById(R.id.last_msg);
            last_msg_time = itemView.findViewById(R.id.last_time);
            avatarImg = itemView.findViewById(R.id.avatar_photo);
            countMsg = itemView.findViewById(R.id.TvCountMsg);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, conversation);
            }

        }
    }

}