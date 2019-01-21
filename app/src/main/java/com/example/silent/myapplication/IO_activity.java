package com.example.silent.myapplication;

/**
 * io主界面activity
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class IO_activity extends AppCompatActivity{

        private static final int MENU_NEXT = 17;
        private static final int BLUETOOTH = 16;

        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_routine:
                        Intent intent2 = new Intent(IO_activity.this,MainActivity1.class);
                        startActivity(intent2);
                        return true;
                    case R.id.navigation_view:
                        Intent intent = new Intent(IO_activity.this,View_activity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_argument:
                        Intent intent3 = new Intent(IO_activity.this,Argument_activity.class);
                        startActivity(intent3);
                        return true;
                    case R.id.navigation_io:
                        //当前活动不做跳转
                        return true;
                }
                return false;
            }
        };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.io_layout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.constraint.ConstraintLayout preLayout = (android.support.constraint.ConstraintLayout)findViewById(R.id.io_layout);
        preLayout.setBackgroundResource(R.drawable.bg4);
    }

    //在页面标题栏添加按钮
    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_io, menu);

        MenuItem item = menu.add(0, MENU_NEXT, 0, R.string.operation_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(listener);
        item.setIcon(R.drawable.next);

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
        if (id == R.id.menu_io1) {
            Toast.makeText(IO_activity.this, "IO下载参数", Toast.LENGTH_SHORT).show();
            return true;
        }else if(id == R.id.menu_io2){
            Toast.makeText(IO_activity.this, "IO恢复厂值", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case MENU_NEXT:
                    Toast.makeText(IO_activity.this, "如需返回主菜单，请点击-->上一页", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(IO_activity.this,IO_activity2.class);
                    startActivity(intent);
                    break;
                case BLUETOOTH:
                 //   Toast.makeText(IO_activity.this, "请注意蓝牙是否已连接！！！", Toast.LENGTH_LONG).show();
                 //   Intent intent2 = new Intent(IO_activity.this,BTClient.class);
                 //   startActivity(intent2);
                    break;
                 default:
                    break;
            }
            return false;
        }
    };
}
