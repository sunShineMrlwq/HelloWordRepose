package com.example.lwq.damaiclient.modle.IM.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arrownock.im.callback.AnIMMessageSentCallbackData;
import com.arrownock.im.callback.AnIMReadACKCallbackData;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.app.BaseActivity;
import com.example.lwq.damaiclient.modle.IM.IMApp.AnIMManger;
import com.example.lwq.damaiclient.modle.IM.IMApp.UserManager;
import com.example.lwq.damaiclient.modle.IM.IMUtils.Constant;
import com.example.lwq.damaiclient.modle.IM.adapter.ChatMessageAdapter;
import com.example.lwq.damaiclient.modle.IM.bean.Chat;
import com.example.lwq.damaiclient.modle.IM.bean.ChatUser;
import com.example.lwq.damaiclient.modle.IM.bean.IMMessage;
import com.example.lwq.damaiclient.modle.IM.bean.User;
import com.example.lwq.damaiclient.widget.AppBaryx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by lwq on 2015/12/8.
 */
public class ChatMessageActivity extends BaseActivity implements View.OnClickListener,Observer {
    private AppBaryx appBaryx;
    private TextView header_back,type_city, header_tab, header_title,header_conetnt;
    private ImageView imgae_right;
    private  Button bt_sendMessage;
    private ListView listview_chatmessage;
    private AnIMManger  sIMManger;
    private EditText et_message_content;
    private ChatMessageAdapter chatMessageAdapter;
    private Chat mChat;
    private ChatUser myUser;
    List<IMMessage> messageList=new ArrayList<IMMessage>();
    Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_chatmessage);
        Constant.chatMessageActivity=this;

        sIMManger = AnIMManger.getInstance(this);//绑定注册所有Observer
        sIMManger.addObserver(this);

        initView();
        initData();
    }

    public void initView() {//初始化布局
        appBaryx = (AppBaryx) findViewById(R.id.footbar);
        header_back= appBaryx.getHeader_back();
        type_city = appBaryx.getType_city();
        header_tab = appBaryx.getHeader_tab();
        header_title = appBaryx.getHeader_titletv();
        imgae_right = appBaryx.getImgae_right();
        header_conetnt= appBaryx.getHeader_conetnt();
        header_back.setText("返回");
        header_tab.setText("逗乐勒");
        header_tab.setVisibility(View.VISIBLE);
        header_back.setVisibility(View.VISIBLE);
        header_conetnt.setVisibility(View.GONE);
        type_city.setVisibility(View.GONE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);
        bt_sendMessage = (Button) findViewById(R.id.bt_sendMessage);
        listview_chatmessage  = (ListView) findViewById(R.id.listview_chatmessage);

        et_message_content = (EditText) findViewById(R.id.et_message_content);

        header_back.setOnClickListener(this);
        bt_sendMessage.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.bt_sendMessage:
                sendMessage();
                break;
        }
    }

    //初始化数据（显示和好友聊天的保存在本地的数据）
    public void initData(){
        mChat = (Chat) getIntent().getExtras().getSerializable(Constant.INTENT_EXTRA_KEY_CHAT);
        User mUser = UserManager.getInstance(this).currentUser;
        myUser = new ChatUser(mUser.clientId,mUser.userName,mUser.userPhotoUrl);

        chatMessageAdapter=new ChatMessageAdapter(this,messageList);
        listview_chatmessage.setAdapter(chatMessageAdapter);
        fillLocalData();
    }
    //发送信息
    public void sendMessage(){
        String text = et_message_content.getText().toString();
        if(text !=null && text.length()>0){
            IMMessage msg=new IMMessage();
            msg.message = text;
            msg.type = IMMessage.TYPE_TEXT;
            msg.chat = mChat;

            tosendMessage(msg);
            et_message_content.setText("");
        }
    }
    public IMMessage tosendMessage(IMMessage message){
        message.chat = mChat;
        IMMessage msg = sIMManger.sendMessage(myUser, message);
        return msg;
    }

    //observable通知更新
    @Override
    public void update(Observable observable, final Object data){//通知更新
        if(data instanceof IMMessage) {
            final IMMessage msg = (IMMessage) data;
            appendUpdateMessage(msg);//添加发送的消息，发送的状态

        }else if(data instanceof AnIMMessageSentCallbackData){//已发送的通知（发送失败，以及发送成功）
            Log.i("ChatMessageActivity", "消息发送成功？");
            handler.post(new Thread() {
                @Override
                public void run() {
                    AnIMMessageSentCallbackData msgData = (AnIMMessageSentCallbackData) data;

                    if (msgData.isError()) {
                        chatMessageAdapter.updateMessageStatus(msgData.getMsgId(), IMMessage.STATUS_FAILED);
                    } else {
                        chatMessageAdapter.updateMessageStatus(msgData.getMsgId(), IMMessage.STATUS_SENT);
                    }
                }
            });

        }else if(data instanceof AnIMReadACKCallbackData){//消息已阅读的通知
            handler.post(new Thread() {
                @Override
                public void run() {
                    AnIMReadACKCallbackData msgData = (AnIMReadACKCallbackData) data;
                }
            });
        }

    }

    //添加发送的信息
    public void appendUpdateMessage(IMMessage msg){
        if(TextUtils.equals(mChat.targetClientId,msg.chat.targetClientId)) {
            chatMessageAdapter.addMessage(msg);
            listview_chatmessage.setSelection(listview_chatmessage.getBottom());
        }
    }
    //获取本地储存的数据
    public void fillLocalData(){
        AnIMManger.getInstance(this).getMessageByChat(mChat, new AnIMManger.GetMessageCallback(){
            @Override
            public void onFinish(final List<IMMessage> data) {
                Log.i("IMMessage数据",data.size()+"");
                chatMessageAdapter.applyData(data);
                listview_chatmessage.setSelection(listview_chatmessage.getBottom());
            }
        });
    }
}