package com.example.lwq.damaiclient.modle.IM.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.app.BaseActivity;
import com.example.lwq.damaiclient.modle.IM.IMApp.UserManager;
import com.example.lwq.damaiclient.modle.IM.adapter.UserListAdapter;
import com.example.lwq.damaiclient.modle.IM.bean.User;
import com.example.lwq.damaiclient.widget.AppBaryx;

import java.util.List;

/**
 * Created by lwq on 2015/12/15.
 */
public class AddFriendActivity extends BaseActivity implements View.OnClickListener {
    private AppBaryx appBaryx;
    private TextView header_back,type_city, header_tab, header_title,header_conetnt;
    private ImageView imgae_right;
    private EditText username_et;
    UserListAdapter mUserListAdapter;
    ListView mListView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_search);
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
        header_back.setText("添加朋友");
        header_conetnt.setText("搜索");
        header_back.setVisibility(View.VISIBLE);
        header_conetnt.setVisibility(View.VISIBLE);
        type_city.setVisibility(View.GONE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);
        header_tab.setVisibility(View.GONE);

        username_et = (EditText) findViewById(R.id.user_name);
        username_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fillRemoteData(username_et.getText().toString());//通过用户名匹配搜索相关用户
                }
                return false;
            }
        });
        mListView = (ListView)findViewById(R.id.friend_search_listView);
//        mListView.setDivider(null);
//        mListView.setDividerHeight(0);
        mUserListAdapter = new UserListAdapter(this);
        mListView.setAdapter(mUserListAdapter);

        header_back.setOnClickListener(this);
        header_conetnt.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back:
                onBackPressed();
                break;
            case R.id.header_conetnt:
                fillRemoteData(username_et.getText().toString());//通过用户名匹配搜索相关用户
                break;
        }
    }

    public void fillRemoteData(String username){//通过用户名匹配搜索相关用户
        UserManager.getInstance(this).searchRemoteUser(username, new UserManager.FetchUserCallback(){
            @Override
            public void onFinish(final List<User> users) {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        mUserListAdapter.applyData(users);
                    }
                });
            }
        });
    }
}
