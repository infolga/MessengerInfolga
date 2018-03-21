package com.infolga.messengerinfolga;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by infol on 20.03.2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    public MyAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new MyViewHolder(
                inflater.inflate(R.layout.conversation_headline, viewGroup, false));


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
