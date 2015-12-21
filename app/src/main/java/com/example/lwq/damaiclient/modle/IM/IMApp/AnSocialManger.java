package com.example.lwq.damaiclient.modle.IM.IMApp;

import android.content.Context;

import com.arrownock.exception.ArrownockException;
import com.arrownock.social.AnSocial;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.app.MyApplication;

/**
 * Created by lwq on 2015/12/10.
 */
public class AnSocialManger {
    private static AnSocialManger sAnSocialManger;
    public AnSocial anSocial;
    public Context context;
    public AnSocialManger(Context ct){
        try {
            anSocial=new AnSocial(ct,ct.getString(R.string.app_key));
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }
    /**
     *创建AnSocial单例实例，并做初始化配置
     * */
    public static AnSocialManger getInstance(Context ct){
        if(sAnSocialManger==null){
            sAnSocialManger = new AnSocialManger(ct);
        }
        return sAnSocialManger;
    }
}
