package com.e.laocow.batteryutil;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ShutdownTipActivity extends ActionBarActivity {
    private TextView tipTV;
    private boolean shutdownFlag=true;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shutdown_tip);
        tipTV=(TextView)findViewById(R.id.shutdownTimeTipTV);
        StateManager.getInstance().setContext(this);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) { // 处理消息
                switch (msg.what){
                    case 1:
                        tipTV.setText("请充电，否则"+String.valueOf(msg.obj)+"秒后强制关机");
                        break;
                    case 2:
                        tipTV.setText("关机中。。。");
                        break;
                    case 3:

                        tipTV.setText("已经取消了关机,");
                        break;
                    case 5:
                        tipTV.setText("本程序将自动关闭");

                        break;
                    case 4:
                        tipTV.setText("手机正在充电，停止自动关机");
                        break;
                }
            }
        };

        Countdown countdown=new Countdown();
        new Thread(countdown).start();
    }
    public void cancelShutdownBut(View view){
        shutdownFlag=false;
    }
    class Countdown implements Runnable{
        private int timeout=15;

        @Override
        public void run() {
            for(int i=0;i<timeout;i++){
                if(!shutdownFlag){
                    shutdownFlag=false;
                    break;
                }
                if(isCharging()){
                    shutdownFlag=false;
                    sendMessage(4);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg=new Message();
                msg.what=1;
                msg.obj=timeout-i;
                handler.sendMessage(msg);

            }
            if(shutdownFlag) {
               sendMessage(2);
                Shutdown.nowShutdown();
            }else{
                sendMessage(3);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendMessage(5);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }
        private void sendMessage(int what){
            Message msg=new Message();
            msg.what=what;

            handler.sendMessage(msg);

        }
        private boolean isCharging(){
            IntentFilter intentFilter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent intent=ShutdownTipActivity.this.registerReceiver(null,intentFilter);
            int stats=intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            return stats==BatteryManager.BATTERY_STATUS_FULL||BatteryManager.BATTERY_STATUS_CHARGING==stats;

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_shutdown_tip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
