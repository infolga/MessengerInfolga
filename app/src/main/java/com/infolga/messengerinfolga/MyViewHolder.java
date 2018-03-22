package com.infolga.messengerinfolga;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * Created by infol on 20.03.2018.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    private CircularImageView avatar_photo;
    private TextView title;
    private TextView last_time;
    private TextView last_sms;
    private ImageView imageCount;

    public MyViewHolder(View itemView) {
        super(itemView);

        avatar_photo = (CircularImageView) itemView.findViewById(R.id.avatar_photo);
        title = (TextView) itemView.findViewById(R.id.title);
        last_time = (TextView) itemView.findViewById(R.id.last_time);
        last_sms = (TextView) itemView.findViewById(R.id.last_sms);
        imageCount = (ImageView) itemView.findViewById(R.id.imageCount);
    }
}
