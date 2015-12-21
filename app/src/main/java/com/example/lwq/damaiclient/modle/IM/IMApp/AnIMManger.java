package com.example.lwq.damaiclient.modle.IM.IMApp;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.query.Select;
import com.arrownock.exception.ArrownockException;
import com.arrownock.im.AnIM;
import com.arrownock.im.callback.AnIMCallbackAdapter;
import com.arrownock.im.callback.AnIMMessageCallbackData;
import com.arrownock.im.callback.AnIMMessageSentCallbackData;
import com.arrownock.im.callback.AnIMReadACKCallbackData;
import com.arrownock.im.callback.AnIMReceiveACKCallbackData;
import com.arrownock.im.callback.AnIMTopicMessageCallbackData;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.modle.IM.IMUtils.Constant;
import com.example.lwq.damaiclient.modle.IM.bean.Chat;
import com.example.lwq.damaiclient.modle.IM.bean.ChatUser;
import com.example.lwq.damaiclient.modle.IM.bean.IMMessage;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by lwq on 2015/12/10.
 */
public class AnIMManger extends Observable {
    private static AnIMManger sAnIMManger;
    public AnIM anIM;
    public Context context;
    private Handler handler;
    public static String currentClientId ;
    private boolean retryConnect = false;//是否重新连接
    public enum UpdateType{
        Topic,Chat,FriendRequest,Like
    }

    private AnIMManger(Context ct){
        this.context = ct;
        handler = new Handler();
        try {
            //(接收消息和消息回执)
//            IAnIMCallback imCallback = new MessageCallback();
            anIM = new AnIM(ct,ct.getString(R.string.app_key));
            anIM.setCallback(imCallback);
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }

    /**
     *创建AnIMManger单例实例，并做初始化配置
     * */
    public static AnIMManger getInstance(Context ct){
        if(sAnIMManger==null){
            sAnIMManger = new AnIMManger(ct);
        }
        return sAnIMManger;
    }
    /**
     *获取AnIM实例
     * */
    public AnIM getAnIM(){
        return anIM;
    }
    /**
     *获取AnIM连接
     * */
    public void connect(String clientId){
        this.currentClientId = clientId;
        retryConnect = true;//是否重新连接
        try {
            anIM.connect(clientId);
        } catch (ArrownockException e) {

            e.printStackTrace();
        }
    }
    /**
     *获取断开连接
     * */
    public void disconnect(boolean logout){
        retryConnect=false;
        if(logout){//退出
            currentClientId=null;
        }
        try {
            anIM.disconnect();
        } catch (ArrownockException e) {
            e.printStackTrace();e.printStackTrace();
        }
    }
    //获取当前的用户的ClientId
    public String getCurrentClientId(){
        return currentClientId;
    }

    //接受到消息的处理
    public void handleChatMessage(Object data){
        IMMessage msg=new IMMessage();
        if(data instanceof AnIMMessageCallbackData){//一对一聊天接受消息的回调
            AnIMMessageCallbackData msgData = (AnIMMessageCallbackData)data;

            Chat chat =AnIMManger.getInstance(context).addChat(msgData.getFrom());

            msg.currentClientId = currentClientId;
            msg.chat = chat;
            msg.message = msgData.getMessage();
            msg.msgId = msgData.getMsgId();
            msg.fromClient = msgData.getFrom();
            msg.status = IMMessage.STATUS_SENT;
            msg.type = IMMessage.TYPE_TEXT;
            msg.readed = false;
            if(msgData.getCustomData()!=null){
                if( msgData.getCustomData().containsKey("name"))
                    msg.fromUsername = msgData.getCustomData().get("name");
                if( msgData.getCustomData().containsKey("photoUrl"))
                    msg.fromUserIconUrl = msgData.getCustomData().get("photoUrl");
            }
            msg.update();
        }else if(data instanceof AnIMTopicMessageCallbackData) {//群组接受消息的回调

        }
    }
    /**
     * Created by lwq on 2015/12/10.
     *  AnIMCallbackAdapter接口的实现类可接收到相应方法的异步执行返回结果,
     *  AnIMCallbackAdapter 是此接口的空实现
     */
    private AnIMCallbackAdapter imCallback = new AnIMCallbackAdapter(){
        //给好友发送消息的回调
        @Override
        public void messageSent(final AnIMMessageSentCallbackData data) {
            Log.i("IMManergerManager", "messageSent");
            final IMMessage msg = new IMMessage();
            msg.msgId = data.getMsgId();
            msg.currentClientId = currentClientId;
            msg.readed = true;

            if(data.isError()){//判断消息发送是否成功
                data.getException().printStackTrace();
                msg.status = IMMessage.STATUS_FAILED;//发送失败
            }else{
                msg.status = IMMessage.STATUS_SENT;//发送成功
            }

            msg.update();//重置消息的内容（给消息赋值）

            Log.i("IMManergerManager消息的状态", msg.getFromTable().status);

            setChanged();//设置数据更新为true
            notifyObservers(data);//更新数据
        }

        //这条消息的接收方将会通过下面的回调方法获得到消息:
        @Override
        public void receivedMessage(AnIMMessageCallbackData data) {
            handleChatMessage(data);
            setChanged();//设置数据更新为true
            notifyObservers(data);//更新数据

        }
        // 已送达
        @Override
        public void receivedReceiveACK(AnIMReceiveACKCallbackData data) {
            super.receivedReceiveACK(data);
        }
        // 已阅读
        @Override
        public void receivedReadACK(AnIMReadACKCallbackData anIMReadACKCallbackData) {
            super.receivedReadACK(anIMReadACKCallbackData);
        }
    };


    //发送消息
    public IMMessage sendMessage(ChatUser user,IMMessage message) {
        Chat chat = message.chat;
        String msgId=null;
        Map<String, String> customData = new HashMap<String, String>();
        customData.put("name",user.getUsername());
        customData.put("photoUrl", user.getIconUrl());
        //发送私聊文本消息、自定义数据到指定的客户端，并指定是否需要消息送达接收者的确认信息.
        try {
            msgId = anIM.sendMessage(chat.targetClientId, message.message, customData, true);
        } catch (ArrownockException e) {
            Log.i("AnIMManger", "发送消息出错了");
            e.printStackTrace();
        }
        message.msgId = msgId;
        message.currentClientId = currentClientId;
        message.fromClient = currentClientId;
        message.status = IMMessage.STATUS_SENDING;
        message.readed = true;

        message.fromUsername = user.getUsername();
        message.fromUserIconUrl = user.getIconUrl();
        message.update();

        setChanged();
        notifyObservers(message);
        return  message;
    }


    //一下是本地的

    /**
     * 给指定好友发送消息
     * */
    public Chat addChat(String targetClientId){//给指定好友发信息
        Chat chat = new Chat();
        chat.currentClientId = currentClientId;
        chat.targetClientId = targetClientId;
        chat.update();
        return chat.getFromTable();
    }
    /**
     * 获取和某个人的聊天记录
     * */
    public void getMessageByChat(final Chat chat,final GetMessageCallback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("getMessageByChat", (chat.topic == null) + "");
              final List<IMMessage>  data= chat.messages();
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        if(callback!=null){
                            callback.onFinish(data);
                        }
                    }
                });
            }
        }).start();
    }

    //TODO Chat
    /**
     * @param   callback 聊天记录的回调
     * */
    public void getAllMyChat(final GetChatCallback callback){//获取和所有用户的聊天记录
        new Thread(new Runnable(){
            @Override
            public void run() {
                final List<Chat> data = new Select().from(Chat.class).where("currentClientId = ? ",currentClientId).execute();
                Collections.sort(data, new Comparator<Chat>() {
                    @Override
                    public int compare(Chat chat1, Chat chat2) {
                        if (chat1.lastMessage() == null || chat2.lastMessage() == null) {
                            return 1;
                        }

                        long time1 = chat1.lastMessage().timestamp;
                        long time2 = chat2.lastMessage().timestamp;
                        if (time1 > time2) {
                            return -1;
                        } else if (time1 < time2) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });

                Log.i("getAllMyChat", data.size()+"");
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        if(callback!=null){
                            callback.onFinish(data);
                        }
                    }
                });
            }
        }).start();
    }
    public void notifyChatUpdated() {//更新
        setChanged();
        notifyObservers(UpdateType.Chat);
    }
    public interface GetChatCallback{//所有用户的聊天记录的回调
        public void onFinish(List<Chat> data);
    }
    public interface GetMessageCallback{
        public void onFinish(List<IMMessage> data);
    }


}
