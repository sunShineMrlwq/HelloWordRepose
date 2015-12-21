package com.example.lwq.damaiclient.modle.IM.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.adapter.TabListPagerAdapter;
import com.example.lwq.damaiclient.app.BaseFragment;
import com.example.lwq.damaiclient.modle.IM.Activity.PyqActivity;
import com.example.lwq.damaiclient.widget.AppBaryx;
import com.example.lwq.damaiclient.widget.DmListView;
import com.example.lwq.damaiclient.widget.TabPageIndicator;

/**
 * Created by lwq on 2015/12/4.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener{
    View root;
    private DmListView dmListView;
    private AppBaryx appBaryx;
    private TextView header_back, type_city, header_tab, header_title, header_conetnt;
    private ImageView imgae_right;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    private RelativeLayout re_fx_sys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == root) {
            root = inflater.inflate(R.layout.fragment_im_my, null);
            initView();
            isPrepared = true;
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
        re_fx_sys = (RelativeLayout) root.findViewById(R.id.re_fx_sys);

        header_conetnt.setOnClickListener(this);
        header_back.setOnClickListener(this);
        re_fx_sys.setOnClickListener(this);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back:
                getActivity().finish();
                break;
            case R.id.re_fx_sys:
                startActivity(new Intent(getActivity(), PyqActivity.class));
                break;
            case R.id.header_conetnt:
                break;
        }
    }
}