package com.example.lwq.damaiclient.modle.IM.bean;

import com.activeandroid.query.Select;

/**
 * Created by lwq on 2015/12/15.
 */
public class UserLocal {
    /**
     * 保存用户本地信息（先判断用户信息是否有改变）
     * */
    public static boolean same(User user){
        User userExisit = new Select().from(User.class)
                .where("clientId = ? ", user.clientId).executeSingle();
        if( (userExisit!=null) && userExisit.userName!=null && userExisit.userId!=null && (userExisit.userId.equals(user.userId)) && (userExisit.userName.equals(user.userName))){
            return (userExisit.userPhotoUrl==null && user.userPhotoUrl==null);
        }else{
            return false;
        }

    }
    public static User update(User user) {
        User userExisit = new Select().from(User.class)
                .where("clientId = ? ", user.clientId).executeSingle();

        // 不存在
        if (userExisit == null) {
            userExisit = user;
        }else{
            if(user.userId!=null)
                userExisit.userId = user.userId;
            if(user.clientId!=null)
                userExisit.clientId = user.clientId;
            if(user.userName!=null)
                userExisit.userName = user.userName;
            if(user.userPhotoUrl!=null)
                userExisit.userPhotoUrl = user.userPhotoUrl;
        }
        userExisit.save();
        return userExisit;
    }
}
