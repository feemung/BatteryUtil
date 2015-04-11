package com.e.laocow.batteryutil;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;


public class MainActivity extends ActionBarActivity {
    private static Handler handler;

    private boolean hasPhoneRoot;
    private TextView rootTV;
    private Switch runFlagSwi;
    private Switch aliveRunFlagSwi;
    private boolean isAllowRoot;
    private TextView countTV;
    private Logger l=Logger.getLogger(MainActivity.class);
    private FrameLayout dataFrameLayout;
    public LinearLayout mainLL;
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        l.d("onCreate into");
        setContentView(R.layout.activity_main);
        //Settings.setIsAllowRoot(this,false);
        mainActivity=this;
        StateManager.getInstance().setContext(this);
        init();
        check();

        }
    private void init(){
        rootTV =(TextView)findViewById(R.id.getRootTV);
        runFlagSwi=(Switch)findViewById(R.id.runFlagSwi);
        runFlagSwi.setChecked(Settings.getRunFlag(this));
        runFlagSwi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.setRunFlag(MainActivity.this,isChecked);
            }
        });
        aliveRunFlagSwi=(Switch)findViewById(R.id.aliveRunFlagSwi);
        aliveRunFlagSwi.setChecked(Settings.getAliveRunFlag(this));
        aliveRunFlagSwi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.setAliveRunFlag(MainActivity.this, isChecked);
            }
        });
        countTV=(TextView)findViewById(R.id.countTV);
        dataFrameLayout=(FrameLayout)findViewById(R.id.dataFrameLayout);
        int count=DbManager.instance().getShutdownCount();
        countTV.setText(String.valueOf(count));
        mainLL=(LinearLayout)findViewById(R.id.main_mainLL);
    }
    private void check(){
        l.d("check into");
        hasPhoneRoot=Shutdown.isRoot();
        l.i("check -> hasPhoneRoot:%s",String.valueOf(hasPhoneRoot));
        if(!hasPhoneRoot){
            dialog("警告","您的手机还未获得root权限,请获得获取root权限后再使用本软件。");
            return;
        }

        if(!Settings.getIsAllowRoot(this)) {
            dialogGetRoot();
        }else{
            if(Settings.getAppHasRoot(this)) {
                rootTV.setText("本程序已经获得关机权限，可以正常使用。。");

            }
        }
        boolean allowRoot=Settings.getIsAllowRoot(this);
        l.d("settings->allowRoot:%s",String.valueOf(allowRoot));

    }
    private void checkRoot(){
            l.d("check into");
            boolean appHasRoot=Shutdown.isAppHasRoot();
            if(appHasRoot){
                if(!Settings.getAppHasRoot(this)) {
                    dialog("提示", "本程序已经获得关机权限，可以正常使用");
                    Settings.setAppHasRoot(this,true);
                }
                rootTV.setText("本程序已经获得关机权限，可以正常使用。。");
            }else{

                    dialog("警告", "获取关机权限失败！");
                    Settings.setAppHasRoot(this, false);

                rootTV.setText("获取关机权限失败！！");
            }


    }

    private void dialogGetRoot() {
        l.d("dialogGetRoot into");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("使用系统低电量警告时自动关机功能，需要获取root权限");
        builder.setTitle("提示");
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
             public void onClick(DialogInterface dialog, int which) {
                isAllowRoot =true;
                Settings.setIsAllowRoot(MainActivity.this,true);

                dialog.dismiss();

                checkRoot();
        }
        });

            builder.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isAllowRoot=false;
                    Settings.setIsAllowRoot(MainActivity.this,false);
                    rootTV.setText("由于您不同意本程序获得关机权限，所以不能使用");

                    dialog.dismiss();
                }

            });

        builder.create().show();
    }


    private void dialog(String title,String text) {
        l.d("dialog init");
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(text);
        builder.setTitle(title);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    public void setRunFlagBut(View view){

    }
    public void shutdown(View view){
        Intent intent=new Intent();
        intent.setAction("feemung.com.test");
        this.sendBroadcast(intent);


    }
    public void getPhoneRoot(View view){
        if(!Settings.getIsAllowRoot(this)){
            Settings.setAppHasRoot(this,false);
            this.dialogGetRoot();
        }else{
            Settings.setAppHasRoot(this,false);
            checkRoot();
        }
    }
    public void deletedAllBut(View view){
       boolean flag= DbManager.instance().deleteDatabase(this);
        l.d("deletedAllBut#flag:%s",String.valueOf(flag));
        dialog("提示", "删除历史数据成功！");
        countTV.setText("0");

    }
    public void seeAllBut(View view){
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        ShutdownDataFragment shutdownDataFragment= new ShutdownDataFragment();
        fragmentTransaction.replace(R.id.dataFrameLayout,shutdownDataFragment);
        fragmentTransaction.commit();
        mainLL.setVisibility(View.GONE);

    }
    public void about(View view){
        Intent intent=new Intent(this,AboutActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.exit(0);
        //finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
