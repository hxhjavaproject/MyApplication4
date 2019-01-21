package com.example.silent.myapplication;

/**
 * 编辑任务界面activity
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Task_activity extends AppCompatActivity{
    Button btn_cancel,btn_empty,btn_ok;

    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_cancel:  //取消本次输入操作
                    finish();     //销毁当前活动
                    break;
                case R.id.btn_empty:   //清空当前输入

                    break;
                case R.id.btn_ok:      //确定输入的程序代码
                    String str = " ";
                    EditText editText = (EditText)findViewById(R.id.editText_task);
                    str = editText.getText().toString();//获取输入的代码，放到str中
                    Toast.makeText(Task_activity.this, str, Toast.LENGTH_SHORT).show();
                    finish();//销毁当前活动
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_layout);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new MyClickListener() {});

        btn_empty = (Button) findViewById(R.id.btn_empty);
        btn_empty.setOnClickListener(new MyClickListener() {});

        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new MyClickListener() {});

        LinearLayout preLayout = (LinearLayout)findViewById(R.id.task_layout);
        preLayout.setBackgroundResource(R.drawable.bg3);
    }
}
