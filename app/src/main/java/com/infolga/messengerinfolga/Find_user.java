package com.infolga.messengerinfolga;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class Find_user extends AppCompatActivity implements View.OnClickListener, OnItemClickListener {

    private static final String TAG = "Find_user";
    private Handler mHandlerActiveViwe;
    private EditText userNameFind;
    private MyAdapterFindUser myAdapterFindUser;
    private RecyclerView recyclerView;

    private String user_name;

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

//        Message message = new Message();
//        message.what = MSG.XML_CONVERSATION_SINGLE_CREATE;
//        message.obj = user;
//        DD_SQL.instanse(this).HsendMessage(message);

        Log.e(TAG, " onItemClick:  1 ");
        showDialog(((User) user).getFirst_name() + " " + ((User) user).getLast_name(), ((User) user).getUsers_id());
        Log.e(TAG, " onItemClick:   2");

    }


    void showDialog(String s, int user_id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        String inputText = s;

        DialogFragment newFragment = MyDialogFragment.newInstance(inputText, user_id);
        newFragment.show(ft, "dialog");

    }

    public static class MyDialogFragment extends DialogFragment {

        String mText;
        int user_id;

        static MyDialogFragment newInstance(String text, int user_id) {
            MyDialogFragment f = new MyDialogFragment();

            Bundle args = new Bundle();
            args.putString("text", text);
            args.putInt("user_id", user_id);
            f.setArguments(args);

            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            mText = "Create a chat with " + getArguments().getString("text");
            user_id = getArguments().getInt("user_id");
            return new AlertDialog.Builder(getActivity())
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle("Create chat")
                    .setMessage(mText)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    Message message = new Message();
                                    message.what = MSG.XML_CONVERSATION_SINGLE_CREATE;
                                    message.arg1 = user_id;
                                    DD_SQL.instanse(null).HsendMessage(message);

                                    Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
                                }
                            }
                    )
                    .setNegativeButton("Cancel", null)
                    .create();
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
                    myAdapterFindUser.MSG_updete();
                    myAdapterFindUser.notifyDataSetChanged();
                    break;

                default:
                    break;


            }


        }
    }


}
