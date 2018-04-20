package com.infolga.messengerinfolga;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "ConversationActivity";

    private Conversation conversation;
    private EditText textMessage;

    private RecyclerView mMessageRecycler;
    private MyAdapterConversation mMessageAdapter;

    private Handler mHandlerActiveViwe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = findViewById(R.id.toolbarCon);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        int conversation_id = intent.getIntExtra(MSG.XML_ELEMENT_CONVERSATION_ID,-1) ;

        Log.e(TAG, "conversation_id   "+conversation_id);

        conversation = DD_SQL.instanse(this).SQL_select_conversation_from_conversations_where_id(conversation_id);
        toolbar.setTitle(conversation.getName_conversation());
        setTitle(conversation.getName_conversation());

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(layoutManager);


        mMessageAdapter = new MyAdapterConversation(conversation);
        mMessageRecycler.setAdapter(mMessageAdapter);

        Button buttonSend = findViewById(R.id.button_chatbox_send);
        buttonSend.setOnClickListener(this);

        textMessage = findViewById(R.id.edittext_chatbox);


        mHandlerActiveViwe = new ConversationActivity.MyHandlerActiveViwe();
        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);

    }

    protected void onStart() {
        super.onStart();

        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);
        //mMessageAdapter.setInvalide();
        mMessageAdapter.updeteMmessagesArrayList();

        if (mMessageAdapter.getItemCount() > 0) {
            //TODO
            mMessageRecycler.smoothScrollToPosition(0);
        }
        mMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.Add_user) {
            if (conversation.getType().equals("group")) {
                Intent intent = new Intent(this, Find_user.class);
                intent.putExtra("actions", "addUsers");
                intent.putExtra("convenient_id", conversation.getConversation_id());
                startActivity(intent);
            }
            else { Toast.makeText(getApplicationContext(), "This is a single conversation", Toast.LENGTH_LONG).show();}
        }
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_chatbox_send:
                String mes = textMessage.getText().toString();
                if (mes.length() > 0) {

                    Messages messages = new Messages();
                    messages.setMessage_type("text");
                    messages.setMessage(mes);
                    messages.setConversation_id(conversation.getConversation_id());

                    textMessage.setText(null);
                    Message message = new Message();
                    message.what = MSG.ADD_MESSAGES;
                    message.obj = messages;
                    DD_SQL.instanse(this).HsendMessage(message);

                    if (mMessageAdapter.getItemCount() > 0) {
                        mMessageRecycler.smoothScrollToPosition(0);
                    }
                }
                break;
            default:
                break;
        }

    }

    private class MyHandlerActiveViwe extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            Log.e(TAG, "# сообщенее: " + msg.what);
            switch (msg.what) {
//                case MSG.UPDATE_RECYCLER_VIEV:
//                    // myAdapterFindUser.invalideRV();
//
//                    mMessageAdapter.updeteMmessagesArrayList();
//
//                    //mMessageAdapter.notifyItemInserted(mMessageAdapter.getItemCount());
//                    mMessageAdapter.notifyDataSetChanged();
//
//                    break;
                case MSG.UPDATE_RECYCLER_VIEV_ADD_MESSAGES:
                    // myAdapterFindUser.invalideRV();
                    Messages messages = (Messages) msg.obj;
                    if (conversation.getConversation_id() == messages.getConversation_id()) {

                        mMessageAdapter.updeteMmessagesArrayList();
                        mMessageAdapter.ExpectedNumberOfMessagesIncr();
                        //mMessageAdapter.notifyItemInserted(mMessageAdapter.getItemCount());
                        mMessageAdapter.notifyItemInserted(0);

                        if (mMessageAdapter.getItemCount() > 0) {
                            mMessageRecycler.smoothScrollToPosition(0);
                        }
                    } else {

                        Log.e(TAG, "messages.getConversation  "+messages.getConversation_id());
                        DD_SQL.instanse(null).showNotificationNewMessage(messages.getUs_FL_name(), messages.getMessage(), messages.getConversation_id());

                    }

                    break;
                case MSG.UPDATE_RECYCLER_VIEV_ADD_MESSAGES_DATE:


                    mMessageAdapter.updeteMmessagesArrayList();
                    mMessageAdapter.WaitingForDownloadF();

                    mMessageAdapter.notifyDataSetChanged();


                    //mMessageAdapter.notifyItemInserted(mMessageAdapter .getItemCount()-1); ;

                    break;

                default:
                    break;


            }


        }
    }

}
