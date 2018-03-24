package com.infolga.messengerinfolga;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    private Button button;
    private TextView textView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView2);
        // mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);

        DD_SQL.instanse(this);

        Intent intent= new Intent(this , LoginActivity.class);
        startActivity(intent);
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

        if (id == R.id.nav_group) {

        } else if (id == R.id.nav_radio) {

        } else if (id == R.id.nav_Seting) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button:

                //textView.setText(DD_SQL.instanse(null).getAccessToken() );
                Message message= new Message();
                message.what=ServerConnect.SEND_MESSAGE;


                String s      = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
                    "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                    "    <modelVersion>4.0.0</modelVersion>\n" +
                    "\n" +
                    "    <groupId>123</groupId>\n" +
                    "    <artifactId>345</artifactId>\n" +
                    "    <version>1.0-SNAPSHOT</version>\n" +
                    "    <build>\n" +
                    "        <plugins>\n" +
                    "            <plugin>\n" +
                    "                <groupId>org.apache.maven.plugins</groupId>\n" +
                    "                <artifactId>maven-compiler-plugin</artifactId>\n" +
                    "                <configuration>\n" +
                    "                    <source>1.6</source>\n" +
                    "                    <target>1.6</target>\n" +
                    "                </configuration>\n" +
                    "            </plugin>\n" +
                    "        </plugins>\n" +
                    "    </build>\n" +
                    "\n" +
                    "    <dependencies>\n" +
                    "\n" +
                    "        <dependency>\n" +
                    "            <groupId>io.netty</groupId>\n" +
                    "            <artifactId>netty-all</artifactId> <!-- Use 'netty-all' for 4.0 or above -->\n" +
                    "            <version>4.1.16.Final</version>\n" +
                    "            <scope>compile</scope>\n" +
                    "        </dependency>\n" +
                    "    </dependencies>\n" +
                    "</project>";
                message.obj = editText.getText().toString();
                ServerConnect.instanse(this).getmHandlerServerConnect().sendMessage( message);

                break;

            default:
                break;


        }


    }
}
