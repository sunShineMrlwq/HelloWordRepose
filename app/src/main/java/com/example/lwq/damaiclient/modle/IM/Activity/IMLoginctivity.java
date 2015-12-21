package com.example.lwq.damaiclient.modle.IM.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arrownock.social.IAnSocialCallback;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.activity.UserModule.LoginActivity;
import com.example.lwq.damaiclient.app.BaseActivity;
import com.example.lwq.damaiclient.modle.IM.IMApp.AnIMManger;
import com.example.lwq.damaiclient.modle.IM.IMApp.UserManager;
import com.example.lwq.damaiclient.modle.IM.IMUtils.SpfHelper;
import com.example.lwq.damaiclient.modle.IM.adapter.PYQAdapter;
import com.example.lwq.damaiclient.modle.IM.bean.User;
import com.example.lwq.damaiclient.utils.BlurUtil;
import com.example.lwq.damaiclient.utils.ListViewHeightBased;
import com.example.lwq.damaiclient.widget.AppBaryx;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwq on 2015/12/8.
 */
public class IMLoginctivity extends BaseActivity implements View.OnClickListener {
    private AppBaryx appBaryx;
    private TextView header_back,type_city, header_tab, header_title,header_conetnt;
    private ImageView imgae_right;
    private Button login_btn,regsiter_btn;
    private EditText username_et,password_et;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_login);
        initView();
    }

    public void initView() {//初始化布局
        appBaryx = (AppBaryx) findViewById(R.id.footbar);
        header_back= appBaryx.getHeader_back();
        type_city = appBaryx.getType_city();
        header_tab = appBaryx.getHeader_tab();
        header_title = appBaryx.getHeader_titletv();
        imgae_right = appBaryx.getImgae_right();
        header_conetnt= appBaryx.getHeader_conetnt();
        header_back.setText("登陆");
        header_back.setVisibility(View.VISIBLE);
        header_conetnt.setVisibility(View.GONE);
        type_city.setVisibility(View.GONE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);
        header_tab.setVisibility(View.GONE);

        username_et = (EditText) findViewById(R.id.user_name);
        password_et = (EditText) findViewById(R.id.password);

        login_btn = (Button) findViewById(R.id.login_btn);
        regsiter_btn = (Button) findViewById(R.id.regsiter_btn);

        login_btn.setOnClickListener(this);
        header_back.setOnClickListener(this);
        regsiter_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_back:
                onBackPressed();
//                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
            case R.id.login_btn://IM登录
                if(username_et.getText().toString().trim().equals("")){
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password_et.getText().toString().trim().equals("")){
                    Toast.makeText( this, "用密码不能为空",  Toast.LENGTH_SHORT).show();;
                    return;
                }

                login();

                break;
            case R.id.regsiter_btn:
                if(username_et.getText().toString().trim().equals("")){
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password_et.getText().toString().trim().equals("")){
                    Toast.makeText( this, "用密码不能为空",  Toast.LENGTH_SHORT).show();;
                    return;
                }

                singUp();

                break;
        }
    }
    //注册
    public void singUp(){
        UserManager.getInstance(this).signUp(username_et.getText().toString(), password_et.getText().toString(), new IAnSocialCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    Toast.makeText(IMLoginctivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    JSONObject userJson = jsonObject.getJSONObject("response").getJSONObject("user");
                    User user = new User(userJson);
                    afterLogin(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(JSONObject jsonObject) {
                try {
                    Toast.makeText(IMLoginctivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    String errorMsg = jsonObject.getJSONObject("meta").getString("message");
                    Toast.makeText(getBaseContext(), errorMsg,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //登陆
    public void login() {
        UserManager.getInstance(this).login(username_et.getText().toString(), password_et.getText().toString(), new IAnSocialCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    JSONObject userJson = jsonObject.getJSONObject("response").getJSONObject("user");
                    User user = new User(userJson);
                    afterLogin(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(JSONObject jsonObject) {
                try {
                    String errorMsg = jsonObject.getJSONObject("meta").getString("message");
                    Toast.makeText(getBaseContext(), errorMsg,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /***
     * 登陆成功后,连接AnIM
     * */
    public void afterLogin(User user) {
        Log.i("IMLoginctivity", "进入anim链接");
        if (!SpfHelper.getInstance(this).hasSignIn()) {
            SpfHelper.getInstance(this).saveUserInfo(username_et.getText().toString(), password_et.getText().toString());
        }
        AnIMManger.getInstance(this).connect(user.clientId);
        UserManager.getInstance(this).setCurrentUser(user);

//        AnIMManger.getInstance(context).fetchAllRemoteTopic();
//        UserManager.getInstance(context).fetchMyRemoteFriend(null);

//        AnIMManger.getInstance(context).bindAnPush();
        Intent i = new Intent(this, IMMainActivity.class);
        startActivity(i);
        finish();
    }
}
