package com.example.lwq.damaiclient.activity.UserModule;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.app.BaseActivity;
import com.example.lwq.damaiclient.bean.User;
import com.example.lwq.damaiclient.constant.ConstantFinal;
import com.example.lwq.damaiclient.utils.SharedPreferenceUtil;
import com.example.lwq.damaiclient.widget.AppBaryx;

/**
 * @author lwq
 *         主界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private AppBaryx appBaryx;
    private TextView header_back,type_city, header_tab, header_title,header_conetnt;
    private ImageView imgae_right;
    private Button login_btn,regsiter_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        header_conetnt.setText("注册");
        header_back.setText("登陆");
        header_back.setVisibility(View.VISIBLE);
        header_conetnt.setVisibility(View.VISIBLE);
        type_city.setVisibility(View.GONE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);
        header_tab.setVisibility(View.GONE);

        login_btn = (Button) findViewById(R.id.login_btn);

        login_btn.setOnClickListener(this);
        header_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                ConstantFinal.outlogin=false;
                SharedPreferenceUtil.getInstance("USERINFO").saveshuxingData("username", "lwq");
                setResult(ConstantFinal.islogin);
                finish();
                break;
            case R.id.header_back:
                onBackPressed();
                break;
        }
    }
}
