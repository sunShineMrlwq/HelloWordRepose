package com.example.lwq.damaiclient.app;

import java.util.Map;
import java.util.Stack;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;

import com.activeandroid.ActiveAndroid;
import com.example.lwq.damaiclient.modle.IM.IMApp.AnSocialManger;

/**
 * Application类
 * @author lwq
 * */
public class MyApplication extends Application {
	private static Stack<Activity> activityStack;
	private static MyApplication singleton;
	public static Map<String, Long> map;
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		ActiveAndroid.initialize(this);
		AnSocialManger.getInstance(this);//初始化箭扣
	}


	// 单例模式
	public static MyApplication getInstance() {
		return singleton;
	}

	/**
	 * @param activity
	 * add Activity 添加Activity到栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		if(!activityStack.contains(activity)){
			activityStack.add(activity);
		}
	}

	/**
	 *
	 * get current Activity 获取当前Activity（栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * @param activity
	 * 删除activity
	 */
	public void reMoveActivity(Activity activity){
		activityStack.remove(activity);		
	}

	/**
	 * 结束当前Activity（栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {

		if (activity != null) {
			if(activityStack.contains(activity)){
				activityStack.remove(activity);
				activity.finish();
				activity = null;
			}
		}

	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		try {
			for (Activity activity : activityStack) {
				if (null!=cls && activity.getClass().equals(cls)) {
					finishActivity(activity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit() {
		try {
			finishAllActivity();
		} catch (Exception e) {
		}
	}

	@Override
	public void onTerminate() {
		AppExit();
		super.onTerminate();
	}
}
