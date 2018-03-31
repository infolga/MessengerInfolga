package com.infolga.messengerinfolga;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Find_user extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Find_user";
    private Handler mHandlerActiveViwe;
    private EditText userNameFind;
    private MyAdapterFindUser myAdapterFindUser;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        mHandlerActiveViwe = new Find_user.MyHandlerActiveViwe();
        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);


        userNameFind = findViewById(R.id.userNameFind);

        Button button = findViewById(R.id.btFind);
        button.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.RvUserFind);
        recyclerView.setHasFixedSize(false);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        myAdapterFindUser = new MyAdapterFindUser(userNameFind.getText().toString());
        recyclerView.setAdapter(myAdapterFindUser);


    }

    @Override
    protected void onStart() {
        super.onStart();
        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);
    }

    @Override
    public void onClick(View view) {
        Message message;

        switch (view.getId()) {
            case R.id.btFind://  R.id.btFind:

                // Log.e(TAG, userNameFind.getText().toString());
                myAdapterFindUser.setUserMameLike(userNameFind.getText().toString());
                myAdapterFindUser.invalideRV();
                myAdapterFindUser.notifyDataSetChanged();

                break;

            default:
                break;


        }
    }


    private class MyHandlerActiveViwe extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View focusView = null;

            Log.e(TAG, "# сообщенее: " + msg.what);
            switch (msg.what) {

                case MSG.UPDATE_RECYCLER_VIEV:
                    // myAdapterFindUser.invalideRV();
                    myAdapterFindUser.notifyDataSetChanged();
                    break;

                default:
                    break;


            }


        }
    }


}
