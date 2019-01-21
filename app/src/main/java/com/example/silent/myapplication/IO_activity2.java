package com.example.silent.myapplication;

/**
*IO界面的辅界面activity，用以展示io界面剩余数据
**/

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class IO_activity2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.io_layout2);

        android.support.constraint.ConstraintLayout preLayout = (android.support.constraint.ConstraintLayout)findViewById(R.id.io_layout);
        preLayout.setBackgroundResource(R.drawable.bg4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.io_page,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.io_pageBtn:
                Intent intent = new Intent(IO_activity2.this,IO_activity.class);
                startActivity(intent);
                break;
             default:
                 break;
        }
        return super.onOptionsItemSelected(item);
    }
}
