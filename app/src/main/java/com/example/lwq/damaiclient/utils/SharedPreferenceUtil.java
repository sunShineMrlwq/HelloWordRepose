package com.example.lwq.damaiclient.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.lwq.damaiclient.app.MyApplication;
import com.example.lwq.damaiclient.bean.User;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * @author lwq
 * */
public class SharedPreferenceUtil {
    private static SharedPreferences sharedPreferences;
    private static Gson gson = new Gson();
    public static SharedPreferenceUtil getInstance(String name) {
        if (sharedPreferences == null) {
            sharedPreferences = MyApplication.getInstance().
                    getSharedPreferences(name, Application.MODE_PRIVATE);
        }
        return new SharedPreferenceUtil();
    }
    public static void saveshuxingData(String file_key, String value) {
        sharedPreferences.edit().putString(file_key, value).commit();
    }
    public static String getshuxingData(String file_key) {
       return sharedPreferences.getString(file_key, "");
    }
    /**
     * @param file_key 保存数据()
     * @param o
     */
    public static void saveData(String file_key, Class<T> o) {
        String json = gson.toJson(o);
        sharedPreferences.edit().putString(file_key, json).commit();
    }

    /**
     * @param file_key
     * @param o    获取数据
     */
    public static Objects getData(String file_key, Objects o) {
        try {
            Objects objects  = null;
            String reslut = sharedPreferences.getString("file_key", null);
            if (reslut != null) {
                objects = gson.fromJson(reslut, o.getClass());
            }
            return objects;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除数据
     *
     */
    public static void detleData() {
        try {
            if (null != sharedPreferences) {
                sharedPreferences.edit().clear().commit();
            }
        } catch (Exception e) {
        }
    }
}