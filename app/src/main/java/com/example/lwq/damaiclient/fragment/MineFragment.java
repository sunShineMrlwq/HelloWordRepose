package com.example.lwq.damaiclient.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.activity.UserModule.LoginActivity;
import com.example.lwq.damaiclient.app.BaseFragment;
import com.example.lwq.damaiclient.constant.ConstantFinal;
import com.example.lwq.damaiclient.modle.IM.Activity.IMLoginctivity;
import com.example.lwq.damaiclient.modle.IM.Activity.IMMainActivity;
import com.example.lwq.damaiclient.utils.BlurUtil;
import com.example.lwq.damaiclient.utils.SharedPreferenceUtil;
import com.example.lwq.damaiclient.widget.AppBaryx;

/**
 * Created by lwq on 2015/12/4.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener{
    private View root;
    private AppBaryx appBaryx;
    private TextView header_back,type_city, header_tab, header_title,header_conetnt;
    private ImageView imgae_right,fragment_my_center_to;
    private RelativeLayout re_mine_yhj,re_mine_message,re_mine_map,re_mine_zxing;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null==root){
            root=inflater.inflate(R.layout.fragment_mine,null);
            initView();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null) {
            parent.removeView(root);
        }
        setBackground(R.mipmap.user);
        return root;
    }

    @Override
    protected void lazyLoad() {

    }
    public void initView() {//初始化布局
        appBaryx = (AppBaryx) root.findViewById(R.id.footbar);
        header_back= appBaryx.getHeader_back();
        type_city = appBaryx.getType_city();
        header_tab = appBaryx.getHeader_tab();
        header_title = appBaryx.getHeader_titletv();
        imgae_right = appBaryx.getImgae_right();
        header_conetnt= appBaryx.getHeader_conetnt();
        header_tab.setText("我的");
        header_conetnt.setText("退出登陆");
        header_back.setVisibility(View.GONE);
        header_conetnt.setVisibility(View.VISIBLE);
        type_city.setVisibility(View.GONE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);
        header_tab.setVisibility(View.VISIBLE);

        fragment_my_center_to = (ImageView) root.findViewById(R.id.fragment_my_center_to);
        re_mine_yhj = (RelativeLayout) root.findViewById(R.id.re_mine_yhj);
        re_mine_message = (RelativeLayout) root.findViewById(R.id.re_mine_message);
        re_mine_map = (RelativeLayout) root.findViewById(R.id.re_mine_map);
        re_mine_zxing = (RelativeLayout) root.findViewById(R.id.re_mine_zxing);

        header_conetnt.setOnClickListener(this);
        re_mine_yhj.setOnClickListener(this);
        re_mine_message.setOnClickListener(this);
        re_mine_map.setOnClickListener(this);
        re_mine_zxing.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_conetnt:
                ConstantFinal.outlogin=true;
                SharedPreferenceUtil.getInstance("USERINFO").detleData();
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), ConstantFinal.onlogin);
                break;
            case R.id.re_mine_yhj:
                break;
            case R.id.re_mine_message:
                startActivity(new Intent(getActivity(), IMLoginctivity.class));
                break;
            case R.id.re_mine_map:
                break;
            case R.id.re_mine_zxing:
                break;
        }
    }
    //设置背景虚化
    private void setBackground(int id){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), id);//从资源文件中得到图片，并生成Bitmap图片
        final Bitmap blurBmp = BlurUtil.fastblur(getActivity(), bmp, 10);//0-25，表示模糊值
        final Drawable newBitmapDrawable = new BitmapDrawable(blurBmp); // 将Bitmap转换为Drawable
        fragment_my_center_to.post(new Runnable(){//调用UI线程
            @Override
            public void run() {
                fragment_my_center_to.setBackgroundDrawable(newBitmapDrawable);//设置背景
            }
        });
    }
}