package com.example.lwq.damaiclient.modle.IM.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.arrownock.exception.ArrownockException;
import com.arrownock.social.AnSocialMethod;
import com.arrownock.social.IAnSocialCallback;
import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.adapter.TabListPagerAdapter;
import com.example.lwq.damaiclient.app.BaseFragment;
import com.example.lwq.damaiclient.modle.IM.Activity.ChatMessageActivity;
import com.example.lwq.damaiclient.modle.IM.Activity.UserInfoActivity;
import com.example.lwq.damaiclient.modle.IM.IMApp.UserManager;
import com.example.lwq.damaiclient.modle.IM.IMApp.WebInterface;
import com.example.lwq.damaiclient.modle.IM.IMUtils.Constant;
import com.example.lwq.damaiclient.modle.IM.Utils.PinYinSearh.AssortView;
import com.example.lwq.damaiclient.modle.IM.Utils.PinYinSearh.PinyinAdapter;
import com.example.lwq.damaiclient.modle.IM.bean.FriendRequest;
import com.example.lwq.damaiclient.modle.IM.bean.User;
import com.example.lwq.damaiclient.utils.ListViewHeightBased;
import com.example.lwq.damaiclient.widget.AppBaryx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lwq on 2015/12/4.
 */
public class TxlFragment extends BaseFragment implements View.OnClickListener ,UserManager.updateFriendsrCallback {
    View root;
    private AppBaryx appBaryx;
    private TextView header_back, type_city, header_tab, header_title, header_conetnt;
    private ImageView imgae_right;
    private PinyinAdapter adapter;
    private ExpandableListView eListView;
    private AssortView assortView;
    private List<String> names=new ArrayList<String>();
    private List<String> namelist=new ArrayList<String>();
    private List<User> userList=new ArrayList<User>();
    private List<String> friendsRequstNamel=new ArrayList<String>();
    private List<FriendRequest> friendsRequstLsit=new ArrayList<FriendRequest>();
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == root) {
            root = inflater.inflate(R.layout.fragment_txl, null);
            initView();
            Constant.txlFragment=this;
            isPrepared = true;
            lazyLoad();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null) {
            parent.removeView(root);
        }
        return root;
    }

    public void initView() {
        appBaryx = (AppBaryx) root.findViewById(R.id.footbar);
        header_back = appBaryx.getHeader_back();
        type_city = appBaryx.getType_city();
        header_tab = appBaryx.getHeader_tab();
        header_title = appBaryx.getHeader_titletv();
        imgae_right = appBaryx.getImgae_right();
        header_conetnt = appBaryx.getHeader_conetnt();
        header_conetnt.setText("加好友");
        header_back.setText("消息中心");
        header_back.setVisibility(View.VISIBLE);
        header_conetnt.setVisibility(View.VISIBLE);
        type_city.setVisibility(View.GONE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);
        header_tab.setVisibility(View.GONE);

        eListView = (ExpandableListView) root.findViewById(R.id.elist);
        assortView = (AssortView) root.findViewById(R.id.assort);

        //字母按键回调
        assortView.setOnTouchAssortListener(new AssortView.OnTouchAssortListener() {

            View layoutView=LayoutInflater.from(getActivity())
                    .inflate(R.layout.alert_dialog_menu_layout, null);
            TextView text =(TextView) layoutView.findViewById(R.id.content);
            PopupWindow popupWindow ;

            public void onTouchAssortListener(String str) {
                int index=adapter.getAssort().getHashList().indexOfKey(str);
                if(index!=-1)
                {
                    eListView.setSelectedGroup(index);;
                }
                if(popupWindow!=null){
                    text.setText(str);
                }
                else
                {
                    popupWindow = new PopupWindow(layoutView,
                            100, 100,
                            false);
                    // 显示在Activity的根视图中心
                    popupWindow.showAtLocation(getActivity().getWindow().getDecorView(),
                            Gravity.CENTER, 0, 0);
                }
                text.setText(str);
            }

            public void onTouchAssortUP() {
                if(popupWindow!=null)
                    popupWindow.dismiss();
                popupWindow=null;
            }
        });
        header_conetnt.setOnClickListener(this);
        header_back.setOnClickListener(this);

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
//        initData();
        fetchMyRemoteFriend();
    }
    public void fetchMyRemoteFriend() {
        try{
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("user_id",  UserManager.getInstance(getActivity()).currentUser.userId);
            params.put("limit", 100);
            UserManager.getInstance(getActivity()).anSocial.sendRequest("friends/list.json", AnSocialMethod.GET, params, new IAnSocialCallback() {
                @Override
                public void onFailure(JSONObject response) {
                    Log.i("fetchMyRemoteFriend", response.toString());
                }

                @Override
                public void onSuccess(JSONObject response) {
                    userList.clear();
                    namelist.clear();
                    try {
                        JSONArray users = response.getJSONObject("response").getJSONArray("friends");
                        Log.i("fetchMyRemoteFriend好友列表", users.toString());
                        for(int i =0;i<users.length();i++){
                            JSONObject userJson = users.getJSONObject(i);
                            User user = new User(userJson);
                            boolean isMutual = userJson.getJSONObject("friendProperties").getBoolean("isMutual");
                            userList.add(user);
                            namelist.add(user.userName);
                        }
                        initData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (ArrownockException e) {
            e.printStackTrace();
        }
    }
    public void initData(){
        names.clear();
        names.add("#");
        names.add("#");
        names.addAll(namelist);
//        names.add("liu");
//        names.add("lwqdd");
//        names.add("阿宝");
//        names.add("A.l.N.Y");
//        names.add("安静");
//        names.add("鱼鱼");
//        names.add("妹妹");
//        names.add("爸爸");
//        names.add("林小姐");
//        names.add("卓静怡");
//        names.add("媛媛");
//        names.add("欢欢");
//        names.add("杨帆");
//        names.add("星星");
//        names.add("杨帅");
//        names.add("郑凯");
//        names.add("倒塌");
//        names.add("黑人");
//        names.add("小雅");
//        names.add("娟");
//        Log.i("好哟欧数量", names.toString());
        if(adapter==null){
            adapter = new PinyinAdapter(getActivity(),names,userList);
            eListView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
        //展开所有
        for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
            eListView.expandGroup(i);
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back:
                getActivity().finish();
                break;
            case R.id.header_conetnt:

                break;
        }
    }

    public  void update(User user) {
        Log.i("TxlFragment", "到底更新没");
        names.add(user.userName);
        userList.add(user);
//        adapter.notifyDataSetChanged();
        adapter = new PinyinAdapter(getActivity(),names,userList);
        eListView.setAdapter(adapter);
        //展开所有
        for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
            eListView.expandGroup(i);
        }
    }


    @Override
    public void onFinish(User user) {
        update(user);
    }
}