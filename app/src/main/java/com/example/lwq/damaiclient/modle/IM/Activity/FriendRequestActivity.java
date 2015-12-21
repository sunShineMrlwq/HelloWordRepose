package com.example.lwq.damaiclient.modle.IM.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arrownock.exception.ArrownockException;
import com.arrownock.social.AnSocialMethod;
import com.arrownock.social.IAnSocialCallback;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.app.BaseActivity;
import com.example.lwq.damaiclient.modle.IM.IMApp.UserManager;
import com.example.lwq.damaiclient.modle.IM.IMApp.WebInterface;
import com.example.lwq.damaiclient.modle.IM.adapter.FriendRequestAdapter;
import com.example.lwq.damaiclient.modle.IM.adapter.UserListAdapter;
import com.example.lwq.damaiclient.modle.IM.bean.FriendRequest;
import com.example.lwq.damaiclient.modle.IM.bean.User;
import com.example.lwq.damaiclient.widget.AppBaryx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lwq on 2015/12/15.
 */
public class FriendRequestActivity extends BaseActivity implements View.OnClickListener {
    private AppBaryx appBaryx;
    private TextView header_back,type_city, header_tab, header_title,header_conetnt;
    private ImageView imgae_right;
    private EditText username_et;
    FriendRequestAdapter mUserListAdapter;
    private List<FriendRequest> friendsRequstLsit=new ArrayList<FriendRequest>();
    ListView mListView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im_friendsrequest);
        initView();
        fetchFriendRequest();
    }

    public void initView() {//初始化布局
        appBaryx = (AppBaryx) findViewById(R.id.footbar);
        header_back= appBaryx.getHeader_back();
        type_city = appBaryx.getType_city();
        header_tab = appBaryx.getHeader_tab();
        header_title = appBaryx.getHeader_titletv();
        imgae_right = appBaryx.getImgae_right();
        header_conetnt= appBaryx.getHeader_conetnt();
        header_back.setText("新的朋友");
        header_conetnt.setText("添加朋友");
        header_back.setVisibility(View.VISIBLE);
        header_conetnt.setVisibility(View.VISIBLE);
        type_city.setVisibility(View.GONE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);
        header_tab.setVisibility(View.GONE);


        mListView = (ListView)findViewById(R.id.friend_listView);
//        mListView.setDivider(null);
//        mListView.setDividerHeight(0);
        mUserListAdapter = new FriendRequestAdapter(this);
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

                break;
        }
    }
    /**
     * 获取好友请求
     * target_user_id 与该用户成为好友的用户, 传值为调用users/create.json时生成的用户id
     * */
    public void fetchFriendRequest(){
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("to_user_id", UserManager.getInstance(this).currentUser.userId);
        params.put("limit", 100);
        try {
            UserManager.getInstance(this).anSocial.sendRequest(WebInterface.fetchFriendRequest(), AnSocialMethod.GET, params, new IAnSocialCallback(){
                @Override
                public void onFailure(JSONObject arg0) {
                    Log.d("TxlFragment", arg0.toString());
                }
                @Override
                public void onSuccess(final JSONObject arg0) {
                    try {
                        JSONArray requests = arg0.getJSONObject("response").getJSONArray("friendRequests");
                        Log.d("fetchFriend好友请求列表", requests.toString());
                        for(int i =0;i<requests.length();i++){
                            FriendRequest friendReqst = new FriendRequest(UserManager.getInstance(FriendRequestActivity.this).currentUser.userId,requests.getJSONObject(i));
                            friendsRequstLsit.add(friendReqst);
                        }
                        mUserListAdapter.applyData(friendsRequstLsit);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }

}
