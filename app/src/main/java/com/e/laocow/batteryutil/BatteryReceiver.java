package com.e.laocow.batteryutil;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BatteryReceiver extends BroadcastReceiver {
    public BatteryReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        StateManager.getInstance().setContext(context);
        StateManager.getInstance().setBatteryScale(getBatteryScale(intent));
        if(!Settings.getRunFlag(context)){

            return;
        }
        KeyguardManager keyguardManager=(KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isKeyguardFlag=keyguardManager.inKeyguardRestrictedInputMode();

        if(Intent.ACTION_BATTERY_LOW.equals(intent.getAction())){
            if (isKeyguardFlag) {
                // keyguard on
                Shutdown.nowShutdown();

                Log.d("batteryReceiver","在锁屏状态下已经收到测试广播");

            }else {
                if(Settings.getAliveRunFlag(context)) {
                    startShutdownTip(context);
                }
            }
            return;
        }
        if("feemung.com.test".equals(intent.getAction())){
            if (isKeyguardFlag) {
                // keyguard on
                Shutdown.nowShutdown();
                Log.d("batteryReceiver","在锁屏状态下已经收到测试广播");

            }else {
                startShutdownTip(context);
                Log.d("BatteryReceiver",  "已经收到测试广播");
            }
            return;
        }
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Log.d(BatteryReceiver.class.getName(), "开机自动启动");
            Intent ootStartIntent=new Intent(context,MainActivity.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);

        }
    }
    public void startShutdownTip(Context context){
        Intent intent=new Intent(context,ShutdownTipActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public int getBatteryScale(Intent intent){
        //获取当前电量
        int level = intent.getIntExtra("level", 0);
        //电量的总刻度
        int scale = intent.getIntExtra("scale", 100);
        //把它转成百分比
        return((level*100)/scale);
    }
}
