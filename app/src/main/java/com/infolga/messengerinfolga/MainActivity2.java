package com.infolga.messengerinfolga;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private final String TAG = "MainActivity2";

    private Button button;
    private TextView textView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText editText;
    private Handler mHandlerActiveViwe;

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

        button = findViewById(R.id.button);
        button.setOnClickListener(this);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView2);
        // mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);

        //mHandlerActiveViwe = new LoginActivity.MyHandlerActiveViwe();
        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);
        ServerConnect.instanse(this);
        Intent intent = new Intent(this, LoginActivity.class);

        if (DD_SQL.instanse(this).getAccessToken() == null) {
            startActivity(intent);
        }

//
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {

            Toast toast = Toast.makeText(getApplicationContext(), "dsfsdfsdf", Toast.LENGTH_SHORT);

            View view = this.getCurrentFocus();
            if (view != null) {
                toast.show();

                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(this.getCurrentFocus(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.new_group) {

        } else if (id == R.id.new_channal) {

        } else if (id == R.id.new_chat) {

            Intent intent = new Intent(this, Find_user.class);
            startActivity(intent);

        } else if (id == R.id.nav_contacts) {

        } else if (id == R.id.nav_Seting) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        Message message;
        switch (view.getId()) {
            case R.id.button:


                Log.e(TAG, "getItemCount  " + DD_SQL.instanse(null).SQL_select_count_into_users_where_like(""));


//                message = new Message();
//                message.what = MSG.READ_ALL_CONTACT;
//                DD_SQL.instanse(this).HsendMessage(message);


                break;

            default:
                break;


        }


    }
}
