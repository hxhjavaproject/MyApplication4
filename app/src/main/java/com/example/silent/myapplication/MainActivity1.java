package com.example.silent.myapplication;
/**
 *主界面activity，为程序界面
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int BLUETOOTH = 16;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_routine:
                    //当前活动不做跳转
                    return true;
                case R.id.navigation_view:
                    Intent intent = new Intent(MainActivity1.this,View_activity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_argument:
                    Intent intent2 = new Intent(MainActivity1.this,Argument_activity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_io:
                    Intent intent3 = new Intent(MainActivity1.this,IO_activity.class);
                    startActivity(intent3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.constraint.ConstraintLayout preLayout = (android.support.constraint.ConstraintLayout)findViewById(R.id.content_main1);
        preLayout.setBackgroundResource(R.drawable.bg);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity1, menu);

        MenuItem item2 = menu.add(0, BLUETOOTH, 0, "bluetooth");
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item2.setOnMenuItemClickListener(listener);
        item2.setIcon(R.drawable.bluetooth);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu_routine1) {
            Toast.makeText(MainActivity1.this, "即将打开程序", Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.action_menu_routine2){
            Toast.makeText(MainActivity1.this, "即将保存程序", Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.action_menu_routine3){
            Toast.makeText(MainActivity1.this, "即将下载程序", Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.action_menu_routine4){
            Toast.makeText(MainActivity1.this, "即将删除程序", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case BLUETOOTH:
                 //   Toast.makeText(MainActivity1.this, "请注意蓝牙是否已连接！！！", Toast.LENGTH_LONG).show();
                 //   Intent intent2 = new Intent(MainActivity1.this,BTClient.class);
                 //   startActivity(intent2);
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_task1) {
            Intent intent = new Intent(MainActivity1.this,Task_activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_task2) {
            Intent intent = new Intent(MainActivity1.this,Task_activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_task3) {
            Intent intent = new Intent(MainActivity1.this,Task_activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_task4) {
            Intent intent = new Intent(MainActivity1.this,Task_activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_user) {
            Intent intent = new Intent(MainActivity1.this,Task_activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity1.this,Task_activity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
