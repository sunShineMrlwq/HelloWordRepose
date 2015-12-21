package com.example.lwq.damaiclient.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lwq.damaiclient.R;
import com.example.lwq.damaiclient.adapter.RecommendAdapter;
import com.example.lwq.damaiclient.adapter.TabListPagerAdapter;
import com.example.lwq.damaiclient.app.BaseFragment;
import com.example.lwq.damaiclient.utils.ListViewHeightBased;
import com.example.lwq.damaiclient.widget.AppBaryx;
import com.example.lwq.damaiclient.widget.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwq on 2015/12/4.
 */
public class PerformFragment extends BaseFragment {
    private View root;
    private ViewPager mViewPager;
    private TabListPagerAdapter mTabsAdapter;
    private TabPageIndicator mTabPageIndicator;
    private AppBaryx appBaryx;
    private TextView type_city, header_tab, header_title,header_back;
    private ImageView imgae_right;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == root) {
            root = inflater.inflate(R.layout.fragment_perform, null);
            initView();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null) {
            parent.removeView(root);
        }
        initTab();
        return root;
    }

    public void initView() {//初始化布局
        appBaryx = (AppBaryx) root.findViewById(R.id.footbar);
        type_city = appBaryx.getType_city();
        header_tab = appBaryx.getHeader_tab();
        header_title = appBaryx.getHeader_titletv();
        imgae_right = appBaryx.getImgae_right();
        header_tab.setText("演出");
        header_tab.setVisibility(View.VISIBLE);
        type_city.setVisibility(View.VISIBLE);
        header_title.setVisibility(View.GONE);
        imgae_right.setVisibility(View.GONE);

        mTabPageIndicator = (TabPageIndicator) root.findViewById(R.id.category_strip);
        mViewPager = (ViewPager) root.findViewById(R.id.viewpager);
        //关闭预加载，默认一次只加载一个Fragment
        mViewPager.setOffscreenPageLimit(1);
    }

    //初始化tab
    public void initTab() {
        String[] tabText = new String[]{"全部分类", "演唱会", "音乐会", "话剧歌剧", "舞蹈芭蕾", "体育比赛"};
        //创建FragmentPagerAdapter适配器，并将指定要显示的fragemnt的控件
        mTabsAdapter = new TabListPagerAdapter(getChildFragmentManager(), tabText);
        mViewPager.setAdapter(mTabsAdapter);//给ViewPager设置适配器
        mViewPager.setCurrentItem(0);
        mTabPageIndicator.setViewPager(mViewPager);//获取指示器控件
    }

    @Override
    protected void lazyLoad() {

    }
}
