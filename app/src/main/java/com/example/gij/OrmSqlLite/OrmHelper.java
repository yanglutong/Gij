package com.example.gij.OrmSqlLite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.gij.OrmSqlLite.Bean.CellBean;
import com.example.gij.OrmSqlLite.Bean.CellBeanGSM;
import com.example.gij.OrmSqlLite.Bean.CellBeanNr;
import com.example.gij.OrmSqlLite.Bean.DevicePdBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;


/**
 * Created by Administrator on 2018/3/22 0022.
 */

public class OrmHelper extends OrmLiteSqliteOpenHelper {
    public static final String DB_NAME = "16078.db";
    private static final int DB_VERSION = 1;
 public static    ConnectionSource connectionSources;
    public static  SQLiteDatabase databases;
    //实现一个单例返回DbHelper实例
    private static OrmHelper helper;

    public static OrmHelper getHelper(Context context) {
        if (helper == null) {
            helper = new OrmHelper(context);
        }
        return helper;
    }
    public OrmHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        connectionSources=connectionSource;
        databases=database;
        //建表,和Gson类似，第二个参数即是业务实体类
        try {
            try {
//
                TableUtils.createTable(connectionSource, CellBean.class);//小区工模的数据类
                TableUtils.createTable(connectionSource, CellBeanNr.class);//小区工模的数据类
                TableUtils.createTable(connectionSource, CellBeanGSM.class);//小区工模的数据类
                TableUtils.createTable(connectionSource, DevicePdBean.class);//设备的数据类
//


            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
