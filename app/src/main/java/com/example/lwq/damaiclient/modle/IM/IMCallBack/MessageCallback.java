package com.example.lwq.damaiclient.modle.IM.IMCallBack;

import android.os.Message;
import android.util.Log;

import com.arrownock.im.callback.AnIMCallbackAdapter;
import com.arrownock.im.callback.AnIMMessageCallbackData;
import com.arrownock.im.callback.AnIMMessageSentCallbackData;
import com.arrownock.im.callback.AnIMReceiveACKCallbackData;
import com.arrownock.im.callback.AnIMTopicMessageCallbackData;
import com.example.lwq.damaiclient.modle.IM.IMApp.AnIMManger;
import com.example.lwq.damaiclient.modle.IM.bean.IMMessage;

/**
 * Created by lwq on 2015/12/10.
 *  AnIMCallbackAdapter接口的实现类可接收到相应方法的异步执行返回结果,
 *  AnIMCallbackAdapter 是此接口的空实现
 */
public class MessageCallback extends AnIMCallbackAdapter {

    //给好友发送消息的回调
    @Override
    public void messageSent(AnIMMessageSentCallbackData data) {
        Log.i("IMManergerManager", "messageSent");
        IMMessage msg = new IMMessage();
        msg.msgId = data.getMsgId();
        msg.currentClientId = AnIMManger.currentClientId;
        msg.readed = true;

        if(data.isError()){//判断消息发送是否成功
            data.getException().printStackTrace();
            msg.status = IMMessage.STATUS_FAILED;//发送失败
        }else{
            msg.status = IMMessage.STATUS_SENT;//发送成功
        }
    }

    //接受来自好友发送的信息的回调
    @Override
    public void receivedMessage(AnIMMessageCallbackData data) {
        handleChatMessage(data);

    }
    //接受来自箭扣服务器发送的信息的回调
    @Override
    public void receivedReceiveACK(AnIMReceiveACKCallbackData data) {
        super.receivedReceiveACK(data);
    }

    public void handleChatMessage(Object data){
        IMMessage msg=new IMMessage();
        if(data instanceof AnIMMessageCallbackData){//一对一聊天接受消息的回调

        }else if(data instanceof AnIMTopicMessageCallbackData) {//群组接受消息的回调

        }
    }
}
