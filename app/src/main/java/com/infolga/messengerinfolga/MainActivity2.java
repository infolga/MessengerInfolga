package com.infolga.messengerinfolga;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnItemClickListener {

    private final String TAG = "MainActivity2";
    TextView nav_user_name;
    TextView nav_user_username;
    TextView nav_user_phone;

    private Handler mHandlerActiveViwe;

    private MyAdapterMainActivity myAdapterMainActivity;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        nav_user_name = (TextView) hView.findViewById(R.id.FLmyusers);
        nav_user_phone = (TextView) hView.findViewById(R.id.myusers_phone);
        nav_user_username= (TextView)  hView.findViewById(R.id.myusersname);



        mHandlerActiveViwe = new MyHandlerActiveViwe();
        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);
        ServerConnect.instanse(this);
        Intent intent = new Intent(this, LoginActivity.class);

        if (DD_SQL.instanse(this).getAccessToken() == null) {
            startActivity(intent);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.offsetChildrenHorizontal(5);
        layoutManager.offsetChildrenVertical(10);
        recyclerView.setLayoutManager(layoutManager);

        myAdapterMainActivity = new MyAdapterMainActivity();
        myAdapterMainActivity.SetOnItemClickListener(this);
        recyclerView.setAdapter(myAdapterMainActivity);


    }

    @Override
    protected void onStart() {
        super.onStart();

        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);
        myAdapterMainActivity.setInvalide();
        myAdapterMainActivity.MSG_updete();
        myAdapterMainActivity.notifyDataSetChanged();
        User user = DD_SQL.instanse(this).SQL_select_Myusers_where();
        if (user != null) {
            nav_user_name.setText(user.getFirst_name() + " " + user.getLast_name());
            nav_user_phone.setText(user.getPhone());
            nav_user_username.setText(user.getUser_name());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.search) {
//
//            Toast toast = Toast.makeText(getApplicationContext(), "dsfsdfsdf", Toast.LENGTH_SHORT);
//
//            View view = this.getCurrentFocus();
//            if (view != null) {
//                toast.show();
//
//                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.showSoftInput(this.getCurrentFocus(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
//
//            }
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_group) {

            Intent intent = new Intent(this, Find_user.class);
            intent.putExtra("actions", "newGroup");
            startActivity(intent);
        } else if (id == R.id.new_channal) {


        } else if (id == R.id.new_chat) {

            Intent intent = new Intent(this, Find_user.class);
            intent.putExtra("actions", "newChat");
            startActivity(intent);

        } else if (id == R.id.nav_contacts) {

        } else if (id == R.id.nav_Seting) {

        } else if (id == R.id.nav_Sign_Out) {
            Message message = new Message();
            message.what = MSG.SING_OUT;
            DD_SQL.instanse(null).HsendMessage(message);

            finish();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onItemClick(View view, Object o) {

        Conversation conversation = (Conversation) o;
        Intent intent = new Intent(this, ConversationActivity.class);

        intent.putExtra(MSG.XML_ELEMENT_CONVERSATION_ID, conversation.getConversation_id());

        startActivity(intent);

        Log.e(TAG, o.toString());


    }

    private class MyHandlerActiveViwe extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            Log.e(TAG, "# сообщенее: " + msg.what);
            switch (msg.what) {
//                case MSG.UPDATE_RECYCLER_VIEV:
//                    // myAdapterFindUser.invalideRV();
//                    myAdapterMainActivity.MSG_updete();
//                    myAdapterMainActivity.notifyDataSetChanged();
//                    break;
                case MSG.UPDATE_RECYCLER_VIEV_ADD_CONVERSATION:
                    // myAdapterFindUser.invalideRV();
                    myAdapterMainActivity.MSG_updete();
                    myAdapterMainActivity.notifyDataSetChanged();
                    break;
                case MSG.UPDATE_RECYCLER_VIEV_ADD_CONVERSATION_ALL:
                    // myAdapterFindUser.invalideRV();
                    myAdapterMainActivity.MSG_updete();
                    myAdapterMainActivity.notifyDataSetChanged();
                    break;


                case MSG.UPDATE_RECYCLER_VIEV_ADD_MESSAGES:
                    // myAdapterFindUser.invalideRV();

                    myAdapterMainActivity.MSG_updete();
                    myAdapterMainActivity.notifyDataSetChanged();

                    Messages messages = (Messages) msg.obj;

                    User user1 = DD_SQL.instanse(null).SQL_select_Myusers_where();


                    if (user1.getUsers_id()!=messages.getSender_id())
                    {
                        Log.e(TAG, " ,messages.getConversation_id()  " +messages.getConversation_id());
                        DD_SQL.instanse(null).showNotificationNewMessage(messages.getUs_FL_name(),messages.getMessage(),messages.getConversation_id());
                    }




                    break;

                default:
                    break;


            }


        }
    }

}
