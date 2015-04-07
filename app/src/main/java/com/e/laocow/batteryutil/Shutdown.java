package com.e.laocow.batteryutil;

import android.app.Notification;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by laocow on 2015/4/4.
 */
public class Shutdown {
    public static void nowShutdown(){
        Log.d("Shutdown", "now the phone shut down");
       try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(
                    process.getOutputStream());
           out.writeBytes("reboot -p\n");
            out.writeBytes("exit\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  void getRoot(){
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(
                    process.getOutputStream());
            //out.writeBytes("reboot -p\n");
            out.writeBytes("exit\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void shutdownAfterSleep(){

        //startForeground((int)System.currentTimeMillis(),new Notification());
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("Shutdown", "start to sleep 10000s");
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                nowShutdown();
            }
        });
        thread.start();
    }
}
