package com.e.laocow.batteryutil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by laocow on 2015/4/9.
 */
public class DbManager extends SQLiteOpenHelper{
    private static final int DB_VERSION = 4;
    private static final String TABLE_SESSION_MSG = "session_msg";

    private Logger logger = Logger.getLogger(DbManager.class);

    private static DbManager inst;

    public static synchronized DbManager instance(Context ctx) {
        if (inst == null) {
            inst = new DbManager(ctx, "Battt1.db", null, DB_VERSION);
        }

        return inst;
    }

    private DbManager(Context context, String name, SQLiteDatabase.CursorFactory factory,
                       int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }
    private void createTable(SQLiteDatabase db) {
        logger.d("db#createTable");
        String sql = "create table if not exists session_msg ("
                + "id int auto increment primary key,"
                +"time varchar(50) not null,"
                +"batteryScale int default 0" + ")";

        logger.d("db#create session_msg table -> sql:%s", sql);

        db.execSQL(sql);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        logger.d("db#db onCreate");

        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ssSSS");
    public static String getDate(){
        return df.format(new Date());
    }
    public synchronized void save(int batteryScale) {
        logger.d("db#batteryScale:%d", batteryScale);

        SQLiteDatabase db = getWritableDatabase();
        if (db == null) {
            return;
        }

        String sql = "insert into session_msg (time,batteryScale) values "
                + "(?,?)";
        String time=getDate();
        logger.d("db#save -> sql:%s,time:%s,batteryScale:%d", sql,time,batteryScale);

        // todo eric sql injection
       // db.execSQL(sql,new Object[]{time,batteryScale});
        ContentValues values=new ContentValues();
        values.put("time",time);
        values.put("batteryScale",batteryScale);
        db.insert("session_msg",null,values);
    }
    public synchronized int getShutdownCount(){
        logger.d("db#shutdownCount");
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor=db.rawQuery("Select time(*) from session_msg;", null);
        int count=cursor.getCount();
        cursor.close();
        logger.d("db#shutdownCount:%d",count);
        return count;
    }

    public Map<String,Integer> getAll() {
        Map<String,Integer> map=new TreeMap<>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("session_msg", null, null, null, null, null, null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                logger.d("db#getAll cursor:%s","xuhuan");
                int timeColumn=cursor.getColumnIndex("time");
                String time=cursor.getString(timeColumn);
                int batteryScaleColumn=cursor.getColumnIndex("batteryScale");
                int batteryScale=cursor.getInt(batteryScaleColumn);
                map.put(time,batteryScale);
                logger.d("db#getAll cursor->time:%s,batteryScale:%d",time,batteryScale);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return map;
    }
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase("Battt1.db");
    }
}
