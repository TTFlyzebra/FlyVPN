package android.octopu.wifi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.octopu.wifi.bean.WifiDeviceBean;
import android.os.Handler;
import android.os.HandlerThread;

import com.flyzebra.utils.FlyLog;

import java.util.ArrayList;
import java.util.List;

public class WifiDeviceSQLite extends SQLiteOpenHelper {
    private static final HandlerThread mWriteTask = new HandlerThread("sqlite-write");

    static {
        mWriteTask.start();
    }

    private static final Handler mWriteHandler = new Handler(mWriteTask.getLooper());
    private static final int VERSION = 1;
    private static final String DB_NAME = "octopu_wifi.db";
    private static final String WIFI_TABLE_PUB = "wifidevice_pub";
    private static final String CREATE_WIFI_TABLE_PUB = "create table wifidevice_pub(" +
            "id integer primary key autoincrement," +
            "wifiDeviceId varchar(20) unique," +
            "wifiPassword varchar(255) default ''," +
            "wifiAuthType varchar(20) default ''," +
            "wifiName varchar(255) default ''," +
            "wifiStatus integer default 0," +
            "wifiCreateTime varchar(20) default ''," +
            "wifiUpdateTime varchar(20) default ''," +
            "userId varchar(20)," +
            "longitude real default '0'," +
            "latitude real default '0'," +
            "remarks varchar(1024) default '');";
    private static final String WIFI_TABLE_PRI = "wifidevice_pri";
    private static final String CREATE_WIFI_TABLE_PRI = "create table wifidevice_pri(" +
            "id integer primary key autoincrement," +
            "wifiDeviceId varchar(20) unique," +
            "wifiPassword varchar(255) default ''," +
            "wifiAuthType varchar(20) default ''," +
            "wifiName varchar(255) default ''," +
            "wifiStatus integer default 0," +
            "wifiCreateTime varchar(20) default ''," +
            "wifiUpdateTime varchar(20) default ''," +
            "userId varchar(20)," +
            "longitude real default '0'," +
            "latitude real default '0'," +
            "remarks varchar(1024) default '');";

    public WifiDeviceSQLite(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FlyLog.e("onCreate");
        db.execSQL(CREATE_WIFI_TABLE_PRI);
        db.execSQL(CREATE_WIFI_TABLE_PUB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FlyLog.e("onUpgrade");
    }

    @Override
    public synchronized void close() {
        FlyLog.e("close");
        mWriteHandler.removeCallbacksAndMessages(null);
        super.close();
    }

    public void updataPubWifiDevice(final WifiDeviceBean wifiDeviceBean) {
        mWriteHandler.post(new Runnable() {
            @Override
            public void run() {
                updataWifiDevice(WIFI_TABLE_PUB, wifiDeviceBean);
            }
        });
    }

    public void updataPriWifiDevice(final WifiDeviceBean wifiDeviceBean) {
        mWriteHandler.post(new Runnable() {
            @Override
            public void run() {
                updataWifiDevice(WIFI_TABLE_PRI, wifiDeviceBean);
            }
        });
    }

    public void updataPubWifiDevices(final List<WifiDeviceBean> wifiDeviceBeans) {
        mWriteHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WifiDeviceBean wifiDeviceBean : wifiDeviceBeans) {
                    updataWifiDevice(WIFI_TABLE_PUB, wifiDeviceBean);
                }
            }
        });

    }

    public void updataPriWifiDevices(final List<WifiDeviceBean> wifiDeviceBeans) {
        mWriteHandler.post(new Runnable() {
            @Override
            public void run() {
                for (WifiDeviceBean wifiDeviceBean : wifiDeviceBeans) {
                    updataWifiDevice(WIFI_TABLE_PRI, wifiDeviceBean);
                }
            }
        });
    }

    private void updataWifiDevice(String table, WifiDeviceBean wifiDeviceBean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("wifiDeviceId", wifiDeviceBean.wifiDeviceId);
        value.put("wifiPassword", wifiDeviceBean.wifiPassword);
        value.put("wifiAuthType", wifiDeviceBean.wifiAuthType);
        value.put("wifiName", wifiDeviceBean.wifiName);
        value.put("wifiStatus", wifiDeviceBean.wifiStatus);
        value.put("wifiCreateTime", wifiDeviceBean.wifiCreateTime);
        value.put("wifiUpdateTime", wifiDeviceBean.wifiUpdateTime);
        value.put("userId", wifiDeviceBean.userId);
        value.put("longitude", wifiDeviceBean.longitude);
        value.put("latitude", wifiDeviceBean.latitude);
        value.put("remarks", wifiDeviceBean.remarks);
        if (getWifiDevicePassword(table, wifiDeviceBean.wifiDeviceId) != null) {
            long ret = db.update(table, value,
                    "wifiDeviceId=?", new String[]{wifiDeviceBean.wifiDeviceId});
            if (ret <= 0) {
                FlyLog.w("don't update [%s]", wifiDeviceBean);
            }else{
                FlyLog.v("update table=%s,num=[%d]",table, ret);
            }
        } else {
            long ret = db.insert(table, null, value);
            if (ret == -1) {
                FlyLog.w("insert failed [%s]", wifiDeviceBean);
            }else{
                FlyLog.v("insert table=%s,id=[%d]", table,ret);
            }
        }
    }

    public List<WifiDeviceBean> getWifiDevices(String table) {
        List<WifiDeviceBean> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(table, null, null, null, null, null, null);
        while (c.moveToNext()) {
            WifiDeviceBean wifiDeviceBean = new WifiDeviceBean();
            wifiDeviceBean.id = (c.getInt(c.getColumnIndex("id")));
            wifiDeviceBean.wifiDeviceId = (c.getString(c.getColumnIndex("wifiDeviceId")));
            wifiDeviceBean.wifiPassword = (c.getString(c.getColumnIndex("wifiPassword")));
            wifiDeviceBean.wifiAuthType = (c.getString(c.getColumnIndex("wifiAuthType")));
            wifiDeviceBean.wifiName = (c.getString(c.getColumnIndex("wifiName")));
            wifiDeviceBean.wifiStatus = (c.getInt(c.getColumnIndex("wifiStatus")));
            wifiDeviceBean.wifiCreateTime = (c.getString(c.getColumnIndex("wifiCreateTime")));
            wifiDeviceBean.wifiUpdateTime = (c.getString(c.getColumnIndex("wifiUpdateTime")));
            wifiDeviceBean.userId = (c.getString(c.getColumnIndex("userId")));
            wifiDeviceBean.longitude = (c.getDouble(c.getColumnIndex("longitude")));
            wifiDeviceBean.latitude = (c.getDouble(c.getColumnIndex("latitude")));
            wifiDeviceBean.remarks = (c.getString(c.getColumnIndex("remarks")));
            list.add(wifiDeviceBean);
        }
        c.close();
        return list;
    }

    private String getWifiDevicePassword(String table, String wifiDeviceId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(table, null,
                "wifiDeviceId=?", new String[]{wifiDeviceId},
                null, null, null);
        if (c.moveToNext()) {
            return (c.getString(c.getColumnIndex("wifiPassword")));
        }
        return null;
    }

    private int deleteWifiDevice(String table, String wifiDeviceId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(table, "wifiDeviceId=?", new String[]{wifiDeviceId});
    }



}
