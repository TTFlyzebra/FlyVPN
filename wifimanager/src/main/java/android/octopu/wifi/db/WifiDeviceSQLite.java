package android.octopu.wifi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.octopu.wifi.bean.WifiDeviceBean;

import java.util.ArrayList;
import java.util.List;

public class WifiDeviceSQLite extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "octopu_wifi.db";
    private static final String WIFI_TABLE = "wifidevice";
    private static final String CREATE_WIFI_TABLE = "create table wifidevice(" +
            "id integer primary key," +
            "wifiDeviceId varchar(255) default ''," +
            "wifiPassword varchar(255) default ''," +
            "wifiAuthType varchar(255) default ''," +
            "wifiName varchar(255) default ''," +
            "wifiStatus integer default 0," +
            "wifiCreateTime varchar(20) default ''," +
            "wifiUpdateTime varchar(20) default ''," +
            "userId varchar(255)," +
            "longitude real default '0'," +
            "latitude real default '0'," +
            "remarks varchar(1024) default '');";
    private SQLiteDatabase db;

    public WifiDeviceSQLite(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_WIFI_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db = db;
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public boolean insertWifiDevice(WifiDeviceBean wifiDeviceBean) {
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
        db.insert(WIFI_TABLE, null, value);
        return true;
    }

    public List<WifiDeviceBean> getWifiDevices(String userToken) {
        List<WifiDeviceBean> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from pople where userToken=?";
        Cursor c = db.rawQuery(sql, new String[]{userToken});
        while (c.moveToNext()) {
            WifiDeviceBean wifiDeviceBean = new WifiDeviceBean();
            wifiDeviceBean.id =(c.getInt(c.getColumnIndex("id")));
            wifiDeviceBean.wifiDeviceId =(c.getString(c.getColumnIndex("wifiDeviceId")));
            wifiDeviceBean.wifiPassword =(c.getString(c.getColumnIndex("wifiPassword")));
            wifiDeviceBean.wifiAuthType =(c.getString(c.getColumnIndex("wifiAuthType")));
            wifiDeviceBean.wifiName =(c.getString(c.getColumnIndex("wifiName")));
            wifiDeviceBean.wifiStatus =(c.getInt(c.getColumnIndex("wifiStatus")));
            wifiDeviceBean.wifiCreateTime =(c.getString(c.getColumnIndex("wifiCreateTime")));
            wifiDeviceBean.wifiUpdateTime =(c.getString(c.getColumnIndex("wifiUpdateTime")));
            wifiDeviceBean.userId =(c.getString(c.getColumnIndex("userId")));
            wifiDeviceBean.longitude =(c.getDouble(c.getColumnIndex("longitude")));
            wifiDeviceBean.latitude =(c.getDouble(c.getColumnIndex("latitude")));
            wifiDeviceBean.remarks =(c.getString(c.getColumnIndex("remarks")));
            list.add(wifiDeviceBean);
        }
        c.close();
        return list;
    }

    public WifiDeviceBean findWifiDevice(WifiDeviceBean WifiDeviceBean) {
        WifiDeviceBean wifiDeviceBean = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(WIFI_TABLE,
                null,
                "wifiDeviceId=? and wifiName=?",
                new String[]{WifiDeviceBean.wifiDeviceId, WifiDeviceBean.wifiName},
                null,
                null, null);
        if (c.moveToNext()) {
            wifiDeviceBean = new WifiDeviceBean();
            wifiDeviceBean.id =(c.getInt(c.getColumnIndex("id")));
            wifiDeviceBean.wifiDeviceId =(c.getString(c.getColumnIndex("wifiDeviceId")));
            wifiDeviceBean.wifiPassword =(c.getString(c.getColumnIndex("wifiPassword")));
            wifiDeviceBean.wifiAuthType =(c.getString(c.getColumnIndex("wifiAuthType")));
            wifiDeviceBean.wifiName =(c.getString(c.getColumnIndex("wifiName")));
            wifiDeviceBean.wifiStatus =(c.getInt(c.getColumnIndex("wifiStatus")));
            wifiDeviceBean.wifiCreateTime =(c.getString(c.getColumnIndex("wifiCreateTime")));
            wifiDeviceBean.wifiUpdateTime =(c.getString(c.getColumnIndex("wifiUpdateTime")));
            wifiDeviceBean.userId =(c.getString(c.getColumnIndex("userId")));
            wifiDeviceBean.longitude =(c.getDouble(c.getColumnIndex("longitude")));
            wifiDeviceBean.latitude =(c.getDouble(c.getColumnIndex("latitude")));
            wifiDeviceBean.remarks =(c.getString(c.getColumnIndex("remarks")));
        }
        return wifiDeviceBean;
    }

    public int deleteWifiDevice(WifiDeviceBean wifiDeviceBean) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(WIFI_TABLE, "wifiDeviceId=?", new String[]{wifiDeviceBean.wifiDeviceId});
    }

    public int updateWifiDevice(WifiDeviceBean wifiDeviceBean, String Clause, String[] s) {
        ContentValues value = new ContentValues();
        value.put("wifiDeviceId",wifiDeviceBean.wifiDeviceId);
        value.put("wifiPassword",wifiDeviceBean.wifiPassword);
        SQLiteDatabase db = getWritableDatabase();
        return db.update(WIFI_TABLE, value, Clause, s);
    }

}
