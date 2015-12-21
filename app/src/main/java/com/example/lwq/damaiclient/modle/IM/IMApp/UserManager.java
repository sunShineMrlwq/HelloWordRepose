package com.example.lwq.damaiclient.modle.IM.IMApp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.arrownock.exception.ArrownockException;
import com.arrownock.social.AnSocial;
import com.arrownock.social.AnSocialMethod;
import com.arrownock.social.IAnSocialCallback;
import com.example.lwq.damaiclient.modle.IM.Activity.ChatMessageActivity;
import com.example.lwq.damaiclient.modle.IM.IMUtils.Constant;
import com.example.lwq.damaiclient.modle.IM.bean.FriendRequest;
import com.example.lwq.damaiclient.modle.IM.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by lwq on 2015/12/10.
 */
public class UserManager extends Observable  {
    public static UserManager sUserManager;
    public AnSocial anSocial;
    private Handler handler;
    private Context context;
    public User currentUser;

    public enum UpdateType {Friend,User}

    public UserManager(Context ct) {
        this.context = ct;
        anSocial = AnSocialManger.getInstance(ct).anSocial;
        handler = new Handler();
    }

    public static UserManager getInstance(Context ct) {
        if (sUserManager == null) {
            sUserManager = new UserManager(ct);
        }
        return sUserManager;
    }
    public void setCurrentUser(User user){
        currentUser = user;
    }
    public User getCurrentUser(){
        return currentUser;
    }
    /*
    * 用户登陆
    */
    public void login(final String username,final String pwd,final IAnSocialCallback lsr){
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", pwd);
        try {
            anSocial.sendRequest(WebInterface.login(), AnSocialMethod.POST, params, new IAnSocialCallback(){
                @Override
                public void onFailure(JSONObject arg0) {
                    if(lsr!=null){
                        lsr.onFailure(arg0);
                    }
                }
                @Override
                public void onSuccess(final JSONObject jsonObject) {
                    try {
                        JSONObject userJson = jsonObject.getJSONObject("response").getJSONObject("user");
                        Log.i("UserManager登陆", (userJson.toString()));
                        User user = new User(userJson);
                        saveUser(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(lsr!=null){//登陆成功
                        lsr.onSuccess(jsonObject);
                    }
                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }
    /*
     * 用户注册
     * 使用指定的username, password, password_confirmation或者ext_user_id创建一个User.
     * 如果创建成功, Server端会为该User生成一个唯一标识(即id)
     * 参数说明： enable_im是否从IM Server获取clientId, 如果想要使用anIM功能, 将该字段设置为true,
     * 稍后根据返回的clientId进行anIM的注册. 默认值: false
     */
    public void signUp(final String userName, final String passWord, final IAnSocialCallback lsr) {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("username", userName);
            params.put("password", passWord);
            params.put("password_confirmation", passWord);
            params.put("enable_im", true);
            anSocial.sendRequest(WebInterface.register(), AnSocialMethod.POST, params, new IAnSocialCallback() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        JSONObject userJson = jsonObject.getJSONObject("response").getJSONObject("user");
                        Log.i("UserManager注册成功", (userJson.toString()));
                        User user = new User(userJson);
                        saveUser(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(lsr!=null){//注册成功
                        lsr.onSuccess(jsonObject);
                    }
                }
                @Override
                public void onFailure(JSONObject jsonObject) {//注册失败
                    if(lsr!=null){
                        lsr.onFailure(jsonObject);
                    }
                    Toast.makeText(context, ("注册失败"), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }
    /***
     * 通过用户clientId获取用户的信息
     * **/
    public void fetchUserInfoByClientId(final String clientId){
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("clientId", clientId);
        try {
            anSocial.sendRequest(WebInterface.fetchUserInfoByClientId(), AnSocialMethod.GET, params, new IAnSocialCallback(){
                @Override
                public void onFailure(JSONObject arg0) {
                }
                @Override
                public void onSuccess(final JSONObject arg0) {
                    try {
                        Log.i("fetchUserDataByClientId", clientId + "," + arg0.toString());
                        JSONObject userJson = arg0.getJSONObject("response").getJSONArray("users").getJSONObject(0);
                        User user = new User(userJson);
                        saveUser(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }
    /***
     * 匹配查找用户
     * */
    /***
     * @param username
     * @param cbk //搜索好友的回调接口
     *    查找结果会按照limit分页, 每一页包含limit个结果
     * */

    public void searchRemoteUser(final String username,final FetchUserCallback cbk){//通过用户名搜索用户，最多搜索100人（通过搜索）
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("username",username);
                    params.put("limit",100);
                    anSocial.sendRequest(WebInterface.searchRemoteUser(), AnSocialMethod.GET, params,  new IAnSocialCallback() {
                        @Override
                        public void onFailure(JSONObject response) {
                            Log.i("searchRemoteUser", response.toString());
                            if (cbk != null) {
                                List<User> userList = new ArrayList<User>();
                                cbk.onFinish(userList);
                            }
                        }
                        @Override
                        public void onSuccess(JSONObject response) {
                            List<User> userList = new ArrayList<User>();
                            Log.i("searchRemoteUser", response.toString());
                            try {
                                JSONArray users = response.getJSONObject("response").getJSONArray("users");
                                for(int i =0;i<users.length();i++){
                                    JSONObject userJson = users.getJSONObject(i);
                                    User user = new User(userJson);
                                    saveUser(user);

                                    if(!user.userId.equals(currentUser.userId)){//过滤掉自己
                                        userList.add(user);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(cbk!=null){
                                cbk.onFinish(userList);
                            }
                        }
                    });
                } catch (ArrownockException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 加为好友
     * target_user_id 	与该用户成为好友的用户, 传值为调用users/create.json时生成的用户id
     * user_id 	添加好友的用户, 传值为调用users/create.json时生成的用户id
     * */
    public void addFriendRemote(final User targetUser){
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", currentUser.userId);
        params.put("target_user_id", targetUser.userId);
        try {
            anSocial.sendRequest(WebInterface.addFriendRemote(), AnSocialMethod.POST, params, new IAnSocialCallback(){
                @Override
                public void onFailure(JSONObject arg0) {
                }
                @Override
                public void onSuccess(final JSONObject arg0) {
                    Constant.txlFragment.onFinish(targetUser);
                    saveUser(targetUser);
                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送好友请求
     * target_user_id 	与该用户成为好友的用户, 传值为调用users/create.json时生成的用户id
     * user_id 	添加好友的用户, 传值为调用users/create.json时生成的用户id
     * */
    public void sendFriendRequest(final User targetUser, final IAnSocialCallback cbk){
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", currentUser.userId);
        params.put("target_user_id", targetUser.userId);
        try {
            anSocial.sendRequest(WebInterface.sendFriendRequest(), AnSocialMethod.POST, params, new IAnSocialCallback(){
                @Override
                public void onFailure(JSONObject arg0) {
                    if(cbk!=null){
                        cbk.onFailure(arg0);
                    }
                }
                @Override
                public void onSuccess(final JSONObject arg0) {
                    Log.i("sendFriendRequest发送好友", arg0.toString());
//                    addFriendRemote(targetUser);
                    if(cbk!=null){
                        cbk.onSuccess(arg0);
                    }
                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }
    /**
     * 接受好友请求
     * target_user_id 	与该用户成为好友的用户, 传值为调用users/create.json时生成的用户id
     * user_id 	添加好友的用户, 传值为调用users/create.json时生成的用户id
     * */
    public void approveFriendRequest(final FriendRequest request,final IAnSocialCallback cbk){
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("request_id", request.friendRequestId);
        params.put("keep_request", true);
        try {
            anSocial.sendRequest(WebInterface.approveFriendRequest(), AnSocialMethod.POST, params, new IAnSocialCallback(){
                @Override
                public void onFailure(JSONObject arg0) {
                    if(cbk!=null){
                        cbk.onFailure(arg0);
                    }
                }
                @Override
                public void onSuccess(final JSONObject arg0) {
                    addFriendRemote(request.user());
                    try {
                        Map<String,String> c_data = new HashMap<String,String>();
                        c_data.put(Constant.FRIEND_REQUEST_KEY_TYPE, Constant.FRIEND_REQUEST_TYPE_APPROVE);
                        AnIMManger.getInstance(context).getAnIM().sendBinary(request.user().clientId, new byte[1], Constant.FRIEND_REQUEST_TYPE_SEND,c_data);
                    } catch (ArrownockException e) {
                        e.printStackTrace();
                    }
                    if(cbk!=null){
                        cbk.onSuccess(arg0);
                    }
                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }
    /**
     * 拒绝好友请求
     * target_user_id 	与该用户成为好友的用户, 传值为调用users/create.json时生成的用户id
     * user_id 	添加好友的用户, 传值为调用users/create.json时生成的用户id
     * */
    public void rejectFriendRequest(final FriendRequest request,final IAnSocialCallback cbk){
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("request_id", request.friendRequestId);
        params.put("keep_request", true);
        try {
            anSocial.sendRequest(WebInterface.rejectFriendRequest(), AnSocialMethod.POST, params, new IAnSocialCallback(){
                @Override
                public void onFailure(JSONObject arg0) {
                    if(cbk!=null){
                        cbk.onFailure(arg0);
                    }
                }
                @Override
                public void onSuccess(final JSONObject arg0) {
                    if(cbk!=null){
                        cbk.onSuccess(arg0);
                    }
                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取好友列表
     * user_id 	根据用户进行查询, 传值为调用users/create.json时生成的用户idv
     * */
    public void fetchMyRemoteFriend(final IAnSocialCallback cbk){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("user_id", currentUser.userId);
                    params.put("limit", 100);
                    anSocial.sendRequest(WebInterface.fetchMyRemoteFriend(), AnSocialMethod.GET, params,  new IAnSocialCallback() {
                        @Override
                        public void onFailure(JSONObject response) {
                            Log.i("fetchMyRemoteFriend", response.toString());
                            if(cbk!=null){
                                cbk.onFailure(response);
                            }
                        }
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.i("fetchMyRemoteFriend", response.toString());
//                            try {
//                                JSONArray users = response.getJSONObject("response").getJSONArray("friends");
////                                for(int i =0;i<users.length();i++){
////                                    JSONObject userJson = users.getJSONObject(i);
////                                    User user = new User(userJson);
////                                    saveUser(user);
////                                    //isMutual相互的
////                                    boolean isMutual = userJson.getJSONObject("friendProperties").getBoolean("isMutual");
////                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                            if(cbk!=null){
                                cbk.onSuccess(response);
                            }
                        }
                    });
                } catch (ArrownockException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    /***
     * 注册成功后将用户信息保存在本地
     * */
    public void saveUser(User user){
        if((user.userId==null || user.userName==null) && user.clientId!=null){
            fetchUserInfoByClientId(user.clientId);
        }

        if(!user.same()){//先判断用户信息是否有改变
            user.update();

            setChanged();
            notifyObservers(UpdateType.User);
        }
    }
    public User getUserByClientId(String clientId){
        return new Select()
                .from(User.class)
                .where("clientId = ?",clientId).executeSingle();
    }
    public interface FetchUserCallback{//搜索好友的回调接口
        public void onFinish(List<User> data);
    }
    public interface updateFriendsrCallback{//搜索好友的回调接口
        public void onFinish(User user);
    }
}