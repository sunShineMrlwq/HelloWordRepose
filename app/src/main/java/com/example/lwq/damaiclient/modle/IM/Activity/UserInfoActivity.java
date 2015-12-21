package com.example.lwq.damaiclient.modle.IM.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arrownock.social.IAnSocialCallback;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.app.BaseActivity;
import com.example.lwq.damaiclient.modle.IM.IMApp.AnIMManger;
import com.example.lwq.damaiclient.modle.IM.IMApp.UserManager;
import com.example.lwq.damaiclient.modle.IM.IMUtils.Constant;
import com.example.lwq.damaiclient.modle.IM.bean.Chat;
import com.example.lwq.damaiclient.modle.IM.bean.User;
import com.example.lwq.damaiclient.widget.AppBaryx;

import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by lwq on 2015/12/8.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener,Observer {
    private AppBaryx appBaryx;
    private TextView tv_username, header_back, type_city, header_tab, header_title, header_conetnt;
    private ImageView imgae_right;
    private Button bt_sendMessage, bt_addfrineds;
    private User user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_userinfo);
        initView();
        UserManager.getInstance(this).addObserver(UserInfoActivity.this);
    }

    public void initView() {//初始化布局
        appBaryx = (AppBaryx) findViewById(R.id.footbar);
        header_back = appBaryx.getHeader_back();
        type_city = appBaryx.getType_city();
        header_tab = appBaryx.getHeader_tab();
        header_title = appBaryx.getHeader_titletv();
        imgae_right = appBaryx.getImgae_right();
        header_conetnt = appBaryx.getHeader_conetnt();
        header_back.setText("返回");
        header_tab.setText("详细资料");
        header_tab.setVisibility(View.VISIBLE);
        header_back.setVisibility(View.VISIBLE);
        header_conetnt.setVisibility(View.GONE);
        type_city.setVisibility(View.GONE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);
        bt_sendMessage = (Button) findViewById(R.id.bt_sendMessage);
        bt_addfrineds = (Button) findViewById(R.id.bt_addfrineds);
        if(getIntent().getBooleanExtra("isFriend",false)){
            bt_addfrineds.setVisibility(View.GONE);
        }else{
            bt_addfrineds.setVisibility(View.VISIBLE);
        }

        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_username.setText(getIntent().getStringExtra("USERNAME"));

        header_back.setOnClickListener(this);
        bt_sendMessage.setOnClickListener(this);
        bt_addfrineds.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.bt_sendMessage://发消息
               user= (User) getIntent().getExtras().getSerializable("user");
//                user=UserManager.getInstance(this).fetchUserInfoByClientId();//通过用户的clienetId，获取用户信息
                Chat chat = AnIMManger.getInstance(this).addChat(user.clientId);//添加一个聊天对象
                AnIMManger.getInstance(this).notifyChatUpdated();
                Intent i = new Intent( this,ChatMessageActivity.class);//聊天页面
                Bundle b = new Bundle();
                b.putSerializable(Constant.INTENT_EXTRA_KEY_CHAT, chat);
                i.putExtras(b);
                startActivity(i);
                finish();
                break;
            case R.id.bt_addfrineds://加好友
                sendfriend((User) getIntent().getSerializableExtra("user"));
                break;
        }
    }

    public void sendfriend(User user) {
        UserManager.getInstance(this).sendFriendRequest(user, new IAnSocialCallback(){
            @Override
            public void onSuccess(JSONObject jsonObject) {
                Toast.makeText(UserInfoActivity.this, ("已发送"), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(JSONObject jsonObject) {

            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof UserManager){
            if(data instanceof UserManager.UpdateType){
                if(((UserManager.UpdateType)data).equals(UserManager.UpdateType.User)){
                    refreshUser();
                }
            }
        }
    }
    private void refreshUser(){
        user = user.getFromTable();
//        mUserDetailView.setUserInfo(user);//gengxin
    }
}
