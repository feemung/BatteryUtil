package com.e.laocow.batteryutil;

import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by laocow on 2015/4/4.
 */
public class Shutdown {
    public static Logger l=Logger.getLogger(Shutdown.class);
    public static void nowShutdown(){
        Log.d("Shutdown", "now the phone shut down");
        DbManager.instance().save();
      /* try {

            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream out = new DataOutputStream(
                    process.getOutputStream());
           out.writeBytes("reboot -p\n");
            out.writeBytes("exit\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    /**
     * 判断当前手机是否有ROOT权限
     * @return
     */
    public static boolean isRoot(){
        boolean bool = false;

        try{
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
                bool = false;
            } else {
                bool = true;
            }

        } catch (Exception e) {

        }
        return bool;
    }
    public static synchronized boolean isAppHasRoot()
    {   l.d("isAppHasRoot#into");
        boolean flag=false;
        Process process = null;
        DataOutputStream os = null;
        try
        {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0)
            {
               flag=true;
            } else
            {
               flag=false;
            }
        } catch (Exception e)
        {
            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: "
                    + e.getMessage());
             flag=false;
        } finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
                process.destroy();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        l.i("isAppHasRoot#return ->flag:%s",String.valueOf(flag));
        return flag;
    }



    /**
     * whether context is system application
     *
     * @param context
     * @return
     */
    public static boolean isSystemApplication(Context context) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context, context.getPackageName());
    }

    /**
     * whether packageName is system application
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isSystemApplication(Context context, String packageName) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context.getPackageManager(), packageName);
    }

    /**
     * whether packageName is system application
     *
     * @param packageManager
     * @param packageName
     * @return <ul>
     *         <li>if packageManager is null, return false</li>
     *         <li>if package name is null or is empty, return false</li>
     *         <li>if package name not exit, return false</li>
     *         <li>if package name exit, but not system app, return false</li>
     *         <li>else return true</li>
     *         </ul>
     */
    public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
        if (packageManager == null || packageName == null || packageName.length() == 0) {
            return false;
        }

        try {
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
