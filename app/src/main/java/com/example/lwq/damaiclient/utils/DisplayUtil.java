package com.example.lwq.damaiclient.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

/**
 * Created by yangshuai in the 15:40 of 2015.11.26 .
 * dp, sp, 与 px 互相转换的工具类
 */
public class DisplayUtil {

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    //获取dp
    public static float getScreenDensity(Context context,int i) {
        int dp=0;
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        dp=(int) (i*dm.density);
        return dp;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /***
     * 关闭软键盘
     * */
    public static void colseSoftInputFromWindow(Context context){
//        try {
//            /**隐藏软键盘**/
//            View view = ((Activity) context).getWindow().peekDecorView();
//            if (view != null) {
//                InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            }
//        } catch (Exception e) {
//        }
    }
}
