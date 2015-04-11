package com.e.laocow.batteryutil;

import android.content.Context;

/**
 * Created by laocow on 2015/4/11.
 */
public class StateManager {
    private static StateManager instance;
    private Context context;
    private int batteryScale=0;
    private StateManager(){}
    public static StateManager getInstance(){
        if(instance==null){
        instance=new StateManager();
        }
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getBatteryScale() {
        return batteryScale;
    }

    public void setBatteryScale(int batteryScale) {
        this.batteryScale = batteryScale;
    }
}
