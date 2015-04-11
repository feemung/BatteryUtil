package com.e.laocow.batteryutil;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Settings {

	
	public static void setIsAllowRoot(Context context,boolean allow){
		SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();

             editor.putBoolean("IsAllowRoot", allow);

		editor.commit();
        Logger.getLogger(Settings.class).i("setIsAllowRoot ->allow:%s",String.valueOf(allow));

	}

	public static boolean getIsAllowRoot(Context context) {
		SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);

		boolean flag=  preferences.getBoolean("IsAllowRoot",false);
        Logger.getLogger(Settings.class).i("getIsAllowRoot ->allow:%s",String.valueOf(flag));
        return  flag;
	}

    public static void setAppHasRoot(Context context,boolean allow){
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("AppHasRoot", allow);

        editor.commit();
        Logger.getLogger(Settings.class).i("setAppHasRoot ->allow:%s",String.valueOf(allow));

    }

    public static boolean getAppHasRoot(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);

        boolean flag=  preferences.getBoolean("AppHasRoot",false);
        Logger.getLogger(Settings.class).i("getAppHasRoot ->allow:%s",String.valueOf(flag));
        return  flag;
    }

    public static void setRunFlag(Context context,boolean allow){
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("RunFlag", allow);

        editor.commit();
        Logger.getLogger(Settings.class).i("setAppHasRoot ->RunFlag:%s",String.valueOf(allow));

    }

    public static boolean getRunFlag(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);

        boolean flag=  preferences.getBoolean("RunFlag",true);
        Logger.getLogger(Settings.class).i("getRunFlag ->RunFlag:%s",String.valueOf(flag));
        return  flag;
    }
    public static void setAliveRunFlag(Context context,boolean AliveRunFlag){
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("AliveRunFlag",AliveRunFlag);

        editor.commit();
        Logger.getLogger(Settings.class).i("setAliveRunFlag ->AliveRunFlag:%s",String.valueOf(AliveRunFlag));

    }

    public static boolean getAliveRunFlag(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);

        boolean flag=  preferences.getBoolean("AliveRunFlag",true);
        Logger.getLogger(Settings.class).i("getAliveRunFlag ->AliveRunFlag:%s",String.valueOf(flag));
        return  flag;
    }

    public static void setKeyguardRunFlag(Context context,boolean KeyguardRunFlag){
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("KeyguardRunFlag",KeyguardRunFlag);

        editor.commit();
        Logger.getLogger(Settings.class).i("setKeyguardRunFlag ->KeyguardRunFlag:%s",String.valueOf(KeyguardRunFlag));

    }

    public static boolean getKeyguardRunFlag(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);

        boolean flag=  preferences.getBoolean("KeyguardRunFlag",false);
        Logger.getLogger(Settings.class).i("getKeyguardRunFlag ->KeyguardRunFlag:%s",String.valueOf(flag));
        return  flag;
    }




}
