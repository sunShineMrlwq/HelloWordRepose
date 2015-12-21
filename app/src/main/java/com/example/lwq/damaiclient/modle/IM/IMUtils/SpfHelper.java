package com.example.lwq.damaiclient.modle.IM.IMUtils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lwq.damaiclient.R;

public class SpfHelper {
	private static SpfHelper sSpfHelper ;
	public SharedPreferences Account;
	public SharedPreferences.Editor editor;
	
	private static final String KEY_USER_USERNAME = "username";
	private static final String KEY_USER_PWD = "pwd";
	

	private SpfHelper(Context ct){
		Account = ct.getSharedPreferences(ct.getString(R.string.app_name), 0);
		editor = Account.edit();
	}
	public static SpfHelper getInstance(Context ct){
		if(sSpfHelper == null){
			sSpfHelper = new SpfHelper(ct);
		}
		return sSpfHelper;
	}
	
	public void clearUserInfo(){
		editor.putString(KEY_USER_USERNAME, "").commit();
		editor.putString(KEY_USER_PWD, "").commit();
	}

	public void saveUserInfo(String username,String pwd){//保存用户信息在本地
		editor.putString(KEY_USER_USERNAME, username).commit();
		editor.putString(KEY_USER_PWD, pwd).commit();
	}
	
	public boolean hasSignIn(){//看用户是否注册过
		return getMyUsername()!=null && getMyUsername().length()>0 && getMyPwd()!=null && getMyPwd().length()>0;
	}
	
	public String getMyUsername(){
		return Account.getString(KEY_USER_USERNAME, "");
	}
	public String getMyPwd(){
		return Account.getString(KEY_USER_PWD, "");
	}
}
