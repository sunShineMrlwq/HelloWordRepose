package com.example.lwq.damaiclient.modle.IM.IMApp;

/**
 * Created by lwq on 2015/12/15.
 */
public class WebInterface {
    /**
     * 初始注册
     * */
    public static String register(){
        return "users/create.json";
    }
    /**
     * 用户登陆
     * */
    public static String login(){return "users/auth.json";}
    /**
     * 查找用户
     * */
    public static String fetchUserInfoByClientId(){return "users/query.json";}
    /**
     * 搜索好友
     * */
    public static String searchRemoteUser(){return "users/search.json";}
    /**
     * 加好友
     * */
    public static String addFriendRemote(){return "friends/add.json";}
    /**
     * 获取好友列表
     * */
    public static String fetchMyRemoteFriend(){return "friends/list.json";}
    /**
     * 获取好友请求列表
     * */
    public static String fetchFriendRequest(){return "friends/requests/list.json";}

    /**
     *  发送好友请求
     * */
    public static String sendFriendRequest(){return "friends/requests/send.json";}
    /**
     *  接受好友请求
     * */
    public static String approveFriendRequest(){return "friends/requests/approve.json";}
    /**
     *  拒绝好友请求
     * */
    public static String rejectFriendRequest(){return "friends/requests/reject.json";}

}
