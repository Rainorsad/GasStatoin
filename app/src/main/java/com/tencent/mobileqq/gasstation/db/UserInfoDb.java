package com.tencent.mobileqq.gasstation.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.tencent.mobileqq.gasstation.bean.UserInfoBean;

import org.kymjs.kjframe.KJDB;

import java.util.List;

/**
 * Created by Zhangchen on 2018/3/5.
 */

public class UserInfoDb {
    private KJDB db;

    public UserInfoDb(Context context) {
        db = KJDB.create(context,"userdb.db");
//        db = KJDB.create(context,"userdb.db",false,2,listener); //数据库一旦增添字段在listener中修改
    }

    //储存数据
    public boolean saveData(UserInfoBean userInfoBean){
        if (userInfoBean != null){
            db.save(userInfoBean);
            return true;
        }
        return false;
    }

    //获取数据
    public <T> List<T> getData(Class<T> clazz) {
        return db.findAll(clazz);
    }

    //查找数据
    //如果w字符串过大List<HBChatInfo> listco = mdb.findAllByWhere(HBChatInfo.class,"userid='" + UserId+ "'");
    public <T>List<T>  getDataByWhere(Class<T> tClass,String w){
        return db.findAllByWhere(tClass,w);
    }

    public <T> void deleteUserInfo(Class<T> clazz, String strWhere) {
        db.deleteByWhere(clazz, strWhere);
    }

    //更新数据
    public boolean upData(UserInfoBean userInfoBean,String w){
        if (userInfoBean != null){
            db.update(userInfoBean , w);
            return true;
        }
        return false;
    }

    KJDB.DbUpdateListener listener = new KJDB.DbUpdateListener() {
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try {
                if (i < i1) {
                    String tab = "user_info_sql";

                    String sql = "ALTER TABLE " + tab
                            + " ADD COLUMN userid VARCHAR(50)";
                    sqLiteDatabase.execSQL(sql);
                }
            } catch (java.lang.ExceptionInInitializerError e) {
                Log.d("asdap", e.toString());
            } catch (SQLiteException e) {
                Log.d("asdap SQLiteException", e.toString());
            }
        }
    };
}
