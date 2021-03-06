package com.infolga.messengerinfolga;

import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;


public class MyAdapterFindUser extends RecyclerView.Adapter<MyAdapterFindUser.ViewHolder> {

    private final String TAG = "MyAdapterFindUser";
    private OnItemClickListener mItemClickListener;
    private boolean invalide = true;
    private boolean MSG_updete = true;
    private ArrayList<User> users;
    private int count;
    private String userMameLike;
    private int maxBin;

    public void MSG_updete() {
        MSG_updete = true;
    }

    public MyAdapterFindUser(String userMameLike) {
        maxBin = 0;
        invalide = true;
        this.userMameLike = userMameLike;
        // Log.e(TAG, "public MyAdapterFindUser(String userMameLike)");

    }

    public void setUserMameLike(String userMameLike) {
        maxBin = 0;
        invalide = true;
        this.userMameLike = userMameLike;
    }

    public void invalideRV() {
        invalide = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //Log.e(TAG, "onCreateViewHolder   " + viewGroup);

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position > maxBin) {

            Message message = new Message();
            message.what = MSG.GET_USERS_FROM_LIKE;
            message.arg1 = maxBin;
            message.arg2 = position + 9;
            message.obj = userMameLike;
            maxBin = position + 10;
            DD_SQL.instanse(null).HsendMessage(message);

            //Log.e(TAG, "onBindViewHolder DD_SQL.instanse(null).HsendMessage(message)");
        }
        //Log.e(TAG, "onBindViewHolder maxBin " + maxBin);
        //Log.e(TAG, "onBindViewHolder  position  " + position);
        //Log.e(TAG, "onBindViewHolder  userMameLike  " + userMameLike);


        //User user = DD_SQL.instanse(null).SQL_select_all_into_users_where_like(userMameLike, position);

        User user = users.get(position);

        holder.user = user;
        holder.TvName.setText(user.getFirst_name() + " " + user.getLast_name());
        holder.userName.setText(user.getUser_name());

        //Log.e(TAG, "userName     " + user.getUser_name());
    }

    @Override
    public int getItemCount() {

        if (invalide) {
            Message message = new Message();

            message.what = MSG.GET_USERS_FROM_LIKE;
            message.arg1 = 0;
            message.arg2 = 10;
            message.obj = userMameLike;
            maxBin = 10;
            DD_SQL.instanse(null).HsendMessage(message);
            invalide = false;

            // Log.e(TAG, "getItemCount DD_SQL.instanse(null).HsendMessage(message)");

        }
        if (MSG_updete) {
            users = DD_SQL.instanse(null).SQL_select_all_into_users_where_like(userMameLike);
            count = DD_SQL.instanse(null).SQL_select_count_into_users_where_like(userMameLike);
            MSG_updete = false;
        }
        // Log.e(TAG, "getItemCount  " + DD_SQL.instanse(null).SQL_select_count_into_users_where_like(userMameLike));
        return count;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView TvName;
        private TextView userName;
        private CircularImageView avatarImg;

        private User user;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            avatarImg = view.findViewById(R.id.avatar_photo);
            TvName = view.findViewById(R.id.FLname);
            userName = view.findViewById(R.id.user_name);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, user);
            }
           // Log.d(TAG, "onClick " + user.getUsers_id());
        }
    }


    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
