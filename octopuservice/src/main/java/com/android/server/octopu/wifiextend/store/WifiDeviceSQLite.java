package com.android.server.octopu.wifiextend.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.octopu.FlyLog;

import com.android.server.octopu.wifiextend.bean.WifiDeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @hide
 */
public class WifiDeviceSQLite extends SQLiteOpenHelper {
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
        super.close();
    }

    public void updataPubWifiDevice(final WifiDeviceBean wifiDeviceBean) {
        updataWifiDevice(WIFI_TABLE_PUB, wifiDeviceBean);
    }

    public void updataPriWifiDevice(final WifiDeviceBean wifiDeviceBean) {
        updataWifiDevice(WIFI_TABLE_PRI, wifiDeviceBean);
    }

    public void updataPubWifiDevices(final List<WifiDeviceBean> wifiDevices) {
        for (WifiDeviceBean wifiDeviceBean : wifiDevices) {
            updataWifiDevice(WIFI_TABLE_PUB, wifiDeviceBean);
        }

    }

    public void updataPriWifiDevices(final List<WifiDeviceBean> wifiDevices) {
        for (WifiDeviceBean wifiDeviceBean : wifiDevices) {
            updataWifiDevice(WIFI_TABLE_PRI, wifiDeviceBean);
        }
    }

    public List<WifiDeviceBean> getPubWifiDevices() {
        return getWifiDevices(WIFI_TABLE_PUB);
    }

    public List<WifiDeviceBean> getPriWifiDevice() {
        return getWifiDevices(WIFI_TABLE_PRI);
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
        WifiDeviceBean findwifiDeviceBean = getWifiDevice(table, wifiDeviceBean);
        if (findwifiDeviceBean != null) {
            if (!findwifiDeviceBean.wifiPassword.equals(wifiDeviceBean.wifiPassword)
                    || !findwifiDeviceBean.wifiName.equals(wifiDeviceBean.wifiName)) {
                long ret = db.update(table, value, "wifiDeviceId=?", new String[]{wifiDeviceBean.wifiDeviceId});
                if (ret <= 0) {
                    FlyLog.w("update failed [%s]", wifiDeviceBean);
                } else {
                    FlyLog.v("update table=%s,result=[%d]", table, ret);
                }
            } else {
                FlyLog.v("wifidevice is already insert and no change.");
            }
        } else {
            long ret = db.insert(table, null, value);
            if (ret == -1) {
                FlyLog.w("insert failed [%s]", wifiDeviceBean);
            } else {
                FlyLog.v("insert table=%s,id=[%d]", table, ret);
            }
        }
    }

    private List<WifiDeviceBean> getWifiDevices(String table) {
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

    private WifiDeviceBean getWifiDevice(String table, WifiDeviceBean mWifiDeviceBean) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(table, null,
                "wifiDeviceId=?",
                new String[]{mWifiDeviceBean.wifiDeviceId},
                null, null, null);
        if (c.moveToNext()) {
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
            return wifiDeviceBean;
        }
        return null;
    }

    private int deleteWifiDevice(String table, String wifiDeviceId) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(table, "wifiDeviceId=?", new String[]{wifiDeviceId});
    }


}
