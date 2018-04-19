package com.infolga.messengerinfolga;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Find_user extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private static final String TAG = "Find_user";
    private Handler mHandlerActiveViwe;
    private EditText userNameFind;
    private MyAdapterFindUser myAdapterFindUser;
    private RecyclerView recyclerView;
    private String user_name;
    private int convenient_id;
    private String actions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        actions = intent.getStringExtra("actions");
        convenient_id = intent.getIntExtra("convenient_id", -1);
        if (actions != null) {

            if (actions.equals("newChat")) {

                setTitle("Create new chat");
            } else if (actions.equals("newGroup")) {
                setTitle("Create a new group");
            } else if (actions.equals("addUsers")) {
                setTitle("Add user");
            }
        }

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
        myAdapterFindUser.SetOnItemClickListener(this);
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

                if (!userNameFind.getText().toString().equals(user_name)) {
                    user_name = userNameFind.getText().toString();
                    // Log.e(TAG, userNameFind.getText().toString());
                    myAdapterFindUser.setUserMameLike(userNameFind.getText().toString());
                    myAdapterFindUser.invalideRV();
                    myAdapterFindUser.notifyDataSetChanged();
                }
                break;

            default:
                break;


        }
    }

    @Override
    public void onItemClick(View view, Object user) {

        if (actions != null) {

            if (actions.equals("newChat")) {

                showDialogNewChat(((User) user).getFirst_name() + " " + ((User) user).getLast_name(), ((User) user).getUsers_id());
            } else if (actions.equals("newGroup")) {
                showDialogNewGroup(((User) user).getUsers_id());
            } else if (actions.equals("addUsers")) {
                showDialogAddUsers( convenient_id, ((User) user).getFirst_name() + " " + ((User) user).getLast_name(), ((User) user).getUsers_id());
            }
        }
    }

    private void showDialogAddUsers(final int convenient_id, String s, final int users_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a user " + s+"?");


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message message = new Message();
                message.what = MSG.CONVERSATION_ADD_USERS;
                message.arg1 = users_id;
                message.arg2 = convenient_id;
                DD_SQL.instanse(null).HsendMessage(message);

                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showDialogNewGroup(final int userss_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter the name of the new group chat");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().length() > 0) {

                    Message message = new Message();
                    message.what = MSG.XML_CONVERSATION_GROUP_CREATE;
                    message.arg1 = userss_id;
                    message.obj = input.getText().toString();
                    DD_SQL.instanse(null).HsendMessage(message);


                    Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Short name group", Toast.LENGTH_LONG).show();

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    void showDialogNewChat(String s, final int user_id) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create a chat with " + s);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message message = new Message();
                message.what = MSG.XML_CONVERSATION_SINGLE_CREATE;
                message.arg1 = user_id;
                DD_SQL.instanse(null).HsendMessage(message);

                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }


    private class MyHandlerActiveViwe extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View focusView = null;

            Log.e(TAG, "# сообщенее: " + msg.what);
            switch (msg.what) {

//                case MSG.UPDATE_RECYCLER_VIEV:
//                    // myAdapterFindUser.invalideRV();
//                    myAdapterFindUser.MSG_updete();
//                    myAdapterFindUser.notifyDataSetChanged();
//                    break;

                case MSG.UPDATE_RECYCLER_VIEV_ADD_USERS:
                    // myAdapterFindUser.invalideRV();
                    myAdapterFindUser.MSG_updete();
                    myAdapterFindUser.notifyDataSetChanged();
                    break;

                default:
                    break;


            }


        }
    }


}
