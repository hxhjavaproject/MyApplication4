package com.example.silent.myapplication;

/**
 * 监控界面activity
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class View_activity extends AppCompatActivity {

    private static final int BLUETOOTH = 16;

    private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄

    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号

    private InputStream is;    //输入流，用来接收蓝牙数据
    //private TextView text0;    //提示栏解句柄
    private EditText edit0;    //发送数据输入句柄
    private TextView tv_in;       //接收数据显示句柄
    private ScrollView sv;      //翻页句柄
    private String smsg = "";    //显示用数据缓存
    private String fmsg = "";    //保存用数据缓存

    public String filename=""; //用来保存存储的文件名
    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    boolean _discoveryFinished = false;
    boolean bRun = true;
    boolean bThread = false;

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备

//蓝牙模块代码


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_routine:
                    Intent intent2 = new Intent(View_activity.this,MainActivity1.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_view:
                    //当前活动不做跳转
                    return true;
                case R.id.navigation_argument:
                    Intent intent3 = new Intent(View_activity.this,Argument_activity.class);
                    startActivity(intent3);
                    return true;
                case R.id.navigation_io:
                    Intent intent = new Intent(View_activity.this,IO_activity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    //在页面标题栏添加按钮
    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_io, menu);

        MenuItem item2 = menu.add(0, BLUETOOTH, 0, "bluetooth");
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item2.setOnMenuItemClickListener(listener);
        item2.setIcon(R.drawable.bluetooth);
        return true;
    }

    MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case BLUETOOTH:

                    Toast.makeText(View_activity.this, "请注意蓝牙是否已连接！！！", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent(View_activity.this,DeviceListActivity.class);
                    startActivity(intent2);

                    onConnectButtonClickedaaa();
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_layout);

        //底部菜单栏
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //为对应layout添加背景图片
        android.support.constraint.ConstraintLayout preLayout = (android.support.constraint.ConstraintLayout)findViewById(R.id.view_layout);
        preLayout.setBackgroundResource(R.drawable.bg2);

        android.support.constraint.ConstraintLayout preLayoutA = (android.support.constraint.ConstraintLayout)findViewById(R.id.view_layout);
        preLayoutA.setBackgroundResource(R.drawable.bg);

        /* 解决兼容性问题，6.0以上使用新的API*/
        final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
        final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_ACCESS_COARSE_LOCATION);
                Log.e("11111","ACCESS_COARSE_LOCATION");
            }
            if(this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_ACCESS_FINE_LOCATION);
                Log.e("11111","ACCESS_FINE_LOCATION");
            }
        }
        ///---------------------------------------------------
        //text0 = (TextView)findViewById(R.id.Text0);  //得到提示栏句柄
        edit0 = (EditText)findViewById(R.id.Edit0);   //得到输入框句柄
        sv = (ScrollView)findViewById(R.id.ScrollView01);  //得到翻页句柄
        tv_in = (TextView) findViewById(R.id.in);      //得到数据显示句柄

        //如果打开本地蓝牙设备不成功，提示信息，结束程序
        if (_bluetooth == null){
            Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 设置设备可以被搜索
        new Thread(){
            public void run(){
                if(_bluetooth.isEnabled()==false){
                    _bluetooth.enable();
                }
            }
        }.start();
    }



    public static String bytesToHexString(byte[] bytes) {
    String result = "";
    for (int i = 0; i < bytes.length; i++) {
        String hexString = Integer.toHexString(bytes[i] & 0xFF);
        if (hexString.length() == 1) {
            hexString = '0' + hexString;
        }
        result += hexString.toUpperCase();
    }
    return result;
}
    //发送按键响应
    public void onSendButtonClicked(View v){
        int i=0;
        int n=0;
        if(_socket==null){
            Toast.makeText(this, "请先连接HC模块", Toast.LENGTH_SHORT).show();
            return;
        }
        if(edit0.getText().length()==0){
            Toast.makeText(this, "请先输入数据", Toast.LENGTH_SHORT).show();
            return;
        }
        try{

            OutputStream os = _socket.getOutputStream();   //蓝牙连接输出流
            byte[] bos = edit0.getText().toString().getBytes();//把数据放到了bos里
            for(i=0;i<bos.length;i++){
                if(bos[i]==0x0a)n++;
            }//for判断有几个OXOa
            byte[] bos_new = new byte[bos.length+n];//新建byte 型bos_new，长度是bos+n
            n=0;
            for(i=0;i<bos.length;i++){ //手机中换行为0a,将其改为0d 0a后再发送
                if(bos[i]==0x0a){
                    bos_new[n]=0x0d;
                    n++;
                    bos_new[n]=0x0a;
                }else{
                    bos_new[n]=bos[i];
                }
                n++;
            }//该for将0X0a换成0X0b并且放到了bos_new中
            String str = new String( bos);//将bos_new字符数组转换成string
            os.write(getHexBytes(str));//将string型str转换成16进制的字符数组发送出去
//            os.write(bos_new);
        }catch(IOException e){
        }
    }
    //发送数据时的数据转换成16进制
    private byte[] getHexBytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }


    //接收活动结果，响应startActivityForResult()
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "进来了", Toast.LENGTH_SHORT).show();
        switch(requestCode){
            case REQUEST_CONNECT_DEVICE:     //连接结果，由DeviceListActivity设置返回
                // 响应返回结果
                if (resultCode == Activity.RESULT_OK) {   //连接成功，由DeviceListActivity设置返回
                    // MAC地址，由DeviceListActivity设置返回
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // 得到蓝牙设备句柄
                    _device = _bluetooth.getRemoteDevice(address);

                    // 用服务号得到socket
                    try{
                        _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                    }catch(IOException e){
                        Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                    }
                    //连接socket
                    //Button btn = (Button) findViewById(R.id.BtnConnect);
                    try{
                        _socket.connect();
                        Toast.makeText(this, "连接"+_device.getName()+"成功！", Toast.LENGTH_SHORT).show();
                     //   btn.setText("断开");
                    }catch(IOException e){
                        try{
                            Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                            _socket.close();
                            _socket = null;
                        }catch(IOException ee){
                            Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                        }

                        return;
                    }

                    //打开接收线程
                    try{
                        is = _socket.getInputStream();   //得到蓝牙数据输入流
                    }catch(IOException e){
                        Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(bThread==false){
                        readThread.start();
                        bThread=true;
                    }else{
                        bRun = true;
                    }
                }
                break;
            default:break;
        }
    }





        //接收数据线程
    Thread readThread=new Thread(){
/*

            public synchronized void run() {
                byte[] buffer = new byte[1024];
                int bytes;
                InputStream is = null;
                try {
                    is = _socket.getInputStream();
                    while (true) {
                        if ((bytes = is.read(buffer)) > 0) {
                            byte[] buf_data = new byte[bytes];
                            for (int i = 0; i < bytes; i++) {
                                buf_data[i] = buffer[i];
                            }
                            for (int i = 0; i < buf_data.length; i++) {
                                int v = buf_data[i] & 0xFF;
                                String s = Integer.toHexString(v);
                                Message msg = new Message();
                                msg.obj = s;
                                msg.what = GET_MESSAGE;
                                _Handler.sendMessage(msg);
                            }
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
    };
*/
        public void run(){
            int num = 0;
            byte[] buffer = new byte[1024];
            byte[] buffer_new = new byte[1024];
            int i = 0;
            int n = 0;
            bRun = true;
            //接收线程
            while(true){
                try{
                    while(is.available()==0){
                        while(bRun == false){}
                    }
                    while(true){
                        if(!bThread)//跳出循环
                            return;

                        if ((num = is.read(buffer)) > 0) {
                            byte[] buf_data = new byte[num];
                            for (i = 0; i < num; i++) {
                                buf_data[i] = buffer[i];
                            }
                            for (i = 0; i < buf_data.length; i++) {
                                int v = buf_data[i] & 0xFF;
                                String s = Integer.toHexString(v);
                                smsg+=s;   //写入接收缓存
/*                                Message msg = new Message();
                                msg.obj = s;
                                msg.what = GET_MESSAGE;
                                _Handler.sendMessage(msg);
                                */
                            }
                        }
/*
                        num = is.read(buffer);         //读入数据
                        n=0;

                        String s0 = new String(buffer,0,num);
                        fmsg+=s0;    //保存收到数据
                       for(i=0;i<num;i++){
                            if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){
                                buffer_new[n] = 0x0a;
                                i++;
                            }else{
                                buffer_new[n] = buffer[i];
                            }
                            n++;
                        }

                        String s = new String(buffer_new,0,n);
                        smsg+=s;   //写入接收缓存
*/                        if(is.available()==0)break;  //短时间没有数据才跳出进行显示
                    }
                    //发送显示消息，进行显示刷新
                    handler.sendMessage(handler.obtainMessage());
                }catch(IOException e){
                }
            }
        }
    };

    //消息处理队列
    Handler handler= new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            tv_in.setText(smsg);   //显示数据
            sv.scrollTo(0,tv_in.getMeasuredHeight()); //跳至数据最后一页
        }
    };

    //关闭程序掉用处理部分
    public void onDestroy(){
        super.onDestroy();
        if(_socket!=null)  //关闭连接socket
            try{
                _socket.close();
            }catch(IOException e){}
        //	_bluetooth.disable();  //关闭蓝牙服务
    }

    //连接按键响应函数   //按下链接按键  先进这个函数View v
    public void onConnectButtonClickedaaa(){
        Toast.makeText(this, " 进BUTcli", Toast.LENGTH_LONG).show();
        if(_bluetooth.isEnabled()==false){  //如果蓝牙服务不可用则提示
            Toast.makeText(this, " 打开蓝牙中...", Toast.LENGTH_LONG).show();
            return;
        }
        //如未连接设备则打开DeviceListActivity进行设备搜索
        //Button btn = (Button) findViewById(R.id.BtnConnect);
        if(_socket==null){
            Intent serverIntent = new Intent(this, DeviceListActivity.class); //跳转程序设置
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //设置返回宏定义
        }
        else{
            //关闭连接socket
            try{
                bRun = false;
                Thread.sleep(2000);
                is.close();
                _socket.close();
                _socket = null;
            //    btn.setText("连接");
            }catch(IOException e){}
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    //保存按键响应函数
    public void onSaveButtonClicked(View v){
        Save();
    }

    //清除按键响应函数
    public void onClearButtonClicked(View v){
        smsg="";
        fmsg="";
        tv_in.setText(smsg);
        return;
    }

    //退出按键响应函数
    public void onQuitButtonClicked(View v){

        //---安全关闭蓝牙连接再退出，避免报异常----//
        if(_socket!=null){
            //关闭连接socket
            try{
                bRun = false;
                Thread.sleep(2000);

                is.close();
                _socket.close();
                _socket = null;
            }catch(IOException e){}
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        finish();
    }

    //保存功能实现
    private void Save() {
        //显示对话框输入文件名
        LayoutInflater factory = LayoutInflater.from(View_activity.this);  //图层模板生成器句柄
        final View DialogView =  factory.inflate(R.layout.sname, null);  //用sname.xml模板生成视图模板
        new AlertDialog.Builder(View_activity.this)
                .setTitle("文件名")
                .setView(DialogView)   //设置视图模板
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() //确定按键响应函数
                        {
                            public void onClick(DialogInterface dialog, int whichButton){
                                EditText text1 = (EditText)DialogView.findViewById(R.id.sname);  //得到文件名输入框句柄
                                filename = text1.getText().toString();  //得到文件名

                                try{
                                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好

                                        filename =filename+".txt";   //在文件名末尾加上.txt
                                        File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
                                        File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
                                        if(BuildDir.exists()==false)BuildDir.mkdirs();
                                        File saveFile =new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
                                        FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
                                        stream.write(fmsg.getBytes());
                                        stream.close();
                                        Toast.makeText(View_activity.this, "存储成功！\n\r"+saveFile, Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(View_activity.this, "没有存储卡！", Toast.LENGTH_LONG).show();
                                    }

                                }catch(IOException e){
                                    return;
                                }



                            }
                        })
                .setNegativeButton("取消",   //取消按键响应函数,直接退出对话框不做任何处理
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();  //显示对话框
    }
}
